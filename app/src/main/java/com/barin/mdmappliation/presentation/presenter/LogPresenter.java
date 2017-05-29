package com.barin.mdmappliation.presentation.presenter;

import com.barin.mdmappliation.presentation.model.AppLog;
import com.barin.mdmappliation.presentation.view.LogListView;

/**
 * Created by barin on 3/10/2016.
 */
public interface LogPresenter extends Presenter {
  void bringLogsList();

  void onLogClicked(AppLog logInfo);

  void attachView(LogListView logListView);

  void detachView();

  void uploadLogsToServer();

}
