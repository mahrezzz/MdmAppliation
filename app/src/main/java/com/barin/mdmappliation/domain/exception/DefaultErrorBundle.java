package com.barin.mdmappliation.domain.exception;

/**
 * Created by barin on 3/15/2016.
 */
public class DefaultErrorBundle implements ErrorBundle {

  private static final String DEFAULT_ERROR_MSG = "an undefined material is coming, Sir";

  private final Exception mException;

  public DefaultErrorBundle(Exception e) {

    mException = e;
  }

  @Override public Exception getException() {
    return mException;
  }

  @Override public String getErrorMessage() {
    return mException == null ? DEFAULT_ERROR_MSG : mException.getMessage();
  }
}
