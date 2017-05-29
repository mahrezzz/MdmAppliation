package com.barin.mdmappliation.domain.repository;

import rx.Observable;

/**
 * Created by barin on 4/6/2016.
 */
public interface DeleteRecordsRepository {
  Observable<Integer> deleteLogs(String tableName);
}
