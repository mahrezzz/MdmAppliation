package com.barin.mdmappliation.application.receiver;

import android.content.Context;
import android.content.Intent;
import com.barin.mdmappliation.application.service.TokenHandleService;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.application.util.Constants;
import timber.log.Timber;



public class RegistrationReceiver extends BaseReceiver {

  @Override public void onReceive(Context context, Intent intent) {

    try {
      Timber.i("@RegistrationReceiver called!!", AppUtil.prepareInfoLogFormat(new Object() {
      }));
      if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) || intent.getAction()
          .equals(Intent.ACTION_PACKAGE_REPLACED) || intent.getAction()
          .equalsIgnoreCase(Constants.REGISTRATION_RECEIVER_INTENT_ACTION)) {

        if (context.getPackageName().equals(intent.getData().getSchemeSpecificPart())
            || intent.getStringExtra("package").equalsIgnoreCase(context.getPackageName())) {
          context.startService(new Intent(context, TokenHandleService.class));
        }
      }
    } catch (Exception ex) {
      Timber.e("Aradığım hata bu benim:", ex);
    }
  }
}
