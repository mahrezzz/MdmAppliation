package com.barin.mdmappliation.application.util;

import android.support.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import com.barin.mdmappliation.application.exception.MdmException;
import com.barin.mdmappliation.domain.model.MdmUserCertificate;

public final class AppUtil {

  private static Map<String, MdmUserCertificate> userAndCertificates;

  static {
    userAndCertificates = new HashMap<>();

    userAndCertificates.put("username",
        new MdmUserCertificate(new byte[] { '2', '0', '1', '6', 'x', 'x', 'x', 'x', 'x', 'x', '2', '0', '1', '6' },
            "ftp_server_private_key_rsa", "hostname"));

    userAndCertificates.put("tester", new MdmUserCertificate(new byte[] {
        'm', 'y', '-', 's', 'u', 'p', 'e', 'r', '-', 's', 'e', 'c', 'u', 'r', 'e', '-', 'p', 'a', 's', 's', 'w', 'o',
        'r', 'd'
    }, "tester_private_key", "10.0.3.2"));
  }

  private AppUtil() {
    throw new IllegalAccessError("no no no  illegal access my friend!!");
  }

  public static <T> T checkForNull(T object, String message) {
    if (object == null) {
      throw new NullPointerException(message);
    }
    return object;
  }

  public static String getStackTrace(StackTraceElement[] elements) {

    StringBuilder builder = new StringBuilder();
    for (StackTraceElement element : elements) {
      builder.append(element.toString());
      builder.append("\n");
    }
    return builder.toString();
  }

  public static String prepareInfoLogFormat(Object obj) {
    if (obj == null) {
      throw new NullPointerException("obj==null");
    }
    return String.format("Class:%s \nMethod:%s", obj.getClass().getName(),
        obj.getClass().getEnclosingMethod().getName());
  }

  @Nullable public static MdmUserCertificate getCertificate(String username, String hostname) throws MdmException {

    checkForNull(username, "username==null");
    checkForNull(hostname, "hostname==null");

    MdmUserCertificate mdmUserCertificate = userAndCertificates.get(username.trim().concat(hostname.trim()));

    if (mdmUserCertificate == null) {
      throw new MdmException("No Certificate is available for this server.");
    }

    if (mdmUserCertificate.getHostname().trim().equalsIgnoreCase(hostname.trim())) {
      return mdmUserCertificate;
    }
    return null;
  }



/*
  public static String getCurrentDate() {
    return dateFormat.format(new Date());
  }*/
}
