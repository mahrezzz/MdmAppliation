package com.barin.mdmappliation.domain.interactor.usecase;

import javax.inject.Inject;
import com.barin.mdmappliation.domain.executor.PostExecutionThread;
import com.barin.mdmappliation.domain.executor.ThreadExecutor;
import com.barin.mdmappliation.domain.model.UploadMessage;
import com.barin.mdmappliation.domain.repository.UploadMessageToDbRepository;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by barin on 4/16/2016.
 */
public class UploadMessageCase extends UseCaseWithParams {

  private final UploadMessageToDbRepository uploadMessageToDbRepository;

  @Inject protected UploadMessageCase(UploadMessageToDbRepository uploadMessageToDbRepository,
      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.uploadMessageToDbRepository = uploadMessageToDbRepository;
  }

  @Override public void execute(Subscriber uploadMessageSubscriber, Object object) {
    this.subscription = uploadMessageToDbRepository.uploadMessageLogToDb((UploadMessage) object)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(uploadMessageSubscriber);
  }
}
