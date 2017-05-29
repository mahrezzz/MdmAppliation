package com.barin.mdmappliation.application.service;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;
import com.barin.mdmappliation.R;
import com.barin.mdmappliation.application.AppLogger;
import com.barin.mdmappliation.application.MdmApplication;
import com.barin.mdmappliation.application.service.command.Command;
import com.barin.mdmappliation.application.service.command.CommandFactory;
import com.barin.mdmappliation.application.service.command.ICommand;
import com.barin.mdmappliation.application.service.command.ICommandStatusCallback;
import com.barin.mdmappliation.application.service.command.log_command.LogUploadCommand;
import com.barin.mdmappliation.application.service.command.notification_command.NotificationCreateCommand;
import com.barin.mdmappliation.application.service.command.pull_file_command.PullFileCommand;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.domain.interactor.usecase.UseCaseWithParams;
import com.barin.mdmappliation.presentation.di.component.DaggerServiceComponent;
import com.barin.mdmappliation.presentation.di.component.ServiceComponent;
import com.barin.mdmappliation.presentation.di.module.ServiceModule;
import javax.inject.Inject;
import javax.inject.Named;
import timber.log.Timber;

public class GcmService extends GcmListenerService implements ICommandStatusCallback {

  ServiceComponent mServiceComponent;
  @Inject CommandFactory commandFactory;
  @Inject AppLogger appLogger;

  @Inject @Named("log_upload") UseCaseWithParams logUploadCase;
  @Inject @Named("pull_file") UseCaseWithParams pullFileCase;

  @Override public void onMessageReceived(String from, Bundle data) {
    super.onMessageReceived(from, data);

    Log.d("@@", "GCMService is called.");
    int id = Integer.parseInt(data.getString("type", "" + Integer.MAX_VALUE));
    int commandID = Integer.parseInt(data.getString("commandId", "" + Integer.MAX_VALUE));
    String parameters = data.getString("parameters", "").trim();

    Timber.i("GcmService parameters:" + parameters);


    if (id != Integer.MAX_VALUE && !parameters.equalsIgnoreCase("")) {
      if (commandFactory == null) {
        setupInjection();
      }

      Command commandReceived = new Command(id, parameters, commandID);
      Timber.i(String.format("%s is received!", commandReceived.toString()),
          AppUtil.prepareInfoLogFormat(new Object() {
          }));

      appLogger.uploadLogToDb(commandReceived.toString());
      ICommand command = null;

      switch (commandReceived.getType()) {
        case PUSH_FILE:
          command = new LogUploadCommand(commandReceived, logUploadCase);
          break;
        case PULL_FILE:
          command = new PullFileCommand(commandReceived, pullFileCase);
          break;
        case NOTIFICATION:
          command = new NotificationCreateCommand(commandReceived, getApplicationContext());
          break;
        default:
          commandReceived.setStatus(false);
          commandReceived.setStatusFailReason(getString(R.string.wrong_command_structure));
          appLogger.uploadLogToDb(commandReceived.toString());
          break;
      }
      commandFactory.handleCommand(command, this);
    }
  }

  private void setupInjection() {
    mServiceComponent = DaggerServiceComponent.builder()
        .applicationComponent(((MdmApplication) getApplicationContext()).getApplicationComponent())
        .serviceModule(new ServiceModule(this))
        .build();
    mServiceComponent.inject(this);
  }

  @Override public void getCommandStatus(String commandStatus) {
    appLogger.uploadLogToDb(commandStatus);
    Timber.i("GCM Service Command result:" + commandStatus);
  }
}
