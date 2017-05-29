package com.barin.mdmappliation.domain.model;

import com.barin.mdmappliation.presentation.model.AppLog;

/**
 * Created by barin on 3/10/2016.
 */

public class LogInfoDomain {

  private String detail;
  private AppLog.LogType logType;
  private String date;
  private String message;

  public LogInfoDomain(String message, String logDetail, AppLog.LogType type, String mDate) {
    this.detail = logDetail;
    this.logType = type;
    this.date = mDate;
    this.message = message;
  }

  public String getmDetail() {
    return detail;
  }

  public AppLog.LogType getmType() {
    return logType;
  }

  public String getmDate() {
    return date;
  }

  public String getMessage() {
    return message;
  }

  @Override public String toString() {
    return "LogInfoDomain{" +
        "detail='" + detail + '\'' +
        ", logType=" + logType +
        ", date='" + date + '\'' +
        ", message='" + message + '\'' +
        '}';
  }
}
