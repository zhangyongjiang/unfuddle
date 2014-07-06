package common.util.security;

import java.security.MessageDigest;

public class MD5 {
    public static String md5(Object original) {
        if((original == null)) {
            return null;
        }
        byte[] defaultBytes = original.toString().getBytes();
        try{
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<messageDigest.length;i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if(hex.length() == 0) {
                    hex = "00";
                } else if (hex.length()==1) {
                    hex = "0" + hex;
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
