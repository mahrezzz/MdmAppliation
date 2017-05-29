package com.barin.mdmappliation.application.service;

import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.gms.iid.InstanceIDListenerService;
import javax.inject.Inject;
import com.barin.mdmappliation.R;
import com.barin.mdmappliation.application.MdmApplication;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.presentation.di.component.DaggerServiceComponent;
import com.barin.mdmappliation.presentation.di.component.ServiceComponent;
import com.barin.mdmappliation.presentation.di.module.ServiceModule;
import com.barin.mdmappliation.presentation.model.mapper.GcmTokenMapper;
import timber.log.Timber;

public class InstanceIdListenerService extends InstanceIDListenerService {

  @Inject SharedPreferences sharedPreferences;
  @Inject GcmTokenMapper gcmTokenMapper;
  private ServiceComponent serviceComponent;

  @Override public void onTokenRefresh() {

    Timber.i(getString(R.string.token_refresh_service_called),
        AppUtil.prepareInfoLogFormat(new Object() {
        }));



    if (serviceComponent == null || sharedPreferences == null) {
      setupInjection();
    }


    sharedPreferences.edit().putString(Constants.SHARED_PREFERENCES_TOKEN, "").apply();
    startService(new Intent(getBaseContext(), TokenHandleService.class));

  }

  private void setupInjection() {
    serviceComponent = DaggerServiceComponent.builder()
        .applicationComponent(((MdmApplication) getApplicationContext()).getApplicationComponent())
        .serviceModule(new ServiceModule(this))
        .build();
    serviceComponent.inject(this);
  }
}