package com.barin.mdmappliation.application.service.command.pull_file_command;

import android.support.annotation.NonNull;
import com.barin.mdmappliation.application.service.command.Command;
import com.barin.mdmappliation.application.service.command.ICommand;
import com.barin.mdmappliation.application.service.command.ICommandStatusCallback;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.domain.interactor.DefaultSubscriber;
import com.barin.mdmappliation.domain.interactor.usecase.UseCaseWithParams;
import com.barin.mdmappliation.domain.model.PullFileInfoDomain;

public class PullFileCommand implements ICommand {

  private Command mCommand;

  UseCaseWithParams mPullFileCase;
  ICommandStatusCallback mCommandStatusCallback;

  public PullFileCommand(@NonNull Command command, UseCaseWithParams pullFileCase) {
    AppUtil.checkForNull(command, "command==null");
    mPullFileCase = pullFileCase;


    mCommand = command;
  }

  @Override public void execute(ICommandStatusCallback commandStatusCallback) {
    mCommandStatusCallback = commandStatusCallback;
    mPullFileCase.execute(new PullFileSubscriber(), mCommand);
  }

  private final class PullFileSubscriber extends DefaultSubscriber<PullFileInfoDomain> {

    private boolean mResult = false;

    @Override public void onCompleted() {
      logOnCompleted("Pull File", mCommand.toString());
      mCommand.setStatus(mResult);
      mCommandStatusCallback.getCommandStatus(mCommand.toString());
    }

    @Override public void onError(Throwable e) {
      mCommand.setStatus(false);
      String exceptionMessage = "";
      if (e.getCause() != null && e.getCause().getMessage() != null) {
        exceptionMessage = e.getCause().getMessage();
      } else if (e.getMessage() != null) {
        exceptionMessage = e.getMessage();
      }
      logOnError(mCommand.getmCommandID(), "Pull File", exceptionMessage, (Exception) e);
      mCommand.setStatusFailReason(exceptionMessage);
      mCommandStatusCallback.getCommandStatus(mCommand.toString());
    }

    @Override public void onNext(PullFileInfoDomain result) {
      if (result.isSuccess()) {
        result.createSuccessMessage();
        mResult = true;
      }
      mCommand.setmCommandInfo(result.isSuccess() ? result.getSuccessMessage()
          : "Push File command operation is NOT successful");

      /*mCommandStatusCallback.getCommandStatus(mCommand.toString());
      logOnNext("Pull File", mCommand.toString());*/
    }
  }
}
