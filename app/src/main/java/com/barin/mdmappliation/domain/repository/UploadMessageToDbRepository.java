package com.barin.mdmappliation.domain.repository;

import com.barin.mdmappliation.domain.model.UploadMessage;
import rx.Observable;

/**
 * Created by barin on 4/16/2016.
 */
public interface UploadMessageToDbRepository {

  Observable<Void> uploadMessageLogToDb(UploadMessage uploadMessage);
}
