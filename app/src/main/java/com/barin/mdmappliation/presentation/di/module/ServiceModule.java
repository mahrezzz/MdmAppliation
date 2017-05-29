package com.barin.mdmappliation.presentation.di.module;

import android.app.Service;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import com.barin.mdmappliation.data.repository.LogUploadSftpImpl;
import com.barin.mdmappliation.data.repository.PullFileImpl;
import com.barin.mdmappliation.domain.interactor.usecase.GetTokenCase;
import com.barin.mdmappliation.domain.interactor.usecase.LogUploadCase;
import com.barin.mdmappliation.domain.interactor.usecase.PullFileCase;
import com.barin.mdmappliation.domain.interactor.usecase.RegisterTokenCase;
import com.barin.mdmappliation.domain.interactor.usecase.UseCase;
import com.barin.mdmappliation.domain.interactor.usecase.UseCaseWithParams;
import com.barin.mdmappliation.domain.repository.LogUploadRepository;
import com.barin.mdmappliation.domain.repository.PullFileRepository;
import com.barin.mdmappliation.presentation.di.scope.PerService;

/**
 * Created by barin on 3/29/2016.
 */
@Module public class ServiceModule {

  private final Service mService;

  public ServiceModule(Service service) {

    mService = service;
  }

  @Provides @PerService Service service() {
    return mService;
  }

/*  @Provides @PerService @Named("registerTokenToDatabase")
  UseCaseWithParams provideRegisterTokenToDatabase(RegisterTokenCase registerTokenCase) {
    return registerTokenCase;
  }

  @Provides @PerService @Named("getToken") UseCase getTokenCase(GetTokenCase getTokenCase) {
    return getTokenCase;
  }*/

  @Provides @PerService LogUploadRepository provideLogUploadRepository() {
    return new LogUploadSftpImpl(mService.getApplicationContext());
  }

  @Provides @PerService PullFileRepository providePushFileRepository() {
    return new PullFileImpl(mService.getApplicationContext());
  }

  @Provides @PerService @Named("log_upload") UseCaseWithParams provideLogUploadCase(
      LogUploadCase logUploadCase) {
    return logUploadCase;
  }

  @Provides @PerService @Named("pull_file") UseCaseWithParams providePullFileCase(
      PullFileCase pullFileCase) {
    return pullFileCase;
  }

  @Provides @PerService @Named("getToken") UseCase getTokenCase(GetTokenCase getTokenCase) {
    return getTokenCase;
  }
  @Provides @PerService @Named("registerTokenToDatabase")
  UseCaseWithParams provideRegisterTokenToDatabase(RegisterTokenCase registerTokenCase) {
    return registerTokenCase;
  }


}
