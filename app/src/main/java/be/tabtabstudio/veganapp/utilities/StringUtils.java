package be.tabtabstudio.veganapp.utilities;

public class StringUtils {
    public static String capitize(String str) {
        if (str == null || str.length() < 1) {
            return "";
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
