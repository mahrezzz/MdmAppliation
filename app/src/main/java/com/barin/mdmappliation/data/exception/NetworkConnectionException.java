package com.barin.mdmappliation.data.exception;

/**
 * Created by barin on 5/3/2016.
 */
public class NetworkConnectionException extends Exception {

  public NetworkConnectionException() {
    super();
  }

  public NetworkConnectionException(final String message) {
    super(message);
  }

  public NetworkConnectionException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public NetworkConnectionException(final Throwable cause) {
    super(cause);
  }
}
