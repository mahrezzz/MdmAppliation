package com.barin.mdmappliation.presentation.view.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.List;
import javax.inject.Inject;
import com.barin.mdmappliation.LocalGcmService;
import com.barin.mdmappliation.R;
import com.barin.mdmappliation.application.manager.MyLocalBroadcastManager;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.presentation.model.AppLog;
import com.barin.mdmappliation.presentation.presenter.LogListPresenterImpl;
import com.barin.mdmappliation.presentation.view.LogListView;
import com.barin.mdmappliation.presentation.view.adapter.DividerItemDecoration;
import com.barin.mdmappliation.presentation.view.adapter.LogListAdapter;
import com.barin.mdmappliation.presentation.view.adapter.UsersLayoutManager;

public class LogListFragment extends BaseFragment implements LogListView {

  @Inject LogListPresenterImpl logListPresenter;
  @Inject LogListAdapter logListAdapter;
  @Inject SharedPreferences sharedPreferences;
  @Inject WifiManager wifiManager;
  @Inject MyLocalBroadcastManager myLocalBroadcastManager;

  private CommandRefresherReceiver commandRefresherReceiver;

  @Bind(R.id.btnRefreshLogs) Button btnRefreshLogs;
  @Bind(R.id.btnTest) Button btnTest;
  @Bind(R.id.btnUploadLogs) Button btnUploadLogs;
  @Bind(R.id.listLogs) RecyclerView rvLogs;
  @Bind(R.id.rl_progress) RelativeLayout progressBarLayout;

  @OnClick(R.id.btnRefreshLogs) void refreshLogs() {
    logListPresenter.bringLogsList();
  }

  @OnClick(R.id.btnTest) void testRegister() {
   //...
  }

  @OnClick(R.id.btnUploadLogs) void uploadLogsToServer() {
    //this will be used only if token is not registered or not taken thus upload the logs to the server to know the reason why?
    if (sharedPreferences.getString(Constants.SHARED_PREFERENCES_TOKEN, "").equalsIgnoreCase("")) {
      showLoading();
      logListPresenter.uploadLogsToServer();
    } else {
      showMessage("Token is available no need to upload logs manually ");
    }
  }

  LogClicked logClicked;

  public interface LogClicked {    //replace this with EventBus later
    void onLogClicked(AppLog logInfo);
  }

  public LogListFragment() {
    setRetainInstance(true);
  }

  public static LogListFragment newInstance(Bundle bundle) {
    LogListFragment logListFragment = new LogListFragment();
    if (bundle != null) {
      logListFragment.setArguments(bundle);
    }
    return logListFragment;
  }

  @SuppressWarnings("deprecation") @Override public void onAttach(Activity activity) {
    super.onAttach(activity);

    if (activity instanceof LogClicked) {
      logClicked = (LogClicked) activity;    //to talk with activity, we are providing an interface
    }
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    inject(this);
    //this.getComponent(ActivityComponent.class).inject(this);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View fragmentView = inflater.inflate(R.layout.fragment_log_list, container, false);
    ButterKnife.bind(this, fragmentView);

    setupViews();
    return fragmentView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    commandRefresherReceiver = new CommandRefresherReceiver();
  }

  @Override public void onResume() {
    super.onResume();
    this.logListPresenter.attachView(this);
    logListPresenter.bringLogsList();

    myLocalBroadcastManager.registerReceiver(commandRefresherReceiver,
        new IntentFilter(Constants.COMMAND_BACK_INTENT_FILTER));
  }

  @Override public void onPause() {
    super.onPause();
    this.logListPresenter.detachView();
    myLocalBroadcastManager.unregisterReceiver(commandRefresherReceiver);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    rvLogs.setAdapter(null);
    ButterKnife.unbind(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    this.logListPresenter.destroy();
  }

  @Override public void onDetach() {
    super.onDetach();
  }

  private void setupViews() {
    this.logListAdapter.setOnItemClickListener(onItemClickListener);
    this.rvLogs.setLayoutManager(new UsersLayoutManager(context()));
    this.rvLogs.addItemDecoration(new DividerItemDecoration(getActivity(), null));
   /* this.rvLogs.addItemDecoration(
        new SimpleDividerItemDecoration(getActivity().getApplicationContext()));*/
    this.rvLogs.setAdapter(logListAdapter);
  }

  @Override public void loadLogs(@NonNull List<AppLog> logs) {

    if (logs.size() > -1) {
      logListAdapter.setLogCollection(logs);
    }
  }

  @Override public Context context() {
    return (getActivity()).getApplicationContext();
  }

  @Override public void startLocalGcmService(Bundle bundle) {

    Intent localGcmServiceIntent = new Intent(getActivity(), LocalGcmService.class);
    localGcmServiceIntent.putExtra("bundle", bundle);
    getActivity().startService(localGcmServiceIntent);
  }

  @Override public void showLoading() {
    this.progressBarLayout.setVisibility(View.VISIBLE);
    this.getActivity().setProgressBarIndeterminateVisibility(true);
  }

  @Override public void hideLoading() {
    this.progressBarLayout.setVisibility(View.GONE);
    this.getActivity().setProgressBarIndeterminateVisibility(false);
  }

  @Override public void showErrorMessage(String errorMessage) {
    this.showToastMessage(errorMessage);
  }

  @Override public void showMessage(String message) {
    this.showToastMessage(message);
  }

  private LogListAdapter.OnItemClickListener onItemClickListener = logInfo -> {
    showAlert(logInfo);
    if (this.logListPresenter != null && logInfo != null) {
      this.logListPresenter.onLogClicked(logInfo);
    }
  };

  private class CommandRefresherReceiver extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {
      hideLoading();
      if (intent.getStringExtra(Constants.REFRESH_MESSAGE) != null) {
        showMessage(intent.getStringExtra(Constants.REFRESH_MESSAGE));
      }
      logListPresenter.bringLogsList();
    }
  }
}
