package com.barin.mdmappliation.domain.repository;

import com.barin.mdmappliation.application.service.command.Command;
import rx.Observable;

/**
 * Created by barin on 3/29/2016.
 */
public interface LogUploadRepository extends ConnectionBasedRepository {

  Observable<Boolean> getUploadResult(Command command);
}
