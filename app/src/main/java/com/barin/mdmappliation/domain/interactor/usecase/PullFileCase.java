package com.barin.mdmappliation.domain.interactor.usecase;

import javax.inject.Inject;
import com.barin.mdmappliation.application.service.command.Command;
import com.barin.mdmappliation.domain.executor.PostExecutionThread;
import com.barin.mdmappliation.domain.executor.ThreadExecutor;
import com.barin.mdmappliation.domain.repository.PullFileRepository;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by barin on 4/11/2016.
 */
public class PullFileCase extends UseCaseWithParams {

  private final PullFileRepository mPullFileRepository;

  @Inject
  protected PullFileCase(PullFileRepository pullFileRepository, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    mPullFileRepository = pullFileRepository;
  }

  @Override public void execute(Subscriber pullFileSubscriber, Object object) {
    this.subscription = mPullFileRepository.pullFileFromServerObservable((Command) object)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(pullFileSubscriber);
  }
}
