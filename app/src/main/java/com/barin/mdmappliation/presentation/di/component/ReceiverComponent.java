package com.barin.mdmappliation.presentation.di.component;

import dagger.Component;
import com.barin.mdmappliation.application.receiver.RegistrationReceiver;
import com.barin.mdmappliation.presentation.di.module.BroadcastModule;
import com.barin.mdmappliation.presentation.di.scope.PerReceiver;

/**
 * Created by barin on 3/15/2016.
 */
@PerReceiver @Component(dependencies = ApplicationComponent.class, modules = BroadcastModule.class)
public interface ReceiverComponent {
  void inject(RegistrationReceiver receiver);
}
