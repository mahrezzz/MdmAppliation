package com.barin.mdmappliation.application.service.command;

/**
 * Created by barin on 3/30/2016.
 */

import javax.inject.Inject;
import com.barin.mdmappliation.presentation.di.scope.PerService;

@PerService public class CommandFactory {

  @Inject public CommandFactory() {
  }

  public void handleCommand(ICommand command, ICommandStatusCallback commandStatusCallback) {
    if (command != null) {
      command.execute(commandStatusCallback);




    }
  }
}
