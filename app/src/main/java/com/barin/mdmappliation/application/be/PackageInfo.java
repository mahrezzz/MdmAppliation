package com.barin.mdmappliation.application.be;

import android.content.Context;
import android.content.pm.PackageManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.barin.mdmappliation.R;

/**
 * Created by barin on 4/18/2016.
 * Purpose is to give package related information of the application.
 */
@Singleton public class PackageInfo {

  private final Context mContext;

  @Inject public PackageInfo(Context context) {

    mContext = context;
  }

  public String getPackageVersion() {
    String versionCode = "@";
    try {
      versionCode +=
          mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
      return versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return "";
  }

  public String getApplicationTitle() {
    return mContext.getResources().getString(R.string.app_name) + getPackageVersion();
  }
}
