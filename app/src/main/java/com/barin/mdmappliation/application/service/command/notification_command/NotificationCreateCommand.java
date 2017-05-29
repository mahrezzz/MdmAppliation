package com.barin.mdmappliation.application.service.command.notification_command;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.barin.mdmappliation.R;
import com.barin.mdmappliation.application.service.command.Command;
import com.barin.mdmappliation.application.service.command.ICommand;
import com.barin.mdmappliation.application.service.command.ICommandStatusCallback;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.application.util.Constants;
import timber.log.Timber;

public class NotificationCreateCommand implements ICommand {

  private Command mCommand;
  private Context mContext;
  ICommandStatusCallback mCommandStatusCallback;

  public NotificationCreateCommand(Command command, Context context) {
    AppUtil.checkForNull(command, "command==null");

    mCommand = command;
    mContext = context;
  }

  @Override public void execute(ICommandStatusCallback commandStatusCallback) {
    mCommandStatusCallback = commandStatusCallback;
    showNotification(mCommand);
  }

  public boolean showNotification(Command command) {

    AppUtil.checkForNull(command, "notification command==null");
    if (command.getDetails().size() < 2) {
      throw new RuntimeException("notification command size cannot be smaller than 2");
    }
    if (command.getDetails().get(1).trim().equalsIgnoreCase("UpdateService")) {
      Intent serviceUpdate = new Intent("com.barin.mdmappliation.START_DB_UPDATE");
      mContext.sendBroadcast(serviceUpdate);
    } else {
      String notificationMessage = command.getDetails().get(1);
      String notificationTitle = command.getDetails().get(0);
      Map<Integer, PendingIntent> notificationIntents;
      notificationIntents = new HashMap<>();

      notificationIntents.put(0, getIntent(notificationMessage.hashCode(), 0));
      notificationIntents.put(1, getIntent(notificationMessage.hashCode(), 1));

      Notification notification =
          new NotificationCompat.Builder(mContext).setSmallIcon(android.R.drawable.ic_dialog_email)
              .setAutoCancel(true)
              .setContentTitle(notificationTitle)
              .setOngoing(true)
              .setContentText(notificationMessage)
              .setDefaults(Notification.DEFAULT_ALL)
              .addAction(android.R.drawable.sym_action_chat,
                  mContext.getResources().getString(R.string.readed), notificationIntents.get(0))
              .addAction(android.R.drawable.sym_action_call,
                  mContext.getResources().getString(R.string.extra), notificationIntents.get(1))
              .build();

      NotificationManager notificationManager =
          (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

      Timber.i(String.format(Locale.getDefault(), "Notification  with id:(%d) is created",

          notificationMessage.hashCode()));

      notificationManager.notify(notificationMessage.hashCode(), notification);
      mCommand.setStatus(true);
      String commandMessage =
          String.format(Locale.getDefault(), "Notification  with id:(%d) is showed successfully",
              notificationMessage.hashCode());
      Timber.i(commandMessage);

      mCommand.setmCommandInfo(commandMessage);
      mCommandStatusCallback.getCommandStatus(mCommand.toString());
    }

    return true;
  }

  public PendingIntent getIntent(int i, int actionType) {

    Intent buttonIntent = new Intent();
    switch (actionType) {
      case 0:
        buttonIntent.putExtra(Constants.NOTIFICATION_ID_TAG, i);
        buttonIntent.setAction(Constants.READ_ACTION);
        break;
      case 1:
        buttonIntent.putExtra(Constants.NOTIFICATION_ID_TAG, i);
        buttonIntent.setAction(Constants.EXTRA_ACTION);
        break;
      default:
        throw new RuntimeException("switch logic is mistaken");
    }

    return PendingIntent.getBroadcast(mContext, i, buttonIntent, PendingIntent.FLAG_ONE_SHOT);
  }
}
