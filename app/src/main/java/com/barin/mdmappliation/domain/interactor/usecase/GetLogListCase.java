package com.barin.mdmappliation.domain.interactor.usecase;

import javax.inject.Inject;
import com.barin.mdmappliation.domain.executor.PostExecutionThread;
import com.barin.mdmappliation.domain.executor.ThreadExecutor;
import com.barin.mdmappliation.domain.repository.LogRepository;
import rx.Observable;

/**
 * Created by barin on 3/10/2016.
 */
public class GetLogListCase extends UseCase {

  private LogRepository logRepository;

  @Inject public GetLogListCase(LogRepository repository, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.logRepository = repository;
  }

  @Override
  public Observable buildUseCaseObservable() {   //do want you want just return a observable that's it.
    return logRepository.getLogs();
  }



}
