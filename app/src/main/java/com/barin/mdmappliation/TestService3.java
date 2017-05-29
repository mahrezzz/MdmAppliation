package com.barin.mdmappliation;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.barin.mdmappliation.application.AppLogger;
import com.barin.mdmappliation.application.FakeParameterCreater;
import com.barin.mdmappliation.application.HowProper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;
import timber.log.Timber;

public class TestService3 extends Service implements ICommandStatusCallback {
  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  ServiceComponent serviceComponent;
  @Inject CommandFactory commandFactory;
  @Inject AppLogger appLogger;

  @Inject @Named("log_upload") UseCaseWithParams logUploadCase;
  @Inject @Named("pull_file") UseCaseWithParams pullFileCase;

  List<String> notificationParameters = new ArrayList<>();
  List<String> pushnParameters = new ArrayList<>();
  List<String> pullParameters = new ArrayList<>();

  public void initialize() {
    FakeParameterCreater.initiliaze();
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    initialize();
    //FakeParameterCreater.corruptServerSettings();

    notificationParameters = FakeParameterCreater.getNotificationParameters(HowProper.PROPER);
    pushnParameters = FakeParameterCreater.getPushParameters(HowProper.PROPER);
    pullParameters = FakeParameterCreater.getPullParameters(HowProper.PROPER);

    testObservableZip(pushnParameters, 0);


    //testObservableZip(pullParameters, 1);
    /*testObservableZip(notificationParameters, 2);
*/

    stopSelf();
    return START_NOT_STICKY;
  }

  private void testObservableZip(List<String> parameters, int type) {
    long timeNow = System.currentTimeMillis();
    Bundle dataNotification = new Bundle();
    Observable.zip(Observable.range(0, parameters.size()), Observable.interval(5, TimeUnit.SECONDS),
        (integer, aLong) -> integer).map(integer -> {
      switch (type) {
        case 2:
          dataNotification.putString("commandId", "333");
          dataNotification.putString("type", "2");
          dataNotification.putString("parameters", notificationParameters.get(integer));
          break;
        case 1:
          dataNotification.putString("commandId", "666");
          dataNotification.putString("type", "1");
          dataNotification.putString("parameters", pullParameters.get(integer));
          break;
        case 0:
          dataNotification.putString("commandId", "999");
          dataNotification.putString("type", "0");
          dataNotification.putString("parameters", pushnParameters.get(integer));
          break;
        default:
          break;
      }

      test(dataNotification);

      return "parameters:"
          + parameters.toString()
          + "  "
          + (System.currentTimeMillis() - timeNow)
          + "[ms]";
    }).subscribe(s -> Timber.d(s));
  }

  public void test(Bundle data) {

    int id = Integer.parseInt(data.getString("type", "" + Integer.MAX_VALUE));
    int commandID = Integer.parseInt(data.getString("commandId", "" + Integer.MAX_VALUE));
    String parameters = data.getString("parameters", "").trim();



    Timber.i("TestService Parameters:" + parameters);

    if (id != Integer.MAX_VALUE && !parameters.equalsIgnoreCase("")) {
      if (commandFactory == null) {
        setupInjection();
      }

      Command commandReceived = new Command(id, parameters, commandID);

      String commandMessage = String.format("%s is received!", commandReceived.toString());
      Timber.i(commandMessage, AppUtil.prepareInfoLogFormat(new Object() {
      }));
      appLogger.uploadLogToDb(commandMessage);

      ICommand command;
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
          throw new RuntimeException("Cannot be happen logical mistake..");
      }
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

  @Override public void getCommandStatus(String commandStatus) {
    Timber.i("@TestService3: " + commandStatus);
    appLogger.uploadLogToDb(commandStatus);
  }
}
