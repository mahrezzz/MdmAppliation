/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.barin.mdmappliation.presentation.view.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import com.barin.mdmappliation.application.MdmApplication;
import com.barin.mdmappliation.presentation.di.component.ActivityComponent;
import com.barin.mdmappliation.presentation.di.component.DaggerActivityComponent;
import com.barin.mdmappliation.presentation.di.component.HasComponent;
import com.barin.mdmappliation.presentation.di.module.ActivityModule;
import com.barin.mdmappliation.presentation.model.AppLog;

/**
 * Base {@link Fragment} class for every fragment in this application.
 */
public abstract class BaseFragment extends Fragment {

  AlertDialog.Builder alertDialogBuilder;
  AlertDialog alertDialog;

  ActivityComponent activityComponent;

  @Override public void onActivityCreated(Bundle savedInstanceState) {

    alertDialogBuilder = new AlertDialog.Builder(getActivity());
    alertDialogBuilder.setCancelable(true);
    alertDialogBuilder.setIcon(android.R.drawable.ic_menu_info_details);
    alertDialogBuilder.setOnCancelListener(dialog -> {
      dialog.cancel();
      alertDialog = null;
    });
    super.onActivityCreated(savedInstanceState);
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
  }

  public void inject(LogListFragment logListFragment) {

    activityComponent = DaggerActivityComponent.builder()
        .applicationComponent(
            ((MdmApplication) getActivity().getApplicationContext()).getApplicationComponent())
        .activityModule(new ActivityModule(getActivity()))
        .build();

    activityComponent.inject(logListFragment);
  }

  protected void showAlert(AppLog appLog) {
    alertDialog = alertDialogBuilder.create();
    alertDialog.setTitle("Detay");
    alertDialog.setMessage(appLog.getmDetail());
    alertDialog.show();
  }

  /**
   * Shows a {@link Toast} message.
   *
   * @param message An string representing a message to be shown.
   */

  protected void showToastMessage(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }

  protected <C> C getComponent(Class<C> componentType) {

    return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
  }


/*  protected <C,T> C provideComponent(Class<T> componentType) {
    return componentType.cast(((ProvideComponent<C,T>) getActivity()).getComponent());
  }*/



  @Override public void onDestroy() {
    super.onDestroy();
    activityComponent = null;
  }



}
