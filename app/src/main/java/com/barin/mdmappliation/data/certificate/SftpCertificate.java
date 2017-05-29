package com.barin.mdmappliation.data.certificate;

import java.util.Arrays;

/**
 * Created by barin on 2/10/2016.
 */
public class SftpCertificate {

  private int pathCertificate;
  private byte[] passPhraseKey;

  public SftpCertificate(int pathCertificate, byte[] passPhraseKey) {

    this.passPhraseKey = passPhraseKey;
    this.pathCertificate = pathCertificate;
  }

  public int getPathCertificate() {
    return pathCertificate;
  }

  public byte[] getPassPhraseKey() {
    return passPhraseKey;
  }

  @Override public String toString() {
    return "SftpCertificate{" +
        "pathCertificate=" + pathCertificate +
        ", passPhraseKey=" + Arrays.toString(passPhraseKey) +
        '}';
  }
}
