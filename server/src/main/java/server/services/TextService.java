package server.services;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class TextService {

    private Random random;
    private String alphaNumericalSource = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public TextService() {
        this.random = ThreadLocalRandom.current();
    }

    public TextService(final Random random) {
        this.random = random;
    }

    /**
     * Generates a string of alphanumerical string
     * @param n the length of the randomly generated string
     * @return random string
     */
    public String randomString(final int n, final String src) {
        return this.random.ints(n,0,src.length()).mapToObj(number -> String.valueOf(src.charAt(number))).collect(Collectors.joining());
    }

    public String randomAlphanumericalString(final int n) {
        return randomString(n, alphaNumericalSource);
    }

}
