package com.barin.mdmappliation.presentation.view;

import android.content.Context;
import android.os.Bundle;
import java.util.List;
import com.barin.mdmappliation.presentation.model.AppLog;

/**
 * Created by barin on 3/7/2016.
 */
public interface LogListView extends LoadView {
  void loadLogs(List<AppLog> logs);

  Context context();

  void startLocalGcmService(Bundle bundle);
}
