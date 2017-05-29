package com.barin.mdmappliation.domain.model;

/**
 * Created by barin on 4/13/2016.
 */
public class MdmUserCertificate {

  private final String certificateName;
  private final byte[] passKey;
  private String hostname;

  public MdmUserCertificate(byte[] passKey, String username, String hostname) {
    this.passKey = passKey;
    this.certificateName = username;

    this.hostname = hostname;
  }

  public String getCertificateName() {
    return certificateName;
  }

  public byte[] getPassKey() {
    return passKey;
  }

  public String getHostname() {
    return hostname;
  }
}
