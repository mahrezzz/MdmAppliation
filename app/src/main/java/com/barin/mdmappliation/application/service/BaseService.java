package com.barin.mdmappliation.application.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.barin.mdmappliation.domain.exception.ErrorBundle;
import com.barin.mdmappliation.presentation.exception.ErrorMessageFactory;

public abstract class BaseService extends Service {

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  public void showMessage(String message) {
    toast(message);
  }

  public void showError(ErrorBundle errorBundle) {
    String errorMessage =
        ErrorMessageFactory.create(getApplicationContext(), errorBundle.getException());
    toast(errorMessage);
  }

  public void toast(String message) {
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  }
}
