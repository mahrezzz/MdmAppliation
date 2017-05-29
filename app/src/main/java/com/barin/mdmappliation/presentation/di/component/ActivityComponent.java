package com.barin.mdmappliation.presentation.di.component;

import android.app.Activity;
import dagger.Component;
import com.barin.mdmappliation.presentation.di.module.ActivityModule;
import com.barin.mdmappliation.presentation.di.scope.PerActivity;
import com.barin.mdmappliation.presentation.view.fragment.LogListFragment;

/**
 * Created by barin on 3/10/2016.
 */

@PerActivity @Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

  //expose the activity to sub graphs
  Activity activity();


  void inject(LogListFragment logListFragment);

}
