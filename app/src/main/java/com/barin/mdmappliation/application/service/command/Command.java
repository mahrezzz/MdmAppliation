package com.barin.mdmappliation.application.service.command;

import java.util.Arrays;
import java.util.List;
import com.barin.mdmappliation.application.util.Constants;

public class Command {

  public enum CommandType {
    PUSH_FILE(0),            //upload logs to the server via sftp channel
    PULL_FILE(1),            //pull  any file from given server and
    NOTIFICATION(2);         //just show the message to the user via notification.
    public int type;

    CommandType(int type) {
      this.type = type;
    }

    public String getCommandTypeName() {

      switch (type) {
        case 0:
          return "Push File";
        case 1:
          return "Pull File";
        case 2:
          return "Notification";
        default:
          throw new RuntimeException("cannot be happen");
      }
    }
  }

  public CommandType mType;
  private List<String> mDetails;
  private int mCommandID;

  private boolean mStatus = false;

  private String mStatusFailReason;
  private String mCommandExtraInfo;

  public Command(int type, String parameters, int commandId) {
    mCommandID = commandId;
    for (CommandType commandType : CommandType.values()) {
      if (commandType.type == type) {
        mType = commandType;
        break;
      }
    }
    mDetails = Arrays.asList(parameters.split(Constants.COMMAND_SEPARATOR));
  }

  public CommandType getType() {
    return mType;
  }

  public List<String> getDetails() {
    return mDetails;
  }

  public void setStatus(boolean status) {
    mStatus = status;
  }

  public void setStatusFailReason(String failMessage) {
    mStatusFailReason = failMessage;
  }

  public void setmCommandInfo(String commandExtraInfo) {
    mCommandExtraInfo = commandExtraInfo;
  }

  public int getmCommandID() {
    return mCommandID;
  }

  @Override public String toString() {

    StringBuilder detail = new StringBuilder();
    detail.append("Command ID:(");
    detail.append(mCommandID);
    detail.append(")\tType:");
    detail.append(mType.getCommandTypeName());
    detail.append("\tStatus:");
    detail.append(mStatus);
    detail.append("\tParameters:");
    detail.append(getDetails().toString());

    if (!mStatus && mStatusFailReason != null) {
      detail.append("\tFail Message:");
      detail.append(mStatusFailReason);
    } else if (mStatus && mCommandExtraInfo != null) {
      detail.append("\tResult:");
      detail.append(mCommandExtraInfo);
    }

    return detail.toString();
  }
}
