package com.barin.mdmappliation.application;

import android.util.Log;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import com.barin.mdmappliation.R;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.domain.interactor.DefaultSubscriber;
import com.barin.mdmappliation.domain.interactor.usecase.UseCaseWithParams;
import com.barin.mdmappliation.domain.model.UploadMessage;
import com.barin.mdmappliation.presentation.model.AppLog;
import timber.log.Timber;

@Singleton public class AppLogger {

  private final UseCaseWithParams saveLogUseCase;
  private final UseCaseWithParams uploadLogMessage;

  @Inject public AppLogger(@Named("saveLog") UseCaseWithParams saveLogUseCase,
      @Named("upload_message_to_db") UseCaseWithParams uploadLogMessageCase) {
    this.saveLogUseCase = saveLogUseCase;
    this.uploadLogMessage = uploadLogMessageCase;
  }

  public void uploadLogToDb(String message) {
    uploadLogMessage.execute(new UploadLogSubscriber(), new UploadMessage(message));
  }

  public void log(AppLog log) {
    saveLogUseCase.execute(new SaveLogSubscriber(), log);
  }

  public void logE(String errorMessage, Exception ex) {

    String stackTrace = AppUtil.getStackTrace(ex.getStackTrace());
    log(convertToAppLog(errorMessage + " {" + ex.getMessage() + "}", "Stacktrace:" + stackTrace,
        AppLog.LogType.ERROR));
  }

  public void logI(String message, String detay) {
    AppUtil.checkForNull(message, "@LogI info==null");
    log(convertToAppLog(message, detay, AppLog.LogType.INFO));
  }

  public AppLog convertToAppLog(String message, String detailMessage, AppLog.LogType type) {
    return new AppLog(message, detailMessage, type);
  }

  private final class SaveLogSubscriber extends DefaultSubscriber<Boolean> {

    private final String TAG = SaveLogSubscriber.class.getSimpleName();

    @Override public void onNext(Boolean aBoolean) {
      Log.d(TAG, "SaveLogSubscriber: " + aBoolean);
    }

    @Override public void onError(Throwable e) {
      Log.d(TAG, "SaveLogSubscriber Error:" + e.getMessage());
    }

    @Override public void onCompleted() {
      Log.d(TAG, "SaveLogSubscriber onCompleted");
    }
  }

  private final class UploadLogSubscriber extends DefaultSubscriber<Void> {

    @Override public void onCompleted() {
      super.onCompleted();
      Timber.d(AppStringGetter.STRING_GETTER.getString(R.string.log_upload_completed));
    }

    @Override public void onError(Throwable e) {
      super.onError(e);
      Timber.e(AppStringGetter.STRING_GETTER.getString(R.string.token_getting_error_upload_error),
          e);
    }
  }
}
