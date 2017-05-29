package com.barin.mdmappliation.domain.interactor.usecase;

import javax.inject.Inject;
import com.barin.mdmappliation.application.service.command.Command;
import com.barin.mdmappliation.domain.executor.PostExecutionThread;
import com.barin.mdmappliation.domain.executor.ThreadExecutor;
import com.barin.mdmappliation.domain.repository.LogUploadRepository;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by barin on 3/29/2016.
 */
public class LogUploadCase extends UseCaseWithParams {

  private final LogUploadRepository mLogUploadRepository;

  @Inject
  protected LogUploadCase(LogUploadRepository logUploadRepository, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    mLogUploadRepository = logUploadRepository;
  }

  @Override public void execute(Subscriber registerTokenSubscriber, Object object) {
    this.subscription = mLogUploadRepository.getUploadResult((Command) object)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(registerTokenSubscriber);
  }
}
