package com.barin.mdmappliation.application.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import com.barin.mdmappliation.application.exception.MdmException;
import com.barin.mdmappliation.application.util.GoogleServicesUtil;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Named;
import com.barin.mdmappliation.R;
import com.barin.mdmappliation.application.AppLogger;
import com.barin.mdmappliation.application.MdmApplication;
import com.barin.mdmappliation.application.manager.MyLocalBroadcastManager;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.data.net.RegisterTokenResult;
import com.barin.mdmappliation.domain.exception.DefaultErrorBundle;
import com.barin.mdmappliation.domain.interactor.DefaultSubscriber;
import com.barin.mdmappliation.domain.interactor.usecase.UseCase;
import com.barin.mdmappliation.domain.interactor.usecase.UseCaseWithParams;
import com.barin.mdmappliation.domain.model.TokenDomain;
import com.barin.mdmappliation.presentation.di.component.DaggerServiceComponent;
import com.barin.mdmappliation.presentation.di.component.ServiceComponent;
import com.barin.mdmappliation.presentation.di.module.ServiceModule;
import com.barin.mdmappliation.presentation.model.GcmToken;
import com.barin.mdmappliation.presentation.model.mapper.GcmTokenMapper;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by barin on 4/17/2016.
 */
public class TokenHandleService extends BaseService {

  ServiceComponent mServiceComponent;
  @Inject @Named("getToken") UseCase getTokenCase;
  @Inject @Named("registerTokenToDatabase") UseCaseWithParams registerTokenToDatabaseUseCase;

  @Inject WifiManager wifiManager;
  @Inject SharedPreferences sharedPreferences;
  @Inject AppLogger appLogger;
  @Inject MyLocalBroadcastManager myLocalBroadcastManager;
  @Inject GcmTokenMapper gcmTokenMapper;
  @Inject GoogleServicesUtil googleServicesUtil;

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    try {
      handleTokenRegistration();
    } catch (MdmException e) {
      appLogger.uploadLogToDb(String.format(Locale.getDefault(), "%s : %s", e.getMessage(),
          wifiManager.getConnectionInfo().getMacAddress()));
      Timber.e("TokenHandleService:", e);
    }
    return START_NOT_STICKY;
  }

  private void handleTokenRegistration() throws MdmException {
    setupInjector();

    Timber.d(getString(R.string.token_service_called));

    if (!googleServicesUtil.isGooglePlayServicesAvailable()) {
      throw new MdmException(getString(R.string.google_play_service_not_available));
    }

    if (sharedPreferences.getString(Constants.SHARED_PREFERENCES_TOKEN, "").equalsIgnoreCase("")) {
      Timber.d("Token shared preference da mevcut değil. Gidip alalım o zaman");
      getTokenCase.execute(new GetTokenCaseSubscriber());
    } else if (!sharedPreferences.getString(Constants.SHARED_PREFERENCES_TOKEN, "")
        .equalsIgnoreCase("") && !sharedPreferences.getBoolean(
        Constants.SHARED_PREFERENCES_REGISTER_STATUS, false)) {
      Timber.d(
          "Token shared preference da mevcut ama database e kaydedilmemiş. Gidip kaydedelim o zaman");
      registerTokenToDatabase(
          getGcmToken(sharedPreferences.getString(Constants.SHARED_PREFERENCES_TOKEN, "")));
    }
  }

  private void setupInjector() {
    mServiceComponent = DaggerServiceComponent.builder()
        .applicationComponent(((MdmApplication) getApplicationContext()).getApplicationComponent())
        .serviceModule(new ServiceModule(this))
        .build();
    mServiceComponent.inject(this);
  }

  public void registerTokenToDatabase(GcmToken gcmToken) {
    AppUtil.checkForNull(registerTokenToDatabaseUseCase, "registerTokenCase==null");
    AppUtil.checkForNull(gcmToken, "gcmToken==null");
    registerTokenToDatabaseUseCase.execute(new RegisterTokenSubscriber(), gcmToken);
  }

  public GcmToken getGcmToken(String token) {
    String macID = wifiManager.getConnectionInfo().getMacAddress();
    GcmToken gcmToken = new GcmToken(token);
    gcmToken.setMacId(macID);
    return gcmToken;
  }

  private final class GetTokenCaseSubscriber extends Subscriber<TokenDomain> {

    @Override public void onCompleted() {

      String tokenInShared = sharedPreferences.getString(Constants.SHARED_PREFERENCES_TOKEN, "");
      if (!tokenInShared.equalsIgnoreCase("")) {
        registerTokenToDatabase(getGcmToken(tokenInShared));
      }
    }

    @Override public void onError(Throwable e) {
      Timber.e(getString(R.string.token_not_get), e);
      showError(new DefaultErrorBundle((Exception) e));
      appLogger.uploadLogToDb(e.getMessage());
      informActivity(getString(R.string.token_not_get));

      stopSelf();
    }

    @Override public void onNext(TokenDomain tokenDomain) {
      if (tokenDomain != null) {
        GcmToken token = gcmTokenMapper.transform(tokenDomain);
        sharedPreferences.edit()
            .putString(Constants.SHARED_PREFERENCES_TOKEN, token.getToken())
            .apply();
        Timber.i(getString(R.string.token) + token.getToken(),
            AppUtil.prepareInfoLogFormat(new Object() {
            }));
      }
    }
  }

  private final class RegisterTokenSubscriber extends DefaultSubscriber<RegisterTokenResult> {

    RegisterTokenResult mRegisterTokenResult;

    @Override public void onNext(RegisterTokenResult registerTokenResult) {
      mRegisterTokenResult = registerTokenResult;
    }

    @Override public void onError(Throwable e) {
      Timber.e(getString(R.string.register_token_error), e);
      showError(new DefaultErrorBundle((Exception) e));
      appLogger.uploadLogToDb(getString(R.string.token_is_got_not_registered) + e.getMessage());
      informActivity(getString(R.string.token_is_got_not_registered));
      stopSelf();
    }

    @Override public void onCompleted() {
      if (mRegisterTokenResult != null) {
        if (mRegisterTokenResult.getSuccess().equalsIgnoreCase("true")) {
          sharedPreferences.edit()
              .putBoolean(Constants.SHARED_PREFERENCES_REGISTER_STATUS, true)
              .apply();
          logOnNext("@RegisterTokenSubscriber", getString(R.string.token_uploaded_message));

          appLogger.uploadLogToDb(getString(R.string.token_uploaded_message)
              + "\t Token:"
              + sharedPreferences.getString(Constants.SHARED_PREFERENCES_TOKEN, ""));

          informActivity("");
        } else {
          String errorMessage = mRegisterTokenResult.getResponse() == null ? ""
              : mRegisterTokenResult.getResponse().toString();
          showMessage(errorMessage);

          logOnNext("@RegisterTokenSubscriber", getString(R.string.token_is_got_server_refused));
          informActivity(getString(R.string.token_is_got_server_refused));
        }
      }
      stopSelf();
    }
  }

  public void informActivity(String message) {
    Intent toolbarRefreshIntent = new Intent(Constants.TOOLBAR_REFRESH_INTENT_FILTER);
    toolbarRefreshIntent.putExtra(Constants.REFRESH_MESSAGE, message);
    myLocalBroadcastManager.sendBroadcast(toolbarRefreshIntent);
  }

  @Override public void onDestroy() {

    if (getTokenCase != null) {
      getTokenCase.unsubscribe();
    }
    if (registerTokenToDatabaseUseCase != null) {
      registerTokenToDatabaseUseCase.unsubscribe();
    }
    mServiceComponent = null;
    super.onDestroy();
    Timber.i("TokenHandleService onDestroy called..");
  }
}
