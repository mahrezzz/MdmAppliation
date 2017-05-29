package com.barin.mdmappliation.presentation.model;

/**
 * Created by barin on 2/9/2016.
 */
public class AppLog {

  public enum LogType {
    ERROR("E"), INFO("I");
    public String type;

    LogType(String type) {
      this.type = type;
    }
  }

  private String message;
  private String detail;
  private LogType logType;
  private String date;

  public AppLog(String message, String detail, LogType logType) {
    this.message = message;
    this.detail = detail;
    this.logType = logType;
  }

  public String getMessage() {
    return message == null ? "" : message;
  }

  public String getmDate() {
    return date == null ? "" : date;
  }

  public LogType getmType() {
    return logType;
  }

  public String getmDetail() {
    return detail == null ? "" : detail;
  }

  @Override public String toString() {
    return "AppLog{" +
        "message='" + message + '\'' +
        ", detail='" + detail + '\'' +
        ", logType=" + logType +
        ", date='" + date + '\'' +
        '}';
  }

  public void setmDate(String mDate) {
    this.date = mDate;
  }
}
