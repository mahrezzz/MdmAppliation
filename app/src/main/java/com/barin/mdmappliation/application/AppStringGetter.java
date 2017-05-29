package com.barin.mdmappliation.application;

import android.content.Context;

/**
 * Created by barin on 4/19/2016.
 */
public enum AppStringGetter {
  STRING_GETTER;
  private static Context mContext;

  public synchronized void setContext(Context context) {
    if (mContext == null) {
      mContext = context;
    }
  }

  public synchronized String getString(int resourceID) {
    return mContext.getResources().getString(resourceID);
  }

}
