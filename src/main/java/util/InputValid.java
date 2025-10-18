package util;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.matches;

public class InputValid {

    public static boolean isValidIp(String ipAddress) {
        Pattern IP_PATTERN = Pattern.compile("^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.|$)){4}$");
        return ipAddress != null && IP_PATTERN.matcher(ipAddress).matches();
    }

}
