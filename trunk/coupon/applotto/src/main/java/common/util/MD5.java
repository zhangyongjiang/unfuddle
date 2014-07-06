package common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String md5(Object original) {
        if ((original == null)) {
            return null;
        }
        byte[] defaultBytes = original.toString().getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 0) {
                    hex = "00";
                }
                else if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCode(String number) throws Exception {
        byte[] digest_bytes = generateFullCode(number);
        StringBuilder encryptedString = new StringBuilder(8);
        if (digest_bytes != null) {
            for (int i = 0; i < digest_bytes.length; i++) {
                if (i == 14 || i == 9 || i == 1 || i == 11) {
                    String st = Integer.toHexString(0xFF & digest_bytes[i]);
                    encryptedString.append(st.charAt(0));
                }
            }
        }
        return encryptedString.toString();
    }

    public static byte[] generateFullCode(String number) throws Exception {
        java.security.MessageDigest digest;
        digest = java.security.MessageDigest.getInstance("MD5");
        digest.reset();
        digest.update(number.getBytes());
        return digest.digest();

    }
    
    public static void main(String[] args) throws Exception {
        String number = "6502247603";
        java.security.MessageDigest digest;
        digest = java.security.MessageDigest.getInstance("MD5");
        digest.reset();
        digest.update(number.getBytes());
        byte[] digest_bytes = digest.digest();
        StringBuilder encryptedString = new StringBuilder(8);
        if (digest_bytes != null) {
            for (int i = 0; i < digest_bytes.length; i++) {
                if (i == 14 || i == 9 || i == 1 || i == 11) {
                    String st = Integer.toHexString(0xFF & digest_bytes[i]);
                    encryptedString.append(st.charAt(0));
                }
            }
        }
        
        System.out.println(encryptedString.toString());
    }
}
