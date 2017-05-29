package com.barin.mdmappliation.domain.exception;

/**
 * Created by barin on 3/15/2016.
 */
public interface ErrorBundle {

  Exception getException();

  String getErrorMessage();
}
