package com.barin.mdmappliation.presentation.di.module;

import dagger.Module;
import dagger.Provides;
import com.barin.mdmappliation.application.receiver.BaseReceiver;
import com.barin.mdmappliation.presentation.di.scope.PerReceiver;

/**
 * Created by barin on 3/15/2016.
 */

@Module public class BroadcastModule {

  private final BaseReceiver receiver;

  public BroadcastModule(BaseReceiver receiver) {
    this.receiver = receiver;
  }

  /**
   * Expose the activity to dependents in the graph.
   */
  @Provides @PerReceiver BaseReceiver receiver() {
    return this.receiver;
  }

}
