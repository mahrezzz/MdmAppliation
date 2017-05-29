package com.barin.mdmappliation.presentation.model.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import com.barin.mdmappliation.domain.model.LogInfoDomain;
import com.barin.mdmappliation.presentation.di.scope.PerActivity;
import com.barin.mdmappliation.presentation.model.AppLog;
import rx.Observable;

/**
 * Created by barin on 3/10/2016.
 */

@PerActivity public class LogDataMapper {

  @Inject public LogDataMapper() {
  }

  public AppLog transform(LogInfoDomain log) {
    if (log == null) {
      throw new IllegalArgumentException("Cannot transform a null value");
    }

    AppLog logInfo = new AppLog(log.getMessage(),log.getmDetail(), log.getmType());
    logInfo.setmDate(log.getmDate());
    return logInfo;
  }

  public List<AppLog> transform(List<LogInfoDomain> logs) {
    List<AppLog> transformedLogs;
    if (logs != null && !logs.isEmpty()) {
      transformedLogs = new ArrayList<>();

      Observable.from(logs).forEach(log -> transformedLogs.add(transform(log)));
      return transformedLogs;
    }
    return Collections.emptyList();
  }
}
