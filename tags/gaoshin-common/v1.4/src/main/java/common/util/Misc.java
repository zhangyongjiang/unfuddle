package common.util;

public class Misc {
    public static String removeTag(String str) {
        if (str == null)
            return null;
        else
            return str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    public static void main(String[] args) {
    }
}
