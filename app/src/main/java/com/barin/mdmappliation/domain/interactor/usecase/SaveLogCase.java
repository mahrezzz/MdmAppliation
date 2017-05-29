package com.barin.mdmappliation.domain.interactor.usecase;

import javax.inject.Inject;
import com.barin.mdmappliation.presentation.model.AppLog;
import com.barin.mdmappliation.domain.executor.PostExecutionThread;
import com.barin.mdmappliation.domain.executor.ThreadExecutor;
import com.barin.mdmappliation.domain.repository.SaveLogRepository;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by barin on 4/1/2016.
 */
public class SaveLogCase extends UseCaseWithParams {

  private final SaveLogRepository mSaveLogRepository;

  @Inject protected SaveLogCase(SaveLogRepository saveLogRepository, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    mSaveLogRepository = saveLogRepository;
  }

  @Override public void execute(Subscriber saveLogSubscriber, Object object) {

    this.subscription = mSaveLogRepository.saveLog((AppLog) object)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(saveLogSubscriber);
  }
}
