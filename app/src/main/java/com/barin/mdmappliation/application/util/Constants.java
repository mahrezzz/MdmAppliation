package com.barin.mdmappliation.application.util;

import android.os.Environment;
import java.io.File;

/**
 * Created by barin on 2/9/2016.
 */
public final class Constants {

  //APP RELATED
  public static final int SFTP_PORT = 22;
  public static final String COMMAND_SEPARATOR = "%%";
  public static final String PACKAGE_NAME = "com.barin.mdmappliation";
  public static final String REGISTRATION_RECEIVER_INTENT_ACTION = PACKAGE_NAME;

  //SHARED PREFERENCES
  public static final String SHARED_PREFERENCES_TOKEN = "com.barin.mdmappliation.token";
  public static final String SHARED_PREFERENCES_REGISTER_STATUS =
      "com.barin.mdmappliation.token_register_status";

  //RECEIVER TAGS
  public static final String READ_ACTION = "READ";
  public static final String EXTRA_ACTION = "EXTRA";
  public static final String NOTIFICATION_ID_TAG = "NOT_TAG";
  public static final String REFRESH_MESSAGE = "MESSAGE";

  //INTENT FILTER TAG
  public static final String COMMAND_BACK_INTENT_FILTER = "COMMAND_BACK_FILTER";
  public static final String TOOLBAR_REFRESH_INTENT_FILTER = "TOOLBAR_REFRESH_FILTER";

  //DATABASE
  public static String APP_DATABASE_NAME = "manager_logs.db";
  public static int DATABASE_VERSION = 1;
  public static final String DEFAULT_LOGS_PATH =
      Environment.getExternalStorageDirectory().getAbsolutePath()
          + File.separator
          + "MdmLogs"
          + File.separator
          + "logs"
          + File.separator;

  //REST
  public static final String BASE_URL = "BASE_DOMAIN_WHEREVER_YOU_WANT"; // https://127.0.0.1
  public static final String REMOTE_CONTROL = "RC";

  //LOCALHOST TESTING
  public static boolean LOCAL_TEST = false;
}
