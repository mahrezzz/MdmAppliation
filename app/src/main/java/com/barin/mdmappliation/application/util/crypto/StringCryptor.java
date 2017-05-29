package com.barin.mdmappliation.application.util.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by barin on 4/13/2016.
 */
public class StringCryptor {

  private Cipher deCipher;
  private Cipher enCipher;

  static final String IVSpec = "Axy81AaSAAAZA2A1";

  /**
   * @param encryptionKey en az 16 karakter olmalı
   */
  public StringCryptor(String encryptionKey)
      throws NoSuchPaddingException, InvalidAlgorithmParameterException,
      UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchProviderException,
      InvalidKeyException {
    try {
      // AES şu an güvenli kabul edilen bir şifreleme yöntemi
      SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

      enCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
      enCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IVSpec.getBytes("UTF-8")));

      deCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
      deCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IVSpec.getBytes("UTF-8")));
    } catch (Exception e) {
      //Timber.e("Şifreleme nesnesi oluşturulurken hata:", e);
      throw e;
    }
  }

  /**
   * @return UTF-8 e göre parçaladığı String in şifrelenmiş hali
   * @throws Exception
   */
  public byte[] encrypt(String plainText) throws Exception {
    return enCipher.doFinal(plainText.getBytes("UTF-8"));
  }

  /**
   * @param cipherText daha önce UTF-8 e göre parçalanmış String in şifrelenmiş hali
   * @return şifrelenmiş String in aslı
   * @throws Exception
   */
  public String decrypt(byte[] cipherText) throws Exception {
    return new String(deCipher.doFinal(cipherText), "UTF-8");
  }
}