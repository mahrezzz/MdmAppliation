package com.barin.mdmappliation.domain.repository;

import com.barin.mdmappliation.presentation.model.AppLog;
import rx.Observable;

/**
 * Created by barin on 4/1/2016.
 */
public interface SaveLogRepository {
  Observable<Boolean> saveLog(AppLog log);
}
