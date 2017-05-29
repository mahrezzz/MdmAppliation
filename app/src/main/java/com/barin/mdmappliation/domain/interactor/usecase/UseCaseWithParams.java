package com.barin.mdmappliation.domain.interactor.usecase;

import com.barin.mdmappliation.domain.executor.PostExecutionThread;
import com.barin.mdmappliation.domain.executor.ThreadExecutor;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by barin on 3/28/2016.
 */
public abstract class UseCaseWithParams {

  public abstract void execute(Subscriber registerTokenSubscriber, Object object);

  public final ThreadExecutor threadExecutor;
  public final PostExecutionThread postExecutionThread;

  public Subscription subscription = Subscriptions.empty();

  protected UseCaseWithParams(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    this.threadExecutor = threadExecutor;
    this.postExecutionThread = postExecutionThread;
  }

  public void unsubscribe() {
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }
}
