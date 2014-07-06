package common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public final class PasswordService {
	private static final Logger log = Logger.getLogger(PasswordService.class);

	private static PasswordService instance;
	
	private PasswordService(){
	}
	
	public synchronized String encrypt(String plaintext)
	{
		String encrypted = null;
		MessageDigest md = null;
	    try{
	    	md = MessageDigest.getInstance("MD5"); //Or SHA
	    	md.update(plaintext.getBytes("UTF-8")); 
	    	byte raw[] = md.digest();
	    	//encrypted = (new BASE64Encoder()).encode(raw);
		    Base64 encoder = new Base64();
		    encrypted = encoder.encode(raw);	    	
	    }catch(NoSuchAlgorithmException e){
	    	log.error(e.getMessage());
	    }catch(UnsupportedEncodingException e){
	    	log.error(e.getMessage());
	    }	    
	    return encrypted;
	}
  
  	public static synchronized PasswordService getInstance() {
  		if(instance == null){
		  	return new PasswordService();
  		} else {
  			return instance;
  		}
  	}
}
