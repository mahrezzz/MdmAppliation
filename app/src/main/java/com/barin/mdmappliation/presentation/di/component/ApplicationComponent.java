package com.barin.mdmappliation.presentation.di.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import com.barin.mdmappliation.application.util.GoogleServicesUtil;
import dagger.Component;
import javax.inject.Singleton;
import com.barin.mdmappliation.application.AppLogger;
import com.barin.mdmappliation.application.MdmApplication;
import com.barin.mdmappliation.application.be.PackageInfo;
import com.barin.mdmappliation.application.manager.MyLocalBroadcastManager;
import com.barin.mdmappliation.domain.executor.PostExecutionThread;
import com.barin.mdmappliation.domain.executor.ThreadExecutor;
import com.barin.mdmappliation.domain.repository.GetTokenRepository;
import com.barin.mdmappliation.domain.repository.LogRepository;
import com.barin.mdmappliation.domain.repository.RegisterTokenRepository;
import com.barin.mdmappliation.presentation.di.module.ApplicationModule;
import com.barin.mdmappliation.presentation.model.mapper.GcmTokenMapper;
import com.barin.mdmappliation.presentation.view.activity.BaseActivity;

/**
 * Created by barin on 3/7/2016.
 */

@Singleton  //actually this is a scope for this container
@Component(modules = { ApplicationModule.class }) public interface ApplicationComponent {

  void inject(MdmApplication application);

  void inject(BaseActivity baseActivity);

  //Exposed to sub-graphs.
  Context context();

  ThreadExecutor threadExecutor();

  PostExecutionThread postExecutionThread();

  LogRepository logRepository();

  GetTokenRepository getTokenRepository();

  RegisterTokenRepository getRegisterTokenRepository();

  SharedPreferences sharedPreferences();

  WifiManager wifiManager();

  GcmTokenMapper gcmTokenMapper();

  AppLogger appLogger();

  MyLocalBroadcastManager myLocalBroadcastManager();

  PackageInfo packageInfo();

  GoogleServicesUtil googleServicesUtil();
}
