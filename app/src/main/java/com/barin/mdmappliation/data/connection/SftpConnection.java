package com.barin.mdmappliation.data.connection;

import com.barin.mdmappliation.data.certificate.SftpCertificate;

public class SftpConnection implements ServerConnection {

  //required params.
  private String hostname;
  private String username;
  //boolean withCertificate;
  private int port;

  //optional params
  private String password;
  private SftpCertificate certificate;
  private String remoteDir;
  private String sourceFile;

  public String getHostname() {
    return hostname;
  }

  public String getUsername() {
    return username;
  }

  public int getPort() {
    return port;
  }


  public SftpCertificate getCertificate() {
    return certificate;
  }

  public String getRemoteDir() {
    return remoteDir;
  }

  public String getSourceFile() {
    return sourceFile;
  }

  public String getRemoteFile() {
    return remoteFile;
  }

  private String remoteFile;

  private SftpConnection(Builder builder) {

    sourceFile = builder.sourceFile;
    remoteFile = builder.remoteFile;
    remoteDir = builder.remoteDir;
    hostname = builder.hostname;
    port = builder.port;
    certificate = builder.certificate;
    username = builder.username;
    password = builder.password;
  }

  public static class Builder {
    //required params.
    String hostname;
    String username;
    int port;
    boolean withCertificate;

    //optional params
    String remoteDir;
    String sourceFile;
    String remoteFile;
    SftpCertificate certificate;
    String password;

    public Builder(String hostname, String username, boolean withCertificate, int port) {
      this.username = username;
      this.withCertificate = withCertificate;
      this.hostname = hostname;
      this.port = port;
    }

    public Builder setCertificate(SftpCertificate certificate) {
      this.certificate = certificate;
      return this;
    }

    public Builder setRemoteFile(String remoteFile) {

      this.remoteFile = remoteFile;
      return this;
    }

    public Builder setsourceFile(String sourceFile) {
      this.sourceFile = sourceFile;
      return this;
    }

    public Builder setRemoteDir(String remoteDir) {
      this.remoteDir = remoteDir;
      return this;
    }

    public SftpConnection build() {
      return new SftpConnection(this);
    }
  }

  @Override public String toString() {
    return "SftpConnection{"
        + "hostname='"
        + hostname
        + '\''
        + ", username='"
        + username
        + '\''
        + ", port="
        + port
        + ", password='"
        + password
        + '\''
        + ", certificate="
        + certificate
        + ", remoteDir='"
        + remoteDir
        + '\''
        + ", sourceFile='"
        + sourceFile
        + '\''
        + ", remoteFile='"
        + remoteFile
        + '\''
        + '}';
  }
}
