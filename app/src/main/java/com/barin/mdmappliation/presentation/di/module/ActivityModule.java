package com.barin.mdmappliation.presentation.di.module;

import android.app.Activity;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import com.barin.mdmappliation.domain.interactor.usecase.GetLogListCase;
import com.barin.mdmappliation.domain.interactor.usecase.UseCase;
import com.barin.mdmappliation.presentation.di.scope.PerActivity;

/**
 * Created by barin on 3/10/2016.
 */
@Module public class ActivityModule {
  private final Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides @PerActivity Activity activity() {
    return this.activity;
  }

  @Provides @PerActivity @Named("logList") UseCase provideLogList(GetLogListCase getUserList) {
    return getUserList;
  }
}

