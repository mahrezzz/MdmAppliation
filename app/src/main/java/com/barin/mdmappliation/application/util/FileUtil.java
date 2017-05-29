package com.barin.mdmappliation.application.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import timber.log.Timber;

public final class FileUtil {

  public static boolean checkMD5(String md5, File updateFile)
      throws FileNotFoundException, NoSuchAlgorithmException {

    if (TextUtils.isEmpty(md5) || updateFile == null) {
      Timber.d("MD5 string empty or updateFile null");
      return false;
    }

    String calculatedDigest = calculateMD5(updateFile);
    if (calculatedDigest == null) {
      return false;
    }

    Timber.d("Calculated digest: " + calculatedDigest);
    Timber.d("Provided digest: " + md5);

    return calculatedDigest.equalsIgnoreCase(md5);
  }

  @TargetApi(Build.VERSION_CODES.KITKAT) public static String calculateMD5(File updateFile)
      throws FileNotFoundException, NoSuchAlgorithmException {

    checkFileExists(updateFile, "file is not exists");

    try (InputStream is = new FileInputStream(updateFile)) {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      byte[] buffer = new byte[8192];
      int read;
      while ((read = is.read(buffer)) > 0) {
        digest.update(buffer, 0, read);
      }
      byte[] md5sum = digest.digest();
      BigInteger bigInt = new BigInteger(1, md5sum);
      String output = bigInt.toString(16);
      output = String.format("%32s", output).replace(' ', '0');
      return output;
    } catch (FileNotFoundException | NoSuchAlgorithmException e) {
      throw e;
    } catch (IOException e) {
      throw new RuntimeException("Unable to process file for MD5", e);
    }
  }

  public static File checkFileExists(File file, String message) throws FileNotFoundException {
    if (!file.exists()) {
      throw new FileNotFoundException(message);
    }
    return file;
  }

  public static boolean createFileFolders(File file) {



    return file.mkdirs();
  }
}