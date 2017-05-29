package com.barin.mdmappliation.domain.model;

/**
 * Created by barin on 4/12/2016.
 */
public class PullFileInfoDomain {

  private String localFile;
  private String remoteFile;
  private String remotePath;
  private String localPath;

  private boolean success = false;
  private String successMessage;
  private String md5;

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public boolean isSuccess() {
    return success;
  }

  public void createSuccessMessage() {
    StringBuilder successMessage = new StringBuilder();
    successMessage.append("\n");
    successMessage.append(String.format(remotePath));
    successMessage.append("\t");
    successMessage.append(String.format(remoteFile));
    successMessage.append("\t");
    successMessage.append(String.format(localPath));
    successMessage.append("\t");
    successMessage.append(String.format(localFile));
    successMessage.append("\t");
    successMessage.append(String.format("successfully transferred."));
    this.successMessage = successMessage.toString();
  }

  public String getSuccessMessage() {

    return successMessage;
  }

  public PullFileInfoDomain(String localFile, String remoteFile, String remotePath,
      String localPath, String md5) {
    this.localFile = localFile;
    this.remoteFile = remoteFile;
    this.remotePath = remotePath;
    this.localPath = localPath;
    this.md5 = md5;
  }

  @Override public String toString() {
    return "PullFileInfoDomain{" +
        "localFile='" + localFile + '\'' +
        ", remoteFile='" + remoteFile + '\'' +
        ", remotePath='" + remotePath + '\'' +
        ", localPath='" + localPath + '\'' +
        ", success=" + success +
        ", md5='" + md5 + '\'' +
        '}';
  }
}
