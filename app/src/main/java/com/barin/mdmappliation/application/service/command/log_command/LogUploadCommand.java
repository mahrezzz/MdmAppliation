package com.barin.mdmappliation.application.service.command.log_command;

import android.support.annotation.NonNull;
import com.barin.mdmappliation.application.service.command.Command;
import com.barin.mdmappliation.application.service.command.ICommand;
import com.barin.mdmappliation.application.service.command.ICommandStatusCallback;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.domain.interactor.DefaultSubscriber;
import com.barin.mdmappliation.domain.interactor.usecase.UseCaseWithParams;

public class LogUploadCommand implements ICommand {

  private Command mCommand;

  UseCaseWithParams mLogUploadCase;
  ICommandStatusCallback mCommandStatusCallback;

  public LogUploadCommand(@NonNull Command command, UseCaseWithParams logUploadCase) {

    AppUtil.checkForNull(command, "command==null");
    mLogUploadCase = logUploadCase;
    mCommand = command;
  }

  @Override public void execute(ICommandStatusCallback commandStatusCallback) {
    mCommandStatusCallback = commandStatusCallback;
    mLogUploadCase.execute(new LogUploadSubscriber(), mCommand);
  }

  private final class LogUploadSubscriber extends DefaultSubscriber<Boolean> {

    private boolean result;

    @Override public void onCompleted() {
      logOnCompleted("File Upload", mCommand.toString());
      mCommand.setStatus(result);
      mCommandStatusCallback.getCommandStatus(mCommand.toString());
    }

    @Override public void onError(Throwable e) {
      String exceptionMessage = "";
      result = false;
      if (e.getCause() != null && e.getCause().getMessage() != null) {
        exceptionMessage = e.getCause().getMessage();
      } else if (e.getMessage() != null) {
        exceptionMessage = e.getMessage();
      }

      logOnError(mCommand.getmCommandID(), "File Upload", exceptionMessage, (Exception) e);
      mCommand.setStatusFailReason(exceptionMessage);
      mCommandStatusCallback.getCommandStatus(mCommand.toString());
    }

    @Override public void onNext(Boolean uploadResult) {
      //logOnNext("File Upload", mCommand.toString());
      result = uploadResult;
    }
  }
}
