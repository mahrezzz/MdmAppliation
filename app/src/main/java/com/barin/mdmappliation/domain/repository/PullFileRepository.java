package com.barin.mdmappliation.domain.repository;

import com.barin.mdmappliation.application.service.command.Command;
import com.barin.mdmappliation.domain.model.PullFileInfoDomain;
import rx.Observable;

/**
 * Created by barin on 4/11/2016.
 */
public interface PullFileRepository extends ConnectionBasedRepository {

  Observable<PullFileInfoDomain> pullFileFromServerObservable(Command command);
}
