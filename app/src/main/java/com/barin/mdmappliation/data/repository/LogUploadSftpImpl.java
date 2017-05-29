package com.barin.mdmappliation.data.repository;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import com.barin.mdmappliation.application.exception.MdmException;
import com.barin.mdmappliation.application.service.command.Command;
import com.barin.mdmappliation.application.util.AppUtil;
import com.barin.mdmappliation.application.util.Constants;
import com.barin.mdmappliation.application.util.FileUtil;
import com.barin.mdmappliation.data.certificate.SftpCertificate;
import com.barin.mdmappliation.data.connection.ServerConnection;
import com.barin.mdmappliation.data.connection.SftpConnection;
import com.barin.mdmappliation.domain.model.MdmUserCertificate;
import com.barin.mdmappliation.domain.repository.LogUploadRepository;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
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
import javax.inject.Singleton;
import rx.Observable;
import timber.log.Timber;

@Singleton public class LogUploadSftpImpl implements LogUploadRepository {

  private final Context mContext;
  private static JSch jSch;
  private static Map<String, String> commandParameters;

  private static final String LOCAL_DIR = "local_dir";
  private static final String LOCAL_FILE_NAME = "local_file_name";
  private static final String REMOTE_DIR = "remote_dir";
  private static final String REMOTE_FILE = "remote_file";
  private static final String HOST_USERNAME = "host_username";
  private static final String HOST_NAME = "host_ip";

  static {
    jSch = new JSch();
    commandParameters = new HashMap<>();
    commandParameters.put(LOCAL_DIR, "");
    commandParameters.put(LOCAL_FILE_NAME, "");
    commandParameters.put(REMOTE_DIR, "");
    commandParameters.put(REMOTE_FILE, "");
    commandParameters.put(HOST_USERNAME, "");
    commandParameters.put(HOST_NAME, "");
  }

  @Inject public LogUploadSftpImpl(Context context) {
    mContext = context;
  }

  @Override public Observable<Boolean> getUploadResult(Command command) {

    return Observable.create(subscriber -> {
      try {
        Log.d("@@LogUploadSftpImpl", Thread.currentThread().getName());
        if (uploadFile(command)) {
          subscriber.onNext(true);
        } else {
          subscriber.onNext(false);
        }
      } catch (MdmException | SftpException | JSchException | IOException e) {
        subscriber.onError(e);
      }
      subscriber.onCompleted();
    });
  }

  private boolean uploadFile(Command command) throws MdmException, SftpException, JSchException, IOException {
    AppUtil.checkForNull(command, "command==null");
    fillParameters(command.getDetails());

    MdmUserCertificate mdmUserCertificate =
        AppUtil.getCertificate(commandParameters.get(HOST_USERNAME), commandParameters.get(HOST_NAME));

    if (null != mdmUserCertificate) {
      return uploadFileToServer((SftpConnection) getConnection(mdmUserCertificate));
    }
    throw new MdmException(
        String.format("No certificate is found for the user: %s on the server:%s", commandParameters.get(HOST_USERNAME),
            commandParameters.get(HOST_NAME)));
  }

  @Override public void fillParameters(List<String> parameters) throws MdmException {
    AppUtil.checkForNull(parameters, "parameters cannot be null!!");

    if (parameters.size() < 3) {
      throw new MdmException("Parameters are not enough to resolve your desire man!");
    }


    commandParameters.put(LOCAL_DIR, Environment.getExternalStorageDirectory() + parameters.get(0).trim());
    commandParameters.put(LOCAL_FILE_NAME, parameters.get(1).trim());
    commandParameters.put(REMOTE_DIR, parameters.get(2).trim());
    commandParameters.put(REMOTE_FILE, parameters.get(3).trim());
    commandParameters.put(HOST_USERNAME, parameters.get(4).trim());
    commandParameters.put(HOST_NAME, parameters.get(5).trim());
  }

  @Override public ServerConnection getConnection(MdmUserCertificate mdmUserCertificate) {
    ServerConnection serverConnection;
    SftpCertificate sftpCertificate;

    sftpCertificate = new SftpCertificate(mContext.getResources()
        .getIdentifier(mdmUserCertificate.getCertificateName(), "raw", mContext.getPackageName()),
        mdmUserCertificate.getPassKey());

    serverConnection =
        new SftpConnection.Builder(commandParameters.get(HOST_NAME), commandParameters.get(HOST_USERNAME), true,
            Constants.SFTP_PORT).setCertificate(sftpCertificate)
            .setsourceFile(commandParameters.get(LOCAL_DIR) + commandParameters.get(LOCAL_FILE_NAME))
            .setRemoteDir(commandParameters.get(REMOTE_DIR))
            .setRemoteFile(commandParameters.get(REMOTE_FILE))
            .build();

    return serverConnection;
  }

  private boolean uploadFileToServer(@NonNull SftpConnection connection)
      throws SftpException, JSchException, IOException {

    FileUtil.checkFileExists(new File(connection.getSourceFile()), "Source file should be exists!!");

    Timber.d(connection.toString());
    Channel channel = null;
    Session session = null;
    Properties config;

    try {
      session = jSch.getSession(connection.getUsername(), connection.getHostname(), connection.getPort());
      config = new Properties();
      config.put("StrictHostKeyChecking", "no");
      session.setConfig(config);

      if (Constants.LOCAL_TEST) {
        session.setPassword("password");
        session.setConfig("PreferredAuthentications", "password");
      } else {
        InputStream privateKey =
            mContext.getResources().openRawResource(connection.getCertificate().getPathCertificate());
        byte[] buffer = new byte[20000];
        int byteCountRead;
        byteCountRead = privateKey.read(buffer);
        byte[] privateKeyBytes = Arrays.copyOf(buffer, byteCountRead);
        byte[] passphrase = connection.getCertificate().getPassPhraseKey();
        jSch.addIdentity("fileUpload", privateKeyBytes, null, passphrase);
        config = new Properties();
        session.setConfig(config);
        session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
      }

      session.connect();
      channel = session.openChannel("sftp");
      channel.connect();
      ChannelSftp channelSftp = (ChannelSftp) channel;
      channelSftp.cd(connection.getRemoteDir());

      channelSftp.put(connection.getSourceFile(), connection.getRemoteFile());
      channelSftp.chmod(511, connection.getRemoteDir() + File.separator + connection.getRemoteFile());

      SftpATTRS attributes = channelSftp.lstat(connection.getRemoteFile());
      channelSftp.setStat(connection.getRemoteFile(), attributes);
    } finally {
      if (session != null) {
        session.disconnect();
      }
      if (channel != null) {
        channel.disconnect();
      }
    }
    return true;
  }
}
