package com.ifma.appmhelp.lib;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by leo on 12/8/16.
 */
public class BlowfishCript {

    private static final String key = "U39E6C50x4NUgWEhasssTw==";

    public static String encrypt(String value) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return new String (Base64.encode(cipher.doFinal(value.getBytes()), Base64.DEFAULT));
    }

    public static String decrypt(String value) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String (cipher.doFinal(Base64.decode(value.getBytes(), Base64.DEFAULT)));
    }

}
