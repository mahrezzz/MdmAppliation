package com.barin.mdmappliation.presentation.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.barin.mdmappliation.R;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.application.util.ToolbarColorizeHelper;
import com.barin.mdmappliation.presentation.di.ProvideComponent;
import com.barin.mdmappliation.presentation.di.component.ActivityComponent;
import com.barin.mdmappliation.presentation.di.component.HasComponent;
import com.barin.mdmappliation.presentation.model.AppLog;
import com.barin.mdmappliation.presentation.view.fragment.LogListFragment;
import timber.log.Timber;

public class RemoteControlActivity <C,T> extends BaseActivity
    implements HasComponent<ActivityComponent>,ProvideComponent<C,T>,LogListFragment.LogClicked {

  //ActivityComponent activityComponent;
  ToolbarRefreshReceiver toolbarRefreshReceiver;

  @Bind(R.id.tool_bar) Toolbar myToolbar;
  @Bind(R.id.footer) Toolbar myFooter;
  @Bind(R.id.tool_bar_title) TextView myToolbarTitle;
  @Bind(R.id.footer_text_message) TextView myFooterMessage;



  public static Intent getCallingIntent(Context context) {
    return new Intent(context, RemoteControlActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_remote);
    /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/

    ButterKnife.bind(this);
    toolbarRefreshReceiver = new ToolbarRefreshReceiver();
    //setupInjectors();
    setupViews();

    if (sharedPreferences.getString(Constants.SHARED_PREFERENCES_TOKEN, "").equalsIgnoreCase("")) {
      Timber.d("Token mevcut değil token alma servisi çalışacak");
      //startService(new Intent(this, TokenHandleService.class));
    }
  }

/*  private void setupInjectors() {
    activityComponent = DaggerActivityComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .build();
  }*/

  private void setupViews() {
    setSupportActionBar(myToolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setDisplayUseLogoEnabled(true);
      getSupportActionBar().setLogo(R.mipmap.ic_launcher);

      getSupportActionBar().setTitle("");
    }

    ToolbarColorizeHelper.colorTheTextOnToolbar(myToolbar, Color.WHITE);
    myToolbarTitle.setText(getApplicationTitle());
    myToolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), "Capture_it.ttf"));
    addFragment(R.id.fragmentLogList, LogListFragment.newInstance(null));
  }

  @Override public void onLogClicked(AppLog logInfo) {
    showMessage(logInfo.toString() + " is clicked....");
  }

  @Override protected void onResume() {

    if (myLocalBroadcastManager != null) {
      myLocalBroadcastManager.registerReceiver(toolbarRefreshReceiver,
          new IntentFilter(Constants.TOOLBAR_REFRESH_INTENT_FILTER));
    }

    super.onResume();
  }

  @Override protected void onPause() {
    super.onPause();
    myLocalBroadcastManager.unregisterReceiver(toolbarRefreshReceiver);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
    //activityComponent = null;
  }

  @Override public ActivityComponent getComponent() {
    //return activityComponent;
    return null;
  }

  @Override public C provideComponent(T type) {

    if(type instanceof String){
        return (C) "test";
    }
    throw new RuntimeException("cannnot be happen");
  }

  private class ToolbarRefreshReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {

      Timber.d("Remote Activity ToolbarRefreshReceiver is called");
      String comingMessage = intent.getStringExtra(Constants.REFRESH_MESSAGE);
      if (comingMessage.equalsIgnoreCase("")) {
        tokenRegistered = true;
        myFooter.setVisibility(View.GONE);
        invalidateOptionsMenu();
      } else {
        if (myFooterMessage.getVisibility() == View.GONE) {
          myFooterMessage.setVisibility(View.VISIBLE);
        }
        myFooterMessage.setText(comingMessage);
      }
    }
  }
}
