package com.barin.mdmappliation.data.exception;

/**
 * Created by barin on 5/3/2016.
 */
public class LogsNotFoundException extends Exception {


  public LogsNotFoundException(){

    super();
  }

  public LogsNotFoundException(final String message) {
    super(message);
  }

  public LogsNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public LogsNotFoundException(final Throwable cause) {
    super(cause);
  }




}
