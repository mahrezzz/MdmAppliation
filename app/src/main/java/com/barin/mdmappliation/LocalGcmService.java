package com.barin.mdmappliation;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import com.barin.mdmappliation.application.MdmApplication;
import com.barin.mdmappliation.application.manager.MyLocalBroadcastManager;
import com.barin.mdmappliation.application.service.command.Command;
import com.barin.mdmappliation.application.service.command.CommandFactory;
import com.barin.mdmappliation.application.service.command.ICommand;
import com.barin.mdmappliation.application.service.command.ICommandStatusCallback;
import com.barin.mdmappliation.application.service.command.log_command.LogUploadCommand;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.domain.interactor.usecase.UseCaseWithParams;
import com.barin.mdmappliation.presentation.di.component.DaggerServiceComponent;
import com.barin.mdmappliation.presentation.di.component.ServiceComponent;
import com.barin.mdmappliation.presentation.di.module.ServiceModule;
import rx.Subscription;
import timber.log.Timber;

public class LocalGcmService extends Service implements ICommandStatusCallback {

  @Inject CommandFactory commandFactory;
  @Inject MyLocalBroadcastManager myLocalBroadcastManager;


  Subscription logUploadSubscription;
  ServiceComponent serviceComponent;


  @Inject @Named("log_upload") UseCaseWithParams logUploadCase;

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {

    Bundle comingBundle = intent.getBundleExtra("bundle");
    AppUtil.checkForNull(comingBundle, "localCommand==null");

    uploadLogs(comingBundle);

    return START_NOT_STICKY;
  }

  @Override public void getCommandStatus(String commandStatus) {
    Intent intent = new Intent(Constants.COMMAND_BACK_INTENT_FILTER);
    intent.putExtra(Constants.REFRESH_MESSAGE, commandStatus.trim());
    myLocalBroadcastManager.sendBroadcast(intent);

    intent = new Intent(Constants.TOOLBAR_REFRESH_INTENT_FILTER);
    intent.putExtra(Constants.REFRESH_MESSAGE, commandStatus.trim());
    myLocalBroadcastManager.sendBroadcast(intent);

    stopSelf();
  }

  @Override public void onDestroy() {



    if (logUploadSubscription != null && !logUploadSubscription.isUnsubscribed()) {
      logUploadSubscription.unsubscribe();
      Timber.d("LocalGcmService onDestroy is called and subscription will be unsubscribed");
    }

    serviceComponent = null;
    super.onDestroy();


  }

  public void uploadLogs(Bundle data) {
    if (serviceComponent == null) {
      setupInjection();
    }

    int id = Integer.parseInt(data.getString("type", "" + Integer.MAX_VALUE));
    int commandID = Integer.parseInt(data.getString("commandId", "" + Integer.MAX_VALUE));
    String parameters = data.getString("parameters", "").trim();

    if (id != Integer.MAX_VALUE && !parameters.equalsIgnoreCase("")) {
      if (commandFactory == null) {
        setupInjection();
      }

      Command commandReceived = new Command(id, parameters, commandID);
      Timber.i(String.format("%s local command received!", commandReceived.toString()),
          AppUtil.prepareInfoLogFormat(new Object() {
          }));
      ICommand command = new LogUploadCommand(commandReceived, logUploadCase);
      commandFactory.handleCommand(command, this);
    }
  }

  private void setupInjection() {
    serviceComponent = DaggerServiceComponent.builder()
        .applicationComponent(((MdmApplication) getApplicationContext()).getApplicationComponent())
        .serviceModule(new ServiceModule(this))
        .build();
    serviceComponent.inject(this);
  }
}
