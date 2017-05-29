package com.barin.mdmappliation.presentation.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import com.barin.mdmappliation.application.util.GoogleServicesUtil;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import com.barin.mdmappliation.application.MdmApplication;
import com.barin.mdmappliation.application.be.PackageInfo;
import com.barin.mdmappliation.application.manager.MyLocalBroadcastManager;
import com.barin.mdmappliation.data.JobExecutor;
import com.barin.mdmappliation.data.repository.DeleteTableRecordsImpl;
import com.barin.mdmappliation.data.repository.GetLogsFromDbImpl;
import com.barin.mdmappliation.data.repository.GetTokenImpl;
import com.barin.mdmappliation.data.repository.RegisterTokenImpl;
import com.barin.mdmappliation.data.repository.SaveLogImpl;
import com.barin.mdmappliation.data.repository.UploadMessageImpl;
import com.barin.mdmappliation.domain.executor.PostExecutionThread;
import com.barin.mdmappliation.domain.executor.ThreadExecutor;
import com.barin.mdmappliation.domain.interactor.usecase.DeleteTableRecordsCase;
import com.barin.mdmappliation.domain.interactor.usecase.SaveLogCase;
import com.barin.mdmappliation.domain.interactor.usecase.UploadMessageCase;
import com.barin.mdmappliation.domain.interactor.usecase.UseCaseWithParams;
import com.barin.mdmappliation.domain.repository.DeleteRecordsRepository;
import com.barin.mdmappliation.domain.repository.GetTokenRepository;
import com.barin.mdmappliation.domain.repository.LogRepository;
import com.barin.mdmappliation.domain.repository.RegisterTokenRepository;
import com.barin.mdmappliation.domain.repository.SaveLogRepository;
import com.barin.mdmappliation.domain.repository.UploadMessageToDbRepository;
import com.barin.mdmappliation.presentation.UIThread;

/**
 * Created by barin on 3/7/2016.
 */

@Module public class ApplicationModule {

  private MdmApplication mdmApplication;

  public ApplicationModule(MdmApplication mdmApplication) {
    this.mdmApplication = mdmApplication;
  }

  @Provides @Singleton Context provideContext() {
    return mdmApplication;
  }

  @Provides @Singleton GoogleServicesUtil provideGoogleServicesUtil(Context context) {
    return new GoogleServicesUtil(context);
  }

  @Provides @Singleton SharedPreferences provideSharedPreferences(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Provides @Singleton WifiManager provideWifiManager(Context context) {
    return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
  }

  @Provides @Singleton MyLocalBroadcastManager provideLocalBroadcastManager(Context context) {
    return new MyLocalBroadcastManager(context);
  }

  @Provides @Singleton ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
    return jobExecutor;
  }

  @Provides @Singleton PostExecutionThread providePostExecutionThread(UIThread uiThread) {
    return uiThread;
  }

  @Provides @Singleton PackageInfo providePackageInfo(Context context) {

    return new PackageInfo(context);
  }

  //REPOSITORIES AND USE CASES DURING THE APPLICATION SCOPE
  @Provides @Singleton public GetTokenRepository provideGetTokenRepository(Context context) {
    return new GetTokenImpl(context);
  }

  @Provides @Singleton public RegisterTokenRepository provideRegisterTokenRepository() {
    return new RegisterTokenImpl();
  }

  @Provides @Singleton public LogRepository provideLogRepository(Context context) {
    return new GetLogsFromDbImpl(context);
  }

  @Provides @Singleton public SaveLogRepository provideSaveLogRepository(Context context) {
    return new SaveLogImpl(context);
  }

  @Provides @Singleton
  public DeleteRecordsRepository provideDeleteRecordsRepository(Context context) {
    return new DeleteTableRecordsImpl(context);
  }

  @Provides @Singleton
  public UploadMessageToDbRepository provideUploadMessageRepository(Context context) {
    return new UploadMessageImpl(provideWifiManager(context));
  }

  @Provides @Singleton @Named("saveLog") UseCaseWithParams provideRegisterTokenToDatabase(
      SaveLogCase saveLogCase) {
    return saveLogCase;
  }

  @Provides @Singleton @Named("delete_records") UseCaseWithParams provideDeleteLogsFromDatabase(
      DeleteTableRecordsCase deleteLogsCase) {
    return deleteLogsCase;
  }

  @Provides @Singleton @Named("upload_message_to_db") UseCaseWithParams provideUploadMessageToDb(
      UploadMessageCase uploadMessageCase) {
    return uploadMessageCase;
  }




}
