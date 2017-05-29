package com.barin.mdmappliation.domain.interactor.usecase;

import javax.inject.Inject;
import com.barin.mdmappliation.domain.executor.PostExecutionThread;
import com.barin.mdmappliation.domain.executor.ThreadExecutor;
import com.barin.mdmappliation.domain.repository.DeleteRecordsRepository;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by barin on 4/6/2016.
 */
public class DeleteTableRecordsCase extends UseCaseWithParams {

  private final DeleteRecordsRepository mDeleteLogsRepository;

  @Inject
  protected DeleteTableRecordsCase(DeleteRecordsRepository deleteLogsRepository,
      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.mDeleteLogsRepository = deleteLogsRepository;
  }

  @Override public void execute(Subscriber deleteLogsSubscriber, Object object) {
    this.subscription = mDeleteLogsRepository.deleteLogs((String) object)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(deleteLogsSubscriber);
  }
}
