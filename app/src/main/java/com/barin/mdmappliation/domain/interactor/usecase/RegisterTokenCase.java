package com.barin.mdmappliation.domain.interactor.usecase;

import android.support.annotation.NonNull;
import javax.inject.Inject;
import com.barin.mdmappliation.domain.executor.PostExecutionThread;
import com.barin.mdmappliation.domain.executor.ThreadExecutor;
import com.barin.mdmappliation.domain.repository.RegisterTokenRepository;
import com.barin.mdmappliation.presentation.model.GcmToken;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by barin on 3/28/2016.
 */
public class RegisterTokenCase extends UseCaseWithParams {

  private final RegisterTokenRepository registerTokenRepository;

  @Inject protected RegisterTokenCase(RegisterTokenRepository registerTokenRepository,
      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.registerTokenRepository = registerTokenRepository;
  }

  @Override public void execute(@NonNull Subscriber registerTokenSubscriber, Object object) {
    this.subscription = registerTokenRepository.registerTokenToDatabase((GcmToken) object)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler())
        .subscribe(registerTokenSubscriber);
  }
}
