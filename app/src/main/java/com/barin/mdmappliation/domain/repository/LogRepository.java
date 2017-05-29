package com.barin.mdmappliation.domain.repository;

import java.util.Collection;
import com.barin.mdmappliation.domain.model.LogInfoDomain;
import rx.Observable;

/**
 * Created by barin on 3/10/2016.
 */
public interface LogRepository {

  Observable<Collection<LogInfoDomain>> getLogs();


}
