package com.barin.mdmappliation.application.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import com.barin.mdmappliation.application.MdmApplication;
import com.barin.mdmappliation.presentation.di.component.ApplicationComponent;
import com.barin.mdmappliation.presentation.di.component.ReceiverComponent;
import com.barin.mdmappliation.presentation.di.module.BroadcastModule;


public abstract class BaseReceiver extends BroadcastReceiver {

  ReceiverComponent receiverComponent;

  public BaseReceiver() {
  }

  /**
   * Get an Activity module for dependency injection.
   */
  protected BroadcastModule getBroadcastModule() {
    return new BroadcastModule(this);
  }

  protected ApplicationComponent getApplicationComponent(Context context) {
    return ((MdmApplication) context.getApplicationContext()).getApplicationComponent();
  }

}



