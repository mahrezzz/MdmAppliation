package com.barin.mdmappliation.data.repository;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.inject.Inject;
import com.barin.mdmappliation.application.exception.MdmException;
import com.barin.mdmappliation.application.service.command.Command;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.data.certificate.SftpCertificate;
import com.barin.mdmappliation.data.connection.ServerConnection;
import com.barin.mdmappliation.data.connection.SftpConnection;
import com.barin.mdmappliation.domain.model.MdmUserCertificate;
import com.barin.mdmappliation.domain.model.PullFileInfoDomain;
import com.barin.mdmappliation.domain.repository.PullFileRepository;
import rx.Observable;

public class PullFileImpl implements PullFileRepository {

  private Context mContext;
  private static JSch jSch;
  private static Map<String, String> commandParameters;

  private static final String REMOTE_DIR = "remote_dir";
  private static final String REMOTE_FILE_NAME = "remote_file_name";
  private static final String LOCAL_DIR = "local_dir";
  private static final String LOCAL_FILE_NAME = "local_file_name";
  private static final String HOST_USERNAME = "host_username";
  private static final String HOST_NAME = "host_ip";
  private static final String MD5 = "md5";

  static {
    jSch = new JSch();
    commandParameters = new HashMap<>();
    commandParameters.put(REMOTE_DIR, "");
    commandParameters.put(REMOTE_FILE_NAME, "");
    commandParameters.put(LOCAL_DIR, "");
    commandParameters.put(LOCAL_FILE_NAME, "");
    commandParameters.put(HOST_USERNAME, "");
    commandParameters.put(HOST_NAME, "");
    commandParameters.put(MD5, "");
  }

  @Inject public PullFileImpl(Context context) {
    mContext = context;
  }

  @Override public Observable<PullFileInfoDomain> pullFileFromServerObservable(Command command) {
    return Observable.create(subscriber -> {

      AppUtil.checkForNull(command, "command==null");
      Log.d("@@PullFileImpl", Thread.currentThread().getName());
      try {
        fillParameters(command.getDetails());
        MdmUserCertificate mdmUserCertificate =
            AppUtil.getCertificate(commandParameters.get(HOST_USERNAME),
                commandParameters.get(HOST_NAME));
        subscriber.onNext(
            handleFileTransferring((SftpConnection) getConnection(mdmUserCertificate)));
        subscriber.onCompleted();
      } catch (MdmException | SftpException | IOException | JSchException e) {
        subscriber.onError(e);
      }
    });
  }

  @Override public void fillParameters(List<String> parameters) throws MdmException {

    AppUtil.checkForNull(parameters, "parameters cannot be null!!");
    if (parameters.size() < 3) {
      throw new MdmException("Parameters are not enough to resolve your desire man!");
    }
    commandParameters.put(REMOTE_DIR, parameters.get(0));
    commandParameters.put(REMOTE_FILE_NAME, parameters.get(1));
    commandParameters.put(LOCAL_DIR, Environment.getExternalStorageDirectory() + parameters.get(2));
    commandParameters.put(LOCAL_FILE_NAME, parameters.get(3));
    commandParameters.put(HOST_USERNAME, parameters.get(4));
    commandParameters.put(HOST_NAME, parameters.get(5));

    //commandParameters.put(MD5, parameters.get(6));
  }

  private PullFileInfoDomain handleFileTransferring(@NonNull SftpConnection sftpConnection)
      throws MdmException, IOException, JSchException, SftpException {

    PullFileInfoDomain pullFileInfoDomain = convertPullInfo(commandParameters);

    File file = pullFileFromServer(sftpConnection, commandParameters.get(REMOTE_FILE_NAME));
    if (file != null) {

      pullFileInfoDomain.setSuccess(Boolean.TRUE);


    }

    return pullFileInfoDomain;
  }

  private PullFileInfoDomain convertPullInfo(Map<String, String> commandParameters) {
    return new PullFileInfoDomain(commandParameters.get(REMOTE_FILE_NAME),
        commandParameters.get(REMOTE_FILE_NAME), commandParameters.get(REMOTE_DIR),
        commandParameters.get(LOCAL_DIR), commandParameters.get(MD5));
  }

  @Override public ServerConnection getConnection(MdmUserCertificate mdmUserCertificate) {

    ServerConnection serverConnection;
    SftpCertificate sftpCertificate;

    sftpCertificate = new SftpCertificate(mContext.getResources()
        .getIdentifier(mdmUserCertificate.getCertificateName(), "raw", mContext.getPackageName()),
        mdmUserCertificate.getPassKey());

    serverConnection = new SftpConnection.Builder(commandParameters.get(HOST_NAME),
        commandParameters.get(HOST_USERNAME), true, Constants.SFTP_PORT).setCertificate(
        sftpCertificate)
        .setRemoteDir(commandParameters.get(REMOTE_DIR))
        .setsourceFile(commandParameters.get(LOCAL_FILE_NAME))
        .setRemoteFile(commandParameters.get(REMOTE_FILE_NAME))
        .build();
    return serverConnection;
  }

  @Nullable
  private File pullFileFromServer(@NonNull SftpConnection sftpConnection, String remoteFileName)
      throws JSchException, SftpException, IOException {

    Channel channel = null;
    Session session = null;
    Properties config;

    try {
      session = jSch.getSession(sftpConnection.getUsername(), sftpConnection.getHostname(),
          sftpConnection.getPort());
      config = new Properties();
      config.put("StrictHostKeyChecking", "no");
      session.setConfig(config);

      if (Constants.LOCAL_TEST) {
        session.setPassword("password");
        session.setConfig("PreferredAuthentications", "password");
      } else {

        //the server's .bks file should be on raw folder. Connect to server securely
        InputStream privateKey = mContext.getResources()
            .openRawResource(sftpConnection.getCertificate().getPathCertificate());
        byte[] buffer = new byte[20000];
        int byteCountRead;
        byteCountRead = privateKey.read(buffer);
        byte[] privateKeyBytes = Arrays.copyOf(buffer, byteCountRead);
        byte[] passphrase = sftpConnection.getCertificate().getPassPhraseKey();
        jSch.addIdentity("logs", privateKeyBytes, null, passphrase);
        config = new Properties();
        session.setConfig(config);
        session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
      }

      session.connect();
      channel = session.openChannel("sftp");
      channel.connect();
      ChannelSftp channelSftp = (ChannelSftp) channel;
      channelSftp.cd(sftpConnection.getRemoteDir());

      String destFileName =
          commandParameters.get(LOCAL_DIR) + commandParameters.get(LOCAL_FILE_NAME);
      File destFile = new File(destFileName);
      channelSftp.get(remoteFileName, destFileName);

      return destFile;
    } finally {
      if (session != null) {
        session.disconnect();
      }

      if (channel != null) {
        channel.disconnect();
      }
    }
  }
}
