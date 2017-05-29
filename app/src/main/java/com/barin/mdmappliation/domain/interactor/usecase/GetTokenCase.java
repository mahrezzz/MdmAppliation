package com.barin.mdmappliation.domain.interactor.usecase;

import javax.inject.Inject;
import com.barin.mdmappliation.domain.executor.PostExecutionThread;
import com.barin.mdmappliation.domain.executor.ThreadExecutor;
import com.barin.mdmappliation.domain.repository.GetTokenRepository;
import rx.Observable;

/**
 * Created by barin on 3/25/2016.
 */
public class
    GetTokenCase extends UseCase {

  private final GetTokenRepository getTokenRepository;

  @Inject GetTokenCase(GetTokenRepository getTokenRepository, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.getTokenRepository = getTokenRepository;
  }

  @Override protected Observable buildUseCaseObservable() {
    return getTokenRepository.getToken();
  }
}
