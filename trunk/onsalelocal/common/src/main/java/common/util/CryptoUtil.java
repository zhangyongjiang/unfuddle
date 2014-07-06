package common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CryptoUtil {
	/**
	 * IF ANY CHANGES HERE PLEASE UPDATE IN SDK com.apptera.britedialer.sdk.util.Security.java
	 */
	private static final String SALT = "Ssh! This is a secret!";
	
	public static final String getCryptoString(String sourceString) {
		StringBuffer hexString = new StringBuffer(50);
	    byte s[] = getMD5Bytes(sourceString,SALT);
	    for (int i = 0; i < s.length; i++) 
	    	hexString.append(Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6));
	    
	    return hexString.toString();
	}
	
	private static byte[] getMD5Bytes(String sourceString, String salt){
		byte[] result = null;
		try {
		    MessageDigest m = MessageDigest.getInstance("MD5");
		    if(salt != null)
		    	m.update(salt.getBytes("UTF8"));
		    
		    m.update(sourceString.getBytes("UTF8"));
		    result = m.digest();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	    return result;
	}
}
