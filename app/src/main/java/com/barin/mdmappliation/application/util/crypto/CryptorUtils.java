package com.barin.mdmappliation.application.util.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.NoSuchPaddingException;

public class CryptorUtils {

  // key değerlerine göre şifreleyicileri cacheleyen map
  private static Map<String, StringCryptor> cryptorsCache = new HashMap<>();

  /**
   * Bir String i ortak yöntemle şifreler
   *
   * @throws Exception
   */
  public static byte[] encrypt(String s, String key) throws Exception {
    return getCryptor(key).encrypt(s);
  }

  /**
   * ortak yöntemle şifrelenmiş bir String i eski haline döndürür
   *
   * @throws Exception
   */
  public static String decrypt(byte[] s, String key) throws Exception {
    return getCryptor(key).decrypt(s);
  }

  private static StringCryptor getCryptor(String key)
      throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException,
      NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
    StringCryptor crypt = cryptorsCache.get(key);
    if (crypt == null) {
      crypt = new StringCryptor(key);
      cryptorsCache.put(key, crypt);
    }

    return crypt;
  }
}

