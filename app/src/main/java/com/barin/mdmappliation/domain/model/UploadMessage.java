package com.barin.mdmappliation.domain.model;

/**
 * Created by barin on 4/16/2016.
 */
public class UploadMessage {

  public static final String MESSAGE_TAG = "em";
  private String message;

  public UploadMessage(String message) {
    this.message = message;
  }

  @Override public String toString() {
    return message;
  }
}
