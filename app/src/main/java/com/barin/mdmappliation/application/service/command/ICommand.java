package com.barin.mdmappliation.application.service.command;


public interface ICommand {
  void execute(ICommandStatusCallback commandStatusCallback);
}
