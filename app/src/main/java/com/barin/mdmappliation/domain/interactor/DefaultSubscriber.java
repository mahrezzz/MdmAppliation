package com.barin.mdmappliation.domain.interactor;

import java.util.Locale;
import com.barin.mdmappliation.application.util.AppUtil;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by barin on 3/10/2016.
 */
public class DefaultSubscriber<T> extends Subscriber<T> {

  @Override public void onCompleted() {
    // no-op by default.
  }

  @Override public void onError(Throwable e) {
    // no-op by default.
  }

  @Override public void onNext(T t) {
    // no-op by default.
  }

  public void logOnNext(String name, String message) {

    Timber.i(String.format(Locale.getDefault(), "%s\n@OnNext %s:", message, name),
        AppUtil.prepareInfoLogFormat(new Object() {
        }));
  }

  public void logOnCompleted(String name, String message) {

    Timber.i(String.format("%s\n@OnCompleted %s", message, name),
        AppUtil.prepareInfoLogFormat(new Object() {
        }));
  }

  public void logOnError(int id, String name, String exceptionMessage,Exception e) {


    Timber.e(String.format(Locale.getDefault(), "CommandID:(%d)\n@onError %s:%s", id, name,
        exceptionMessage), e);
  }
}