package com.barin.mdmappliation.application.util;

import android.content.Context;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by barin on 4/29/2016.
 */
@Singleton public class GoogleServicesUtil {

  private final Context mContext;

  @Inject public GoogleServicesUtil(Context context) {

    this.mContext = context;



  }

  public boolean isGooglePlayServicesAvailable() {
    return GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext)
        == ConnectionResult.SUCCESS
        || GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext)
        == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED;


  }
}
