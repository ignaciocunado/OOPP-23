package commons;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TextUtils {

    private static String alphaNumericalSource = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Generates a string of alphanumerical string
     * @param n the length of the randomly generated string
     * @return random string
     */
    public static String randomString(final int n, final String src) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.ints(n,0,src.length()).mapToObj(number -> String.valueOf(src.charAt(number))).collect(Collectors.joining());
    }

    public static String randomAlphanumericalString(final int n) {
        return randomString(n, alphaNumericalSource);
    }

}
