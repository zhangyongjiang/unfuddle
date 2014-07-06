package com.gaoshin.webservice;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import common.util.DesEncrypter;

public class Security {
    public static String encrypt64(String data, String key) {
        return new DesEncrypter(key).encrypt64(data);
    }

    public static String decrypt64(String str64, String key) {
        return new DesEncrypter(key).decrypt64(str64);
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String data = "abc";
        String key = "key";
        String encrypted = encrypt64(data, key);
        String decrypted = decrypt64(encrypted, key);
        System.out.println(data + ", " + encrypted + ", " + decrypted);
    }
}
