package com.barin.mdmappliation.application;

import com.barin.mdmappliation.application.util.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by barin on 4/26/2016.
 */

public final class FakeParameterCreater {

  static List<String> notificationParameters = new ArrayList<>();
  static List<String> pushParameters = new ArrayList<>();
  static List<String> pullParameters = new ArrayList<>();

  static String testHostName;
  static String testUserName;
  static String testServerPath;

  public static void initiliaze() {

    if (Constants.LOCAL_TEST) {
      testHostName = "10.0.3.2";
      testUserName = "tester";
      testServerPath = "/tabletmdm/";
    } else {
      testHostName = "10.85.97.23";
      testUserName = "tbllog";
      testServerPath = "/export/home/tbllog/tabletmdm/";
    }
  }



  public static List<String> getPullParameters(HowProper howProper) {
    pullParameters.clear();

    switch (howProper) {
      case PROPER:
        pullParameters.add(testServerPath
            + Constants.COMMAND_SEPARATOR
            + "test3.txt"
            + Constants.COMMAND_SEPARATOR
            + "/TestFiles/databases/"
            + Constants.COMMAND_SEPARATOR
            + "server_test3.txt"
            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            + testHostName);
        pullParameters.add(testServerPath
            + Constants.COMMAND_SEPARATOR
            + "tabletHardwareInfo"
            + Constants.COMMAND_SEPARATOR
            + "/TestFiles/databases/"
            + Constants.COMMAND_SEPARATOR
            + "server_tabletHardwareInfo"
            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            + testHostName);
        pullParameters.add(testServerPath
            + Constants.COMMAND_SEPARATOR
            + "test2.txt"
            + Constants.COMMAND_SEPARATOR
            + "/TestFiles/databases/"
            + Constants.COMMAND_SEPARATOR
            + "server_test2.txt"
            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            + testHostName);

        pullParameters.add(testServerPath
            + Constants.COMMAND_SEPARATOR
            + "test.apk"
            + Constants.COMMAND_SEPARATOR
            + "/TestFiles/databases/"
            + Constants.COMMAND_SEPARATOR
            + "server_test.apk"
            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            + testHostName);
        break;

      case NOT_PROPER:

        pullParameters.add(testServerPath
            + Constants.COMMAND_SEPARATOR
            + "wrong_file.txt"
            + Constants.COMMAND_SEPARATOR
            + "/TestFiles/databases/"
            + Constants.COMMAND_SEPARATOR
            + "server_wrong_file.txt"
            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            + testHostName);
        pullParameters.add(testServerPath
            + Constants.COMMAND_SEPARATOR
            + "tabletHardwareInfo"
            + Constants.COMMAND_SEPARATOR
            + "/TestFiles/wrong_path/"
            + Constants.COMMAND_SEPARATOR
            + "server_tabletHardwareInfo"
            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            + testHostName);
        pullParameters.add(testServerPath
            + Constants.COMMAND_SEPARATOR
            + "wrong_file.txt"
            + Constants.COMMAND_SEPARATOR
            + "wrong_path"
            + Constants.COMMAND_SEPARATOR
            + "server_wrong_file.txt"
            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            + testHostName);

        break;

      case MIX:

        pullParameters.add(testServerPath
            + Constants.COMMAND_SEPARATOR
            + "test3.txt"
            + Constants.COMMAND_SEPARATOR
            + "/TestFiles/databases/"
            + Constants.COMMAND_SEPARATOR
            + "test3.txt"
            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            + testHostName);
        pullParameters.add(testServerPath
            + Constants.COMMAND_SEPARATOR
            + "wrong_file.txt"
            + Constants.COMMAND_SEPARATOR
            + "/TestFiles/databases/"
            + Constants.COMMAND_SEPARATOR
            + "serrver_wrong_file.txt"

            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            + testHostName);
        pullParameters.add(testServerPath
            + Constants.COMMAND_SEPARATOR
            + "test2.txt"
            + Constants.COMMAND_SEPARATOR
            + "/TestFiles/databases/"
            + Constants.COMMAND_SEPARATOR
            + "server_test2.txt"
            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            + testHostName);

        pullParameters.add(testServerPath
            + Constants.COMMAND_SEPARATOR
            + "test2.txt"
            + Constants.COMMAND_SEPARATOR
            + "/wrong_path/"
            + Constants.COMMAND_SEPARATOR
            + "server_test2.txt"
            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            + testHostName);

        break;

      default:

        throw new RuntimeException("cannot be happen body..");
    }

    return pullParameters;
  }

  public static List<String> getPushParameters(HowProper howProper) {
    pushParameters.clear();

    switch (howProper) {
      case PROPER:
        pushParameters.add("/TestFiles/"
            + Constants.COMMAND_SEPARATOR
            + "configE.json"
            + Constants.COMMAND_SEPARATOR
            + testServerPath
            + Constants.COMMAND_SEPARATOR
            + "tablet_configE.json"
            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            +
            testHostName);

        break;
      case NOT_PROPER:

        pushParameters.add("/TestFiles/"
            + Constants.COMMAND_SEPARATOR
            + "wrong.json"
            + Constants.COMMAND_SEPARATOR
            + testServerPath
            + Constants.COMMAND_SEPARATOR
            + "tablet_wrong.json"
            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            +
            testHostName);

        pushParameters.add("/wrongpath/databases/"
            + Constants.COMMAND_SEPARATOR
            + "logs.db"
            + Constants.COMMAND_SEPARATOR
            + testServerPath
            + Constants.COMMAND_SEPARATOR
            + "tablet_logs.db"

            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            +
            testHostName);
        break;
      case MIX:

        pushParameters.add("/TestFiles/"
            + Constants.COMMAND_SEPARATOR
            + "configE.json"
            + Constants.COMMAND_SEPARATOR
            + testServerPath
            + Constants.COMMAND_SEPARATOR
            + "tablet_configE.json"

            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            +
            testHostName);

        pushParameters.add("/TestFiles/databases/"
            + Constants.COMMAND_SEPARATOR
            + "wrong.db"
            + Constants.COMMAND_SEPARATOR
            + testServerPath
            + Constants.COMMAND_SEPARATOR
            + "tablet_wrong.db"

            + Constants.COMMAND_SEPARATOR
            + testUserName
            + Constants.COMMAND_SEPARATOR
            +
            testHostName);
        break;

      default:
        throw new RuntimeException("cannot be happen!!");
    }

    return pushParameters;
  }

  public static List<String> getNotificationParameters(HowProper howProper) {
    notificationParameters.clear();
    switch (howProper) {

      case PROPER:
        notificationParameters.add("testTitle%%We are champions");
        notificationParameters.add("testTitle%%I feel divosion");
        notificationParameters.add("testTitle%%we the real!!!");
        notificationParameters.add("testTitle%%Aventura");
        break;
      case NOT_PROPER:
        notificationParameters.add("testTitle%%????SDAFsafkajdfl....,,,,????");
        notificationParameters.add("testTitle%%46546456adsfadksjfldkaf???**********");

        break;
      case MIX:
        notificationParameters.add("testTitle%%????SDAFsafkajdfl....,,,,????");
        notificationParameters.add("testTitle%%46546456adsfadksjfldkaf???**********");
        notificationParameters.add("testTitle%%Aventura");
        break;
      default:
        throw new RuntimeException("cannot be happen!!");
    }

    return notificationParameters;
  }

  public static void corruptServerSettings() {
    testHostName = "www.testitbody.com";
    testUserName = "hopdedikaynan";
  }
}
