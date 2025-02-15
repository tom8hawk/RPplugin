package t.me.tom8hawk.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static String fastReplace(String string, String target, String replacement) {
        if (string == null) {
            return null;
        }

        int index = string.indexOf(target);
        if (index == -1) {
            return string;
        }

        return string.substring(0, index) + replacement + string.substring(index + target.length());
    }
}
