package be.tabtabstudio.veganapp.utilities;

public class StringUtils {
    public static String capitize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
