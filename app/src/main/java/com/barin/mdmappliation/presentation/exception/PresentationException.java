package com.barin.mdmappliation.presentation.exception;

/**
 * Created by barin on 3/7/2016.
 */
public class PresentationException extends Exception {

  public PresentationException(String detailMessage) {
    super(detailMessage);
  }

  public String getStrackTrace() {
    return "";
  }
}
