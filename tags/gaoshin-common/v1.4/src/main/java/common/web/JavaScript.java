package common.web;

public class JavaScript {
	public static String escSingle(String s) {
		if(s == null) {
			return "";
		}
		return s.replaceAll("'", "\\\\'");
	}

	public static String escDouble(String s) {
		if(s == null) {
			return "";
		}
		return s.replaceAll("\"", "\\\\\"");
	}

	public static void main(String[] args) {
		System.out.println(escSingle("aa"));
		System.out.println(escSingle("a'a"));
		System.out.println(escDouble("bb"));
		System.out.println(escDouble("b\"b"));
	}
}
