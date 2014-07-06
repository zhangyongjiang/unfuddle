package common.util;

public class CacheUtil {
	
	public static String mintKey(String objectId, Class objectClass) {
		if(objectId != null) {
			return objectId+objectClass.getName();
		}
		return null;
	}

}
