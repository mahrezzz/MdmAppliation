package com.barin.mdmappliation.presentation.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.domain.exception.DefaultErrorBundle;
import com.barin.mdmappliation.domain.exception.ErrorBundle;
import com.barin.mdmappliation.domain.interactor.DefaultSubscriber;
import com.barin.mdmappliation.domain.interactor.usecase.UseCase;
import com.barin.mdmappliation.domain.model.LogInfoDomain;
import com.barin.mdmappliation.presentation.di.scope.PerActivity;
import com.barin.mdmappliation.presentation.exception.ErrorMessageFactory;
import com.barin.mdmappliation.presentation.model.AppLog;
import com.barin.mdmappliation.presentation.model.mapper.LogDataMapper;
import com.barin.mdmappliation.presentation.view.LogListView;
import timber.log.Timber;

/**
 * is responsible for interacting with domain layer and notify the views from here.
 */
@PerActivity public class LogListPresenterImpl implements LogPresenter {

  LogListView logListView;
  private final UseCase getLogsListUseCase;
  private final LogDataMapper logDataMapper;


  @Inject public LogListPresenterImpl(@Named("logList") UseCase getLogsListUseCase,
      LogDataMapper logDataMapper) {
    this.getLogsListUseCase = getLogsListUseCase;
    this.logDataMapper = logDataMapper;
  }

  @Override public void destroy() {
    this.getLogsListUseCase.unsubscribe();  //prevent memory leaks
    this.logListView = null;
  }

  @Override public void bringLogsList() {
    logListView.showLoading();
    getLogsListUseCase.execute(new GetLogListSubscriber());
  }

  @Override public void onLogClicked(AppLog logInfo) {

  }

  @Override public void attachView(@NonNull LogListView logListView) {
    this.logListView = logListView;
  }

  @Override public void detachView() {
    getLogsListUseCase.unsubscribe();
    logListView = null;
  }

  @Override public void uploadLogsToServer() {

    String testHostName;
    String testUserName;
    String testServerPath;

    if (Constants.LOCAL_TEST) {
      testHostName = "10.0.3.2";
      testUserName = "tester";
      testServerPath = "/tabletmdm555/";
    } else {
      testHostName = "testhostname";
      testUserName = "tbllog";
      testServerPath = "/export/home/tbllog/tabletmdm/";
    }

    String parameters = "/TestLogs/logs/"
        + Constants.COMMAND_SEPARATOR
        + "manager_logs.db"
        + Constants.COMMAND_SEPARATOR
        + testServerPath
        + Constants.COMMAND_SEPARATOR
        + testUserName
        + Constants.COMMAND_SEPARATOR
        + testHostName;

    Bundle localCommandBundle = new Bundle();
    localCommandBundle.putString("type", "0");
    localCommandBundle.putString("parameters", parameters);
    localCommandBundle.putString("commandId", "-369");
    logListView.startLocalGcmService(localCommandBundle);
  }

  private void showErrorMessage(ErrorBundle errorBundle) {
    String errorMessage =
        ErrorMessageFactory.create(this.logListView.context(), errorBundle.getException());
    logListView.showErrorMessage(errorMessage);
  }

  //---------SUBSCRIBERS PART HERE-----------------------------
  private final class GetLogListSubscriber extends DefaultSubscriber<List<LogInfoDomain>> {

    @Override public void onCompleted() {
      logListView.hideLoading();
    }

    @Override public void onError(Throwable e) {  //UseErrorFactory Pattern here..

      LogListPresenterImpl.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
      LogListPresenterImpl.this.logListView.hideLoading();
      Timber.e("@GetLogListSubscriber  onError:", e);
    }

    @Override public void onNext(List<LogInfoDomain> logInfoDomains) {
      logListView.loadLogs(logDataMapper.transform(logInfoDomains));
    }
  }
}
