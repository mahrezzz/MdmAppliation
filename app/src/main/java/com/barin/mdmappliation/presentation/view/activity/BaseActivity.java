package com.barin.mdmappliation.presentation.view.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.barin.mdmappliation.application.AppLogger;
import javax.inject.Inject;
import javax.inject.Named;
import com.barin.mdmappliation.R;
import com.barin.mdmappliation.application.MdmApplication;
import com.barin.mdmappliation.application.be.PackageInfo;
import com.barin.mdmappliation.application.manager.MyLocalBroadcastManager;
import com.barin.mdmappliation.application.service.TokenHandleService;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.data.repository.datasource.database.table.TblLog;
import com.barin.mdmappliation.domain.interactor.DefaultSubscriber;
import com.barin.mdmappliation.domain.interactor.usecase.UseCaseWithParams;
import com.barin.mdmappliation.presentation.di.component.ApplicationComponent;
import com.barin.mdmappliation.presentation.di.module.ActivityModule;
import com.barin.mdmappliation.presentation.navigation.Navigator;
import timber.log.Timber;

/**
 * Created by barin on 3/7/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

  @Inject Navigator navigator;
  @Inject SharedPreferences sharedPreferences;
  @Inject MyLocalBroadcastManager myLocalBroadcastManager;
  @Inject PackageInfo packageInfo;
  @Inject AppLogger appLogger;

  @Inject @Named("delete_records") UseCaseWithParams delUseCaseWithParams;

  boolean tokenRegistered = false;
  private static StringBuilder result;

  static {
    result = new StringBuilder();
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getApplicationComponent().inject(this);
  }

  public String getApplicationTitle() {
    return packageInfo.getApplicationTitle();
  }

  public void showMessage(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  protected void addFragment(int containerViewId, Fragment fragment) {
    FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
    fragmentTransaction.add(containerViewId, fragment);
    fragmentTransaction.commit();
  }

  protected ApplicationComponent getApplicationComponent() {
    return ((MdmApplication) getApplication()).getApplicationComponent();
  }

  protected ActivityModule getActivityModule() {
    return new ActivityModule(this);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.main_menu, menu);

    if (!tokenRegistered && sharedPreferences.getString(Constants.SHARED_PREFERENCES_TOKEN, "")
        .equalsIgnoreCase("")) {
      menu.findItem(R.id.action_status).setIcon(R.drawable.not_ok);
    } else {
      menu.removeItem(R.id.action_sync);
      menu.findItem(R.id.action_status).setIcon(R.drawable.ok);
    }
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_delete_logs:
        delUseCaseWithParams.execute(new DeleteRecordsSubscriber(TblLog.TBL_LOG), TblLog.TBL_LOG);
        break;
      case R.id.action_settings:
        if (!sharedPreferences.getString(Constants.SHARED_PREFERENCES_TOKEN, "")
            .equalsIgnoreCase("")) {
          showMessage(
              "Token:" + sharedPreferences.getString(Constants.SHARED_PREFERENCES_TOKEN, ""));
        } else {
          showMessage("Token mevcut değil");
        }
        break;

      case R.id.action_sync:
        startService(new Intent(this, TokenHandleService.class));
        break;

      case R.id.action_status:
        startService(new Intent(this, TokenHandleService.class));
        break;

      default:
        throw new RuntimeException("logical mistake with icons ");
    }

    return super.onOptionsItemSelected(item);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }

  private final class DeleteRecordsSubscriber extends DefaultSubscriber<Integer> {

    private final String mTable;

    public DeleteRecordsSubscriber(String tableName) {
      mTable = tableName;
    }

    @Override public void onCompleted() {
      showMessage(String.format("The  deletion of records of %s is completed", mTable));
    }

    @Override public void onError(Throwable e) {
      showMessage(String.format("The  deletion records of %s troubled. Reason: ", e.getMessage()));
      Timber.e("@DeleteRecordsSubscriber", e);
    }

    @Override public void onNext(Integer count) {
      result.delete(0, result.length());
      result.append("The deletion records of");
      result.append(mTable);
      result.append(
          count > 0 ? String.format(" successfully completed.Affected row count:%d", count)
              : "failed");

      Timber.d(result.toString(), AppUtil.prepareInfoLogFormat(new Object() {
      }));

      showMessage(result.toString());
    }
  }
}
