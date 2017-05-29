package com.barin.mdmappliation.presentation.view;

/**
 * Created by barin on 3/10/2016.
 */
public interface LoadView {

  void showLoading();

  void hideLoading();

  void showErrorMessage(String errorMessage);

  void showMessage(String message);
}
