package common.util.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import common.util.DesEncrypter;

public class Security {
    public static String getVerifiableString(String data, String key) {
        return new DesEncrypter(key).encrypt64(data);
    }

    public static String getOriginalData(String data, String key) {
        if (data == null) {
            return null;
        }
        return new DesEncrypter(key).decrypt64(data);
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
}
