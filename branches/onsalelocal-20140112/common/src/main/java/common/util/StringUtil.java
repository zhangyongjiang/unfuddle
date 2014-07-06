package common.util;

public class StringUtil {
	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}
	
	public static String nullIfEmpty(String s) {
		if (s == null || s.trim().length() == 0) return null;
		return s;
	}
	
	public static String trim(String s) {
		if(s == null)
			return s;
		return s.trim();
	}
}
