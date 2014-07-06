package common.util;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

import common.util.web.Security;

public class DesEncrypter {

    private Cipher ecipher;
    private Cipher dcipher;
    private Base64 b64 = new Base64();

    // 8-byte Salt
    private final byte[] salt = {
            (byte) 0xA9,
            (byte) 0x9B,
            (byte) 0xC8,
            (byte) 0x32,
            (byte) 0x56,
            (byte) 0x35,
            (byte) 0xE3,
            (byte) 0x03 };

    // Iteration count
    private final int iterationCount = 19;

    public DesEncrypter() {
        init("gao");
    }

    public DesEncrypter(String passPhrase) {
        init(passPhrase);
    }

    private void init(String passPhrase) {
        try {
            // Create the key
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (java.security.InvalidAlgorithmParameterException e) {
        } catch (java.security.spec.InvalidKeySpecException e) {
        } catch (javax.crypto.NoSuchPaddingException e) {
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (java.security.InvalidKeyException e) {
        }
    }

    public String encrypt64(String str) {
        Base64 b64 = new Base64();
        String str64 = new String(b64.encode(encrypt(str).getBytes())).trim();
        if (str64.endsWith("=="))
            str64 = str64.substring(0, str64.length() - 2) + "%%";
        else if (str64.endsWith("="))
            str64 = str64.substring(0, str64.length() - 1) + "%";
        return str64;
    }

    public String decrypt64(String str64) {
        Base64 b64 = new Base64();
        if (str64.endsWith("%%"))
            str64 = str64.substring(0, str64.length() - 2) + "==";
        else if (str64.endsWith("%"))
            str64 = str64.substring(0, str64.length() - 1) + "=";

        return decrypt(new String(b64.decode(str64.getBytes())));
    }

    public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new String(b64.encode(enc));
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }

    public String decrypt(String str) {
        try {
            // Decode base64 to get bytes
            byte[] dec = b64.decode(str.getBytes());

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }

    public static String selfencrypt(String... data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < data.length; i++) {
            sb.append("__").append(data[i]);
        }
        String d2 = sb.toString();
        return new DesEncrypter(Security.md5(d2).substring(4, 4)).encrypt64(data[0]) + d2;
    }

    public static List<String> selfdecrypt(String encrypted) {
        int pos = encrypted.indexOf("__");
        String d2 = encrypted.substring(pos + 2);
        String[] items = d2.split("[_]+");
        String d1 = new DesEncrypter(Security.md5(d2).substring(4, 4)).decrypt64(encrypted.substring(0, pos));
        List<String> data = new ArrayList<String>();
        data.add(d1);
        for (String s : items) {
            if (s == null || s.trim().length() == 0) {
                continue;
            }
            data.add(s);
        }
        return data;
    }

    public static void main(String[] args) {
        DesEncrypter encrypter = new DesEncrypter();
        String encrypt = encrypter.encrypt("nihao");
        System.out.println(encrypt + " = " + encrypter.decrypt(encrypt));

        encrypt = selfencrypt("a", "b", "c", "d");
        System.out.println(encrypt + " = " + selfdecrypt(encrypt));
    }
}
