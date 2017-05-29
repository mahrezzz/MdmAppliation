package com.barin.mdmappliation.application.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.barin.mdmappliation.application.util.AppUtil;

/**
 * Created by barin on 4/18/2016.
 * Purpose of this class to provide messaging locally in the app.
 * Receivers should be register/unregister to the same local broadcast manager
 *
 */
@Singleton public class MyLocalBroadcastManager {

  private final LocalBroadcastManager localBroadcastManager;

  @Inject public MyLocalBroadcastManager(Context context) {
    localBroadcastManager = LocalBroadcastManager.getInstance(context);
  }

  public void registerReceiver(BroadcastReceiver receiver, IntentFilter intentFilter) {
    AppUtil.checkForNull(localBroadcastManager, "localBroadcastManager==null");
    localBroadcastManager.registerReceiver(receiver, intentFilter);
  }

  public void unregisterReceiver(BroadcastReceiver receiver) {
    AppUtil.checkForNull(localBroadcastManager, "localBroadcastManager==null");
    localBroadcastManager.unregisterReceiver(receiver);
  }


  public void sendBroadcast(Intent intent) {
    localBroadcastManager.sendBroadcast(intent);
  }
}
