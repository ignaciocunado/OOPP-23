package server.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public final class TextService {

    private String alphaNumericalSource = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Generates a string of alphanumerical string
     * @param n the length of the randomly generated string
     * @return random string
     */
    public String randomString(final int n, final String src) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.ints(n,0,src.length()).mapToObj(number -> String.valueOf(src.charAt(number))).collect(Collectors.joining());
    }

    public String randomAlphanumericalString(final int n) {
        return randomString(n, alphaNumericalSource);
    }

}
