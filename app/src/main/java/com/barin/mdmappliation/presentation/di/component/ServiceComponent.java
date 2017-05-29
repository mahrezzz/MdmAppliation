package com.barin.mdmappliation.presentation.di.component;

import com.barin.mdmappliation.LocalGcmService;
import com.barin.mdmappliation.TestService3;
import com.barin.mdmappliation.application.service.GcmService;
import com.barin.mdmappliation.application.service.InstanceIdListenerService;
import com.barin.mdmappliation.application.service.TokenHandleService;
import com.barin.mdmappliation.presentation.di.module.ServiceModule;
import com.barin.mdmappliation.presentation.di.scope.PerService;
import dagger.Component;

/**
 * Created by barin on 3/29/2016.
 */
@PerService @Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

  void inject(TestService3 testService3);

  void inject(LocalGcmService localGcmService);

  void inject(GcmService gcmService);

  void inject(InstanceIdListenerService instanceIdListenerService);

  void inject(TokenHandleService tokenHandleService);
}
