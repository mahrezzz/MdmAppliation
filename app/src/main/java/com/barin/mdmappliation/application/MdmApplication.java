package com.barin.mdmappliation.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import com.barin.mdmappliation.BuildConfig;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.application.util.FileUtil;
import com.barin.mdmappliation.data.repository.datasource.database.DbHelper;
import com.barin.mdmappliation.presentation.di.component.ApplicationComponent;
import com.barin.mdmappliation.presentation.di.component.DaggerApplicationComponent;
import com.barin.mdmappliation.presentation.di.module.ApplicationModule;
import java.io.File;
import javax.inject.Inject;
import timber.log.Timber;

public class MdmApplication extends Application {

  private ApplicationComponent applicationComponent;

  private DbHelper mDbHelper;
  private SQLiteDatabase mSqLiteDatabase;

  @Inject AppLogger appLogger;
  @Inject SharedPreferences sharedPreferences;

  @Override public void onCreate() {
    super.onCreate();
    setupApplication();
  }

  private void setupApplication() {
    AppStringGetter.STRING_GETTER.setContext(getApplicationContext());

    if (isApplicationSuitableForStart()) {
      setupInjectors();
      setupDatabase();
      setupTimber();
    }
  }

  private boolean isApplicationSuitableForStart() {
    File file = new File(Constants.DEFAULT_LOGS_PATH);
    if (!file.exists()) {
      return FileUtil.createFileFolders(file);
    }
    return true;
  }

  private void setupTimber() {
    if (BuildConfig.DEBUG) {
      //Constants.LOCAL_TEST = false;
      Timber.plant(new Timber.DebugTree() {
        @Override public void e(String message, Object... args) {
          if (args.length > 0) {
            appLogger.logE(message, (Exception) args[0]);
          }
          super.e("@@" + message, args);
        }

        @Override public void i(String message, Object... args) {
          if (args.length > 0) {
            appLogger.logI(message, (String) args[0]);
          }
          super.i("@@" + message, args);
        }

        @Override public void d(String message, Object... args) {
          appLogger.logI(message, null);
          super.d("@@" + message, args);
        }
      });
    } else {
      //Constants.LOCAL_TEST = false;
      Timber.plant(new Timber.DebugTree() {
        @Override public void e(String message, Object... args) {
          if (args.length > 0) {
            appLogger.logE(message, (Exception) args[0]);
          }
          super.e("@@" + message, args);
        }

        @Override public void i(String message, Object... args) {
          if (args.length > 0) {
            appLogger.logI(message, (String) args[0]);
          }
          super.i("@@" + message, args);
        }
      });
    }
  }

  private void setupInjectors() {
    applicationComponent =
        DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    applicationComponent.inject(this);
  }

  private void setupDatabase() {
    mDbHelper = new DbHelper(getApplicationContext());

    mSqLiteDatabase = mDbHelper.getReadableDatabase();
  }

  public ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }

  public synchronized SQLiteDatabase getDb() {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      throw new NetworkOnMainThreadException();
    }
    return mSqLiteDatabase;
  }

  public AppLogger getAppLogger() {
    return appLogger;
  }

  public synchronized void closeDb() {
    if (mDbHelper != null) {
      mDbHelper.close();
    }
    if (mSqLiteDatabase.isOpen()) {
      mSqLiteDatabase.close();
    }
  }

  @Override public void onTerminate() {
    closeDb();
    super.onTerminate();
  }
}
