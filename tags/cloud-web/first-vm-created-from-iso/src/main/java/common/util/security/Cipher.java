package common.util.security;

import java.util.ArrayList;

/**
 * 
 */
public class Cipher {

    private final ArrayList<String> keyList = new ArrayList<String>();

    public void setCipherKeys(String cipherKeys) {
        String[] keys = cipherKeys.split("[\r\n\t ,;]+");
        for (String s : keys) {
            keyList.add(s);
        }
    }

    public String encrypt(String original) {
        DesEncrypter encrypter = new DesEncrypter(keyList.get(keyList.size() - 1));
        return encrypter.encrypt(original + "-" + original.length());
    }

    public String decrypt(String encrypted) {
        if (keyList.size() == 0) {
            keyList.add("sss");
        }
        for (int i = keyList.size() - 1; i >= 0; i--) {
            try {
                DesEncrypter encrypter = new DesEncrypter(keyList.get(i));
                String original = encrypter.decrypt(encrypted);
                int pos = original.lastIndexOf('-');
                if (pos != -1) {
                    int len = Integer.parseInt(original.substring(pos + 1));
                    if (len != original.substring(0, pos).length()) {
                        throw new RuntimeException("not a valid encrypted string. length doesn't match");
                    }
                    return original.substring(0, pos);
                }
            } catch (Exception e) {
            }
        }
        throw new RuntimeException("cannot decrypt encrypted");
    }
}
