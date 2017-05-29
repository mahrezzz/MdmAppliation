package com.barin.mdmappliation.application.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.barin.mdmappliation.R;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.application.util.Constants;
import timber.log.Timber;

/**
 */
public class NotificationReceiver extends BroadcastReceiver {

  @Override public void onReceive(Context context, Intent intent) {

    switch (intent.getAction()) {
      case Constants.READ_ACTION:
        int notificationId = (int) intent.getExtras().get(Constants.NOTIFICATION_ID_TAG);
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(
            notificationId);
        Timber.d(context.getString(R.string.token_with_message_received) + notificationId,
            AppUtil.prepareInfoLogFormat(new Object() {
            }));
        break;
      case Constants.EXTRA_ACTION:
        break;
      default:
        throw new RuntimeException(context.getString(R.string.logical_mistake));
    }
  }
}
