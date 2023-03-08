package server.services;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class TextService {

    private Random random;
    private String alphaNumericalSource = "abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Default constructor for text service
     * Uses a thread safe random
     */
    public TextService() {
        this.random = ThreadLocalRandom.current();
    }

    /**
     * Constructor for testing
     * @param random the random instance to use
     */
    public TextService(final Random random) {
        this.random = random;
    }

    /**
     * Generates a random string using the source
     * @param n the length of the randomly generated string
     * @param src the source of characters to pick from
     * @return random string
     */
    private String randomString(final int n, final String src) {
        return this.random.ints(n,0,src.length())
                .mapToObj(number -> String.valueOf(src.charAt(number)))
                .collect(Collectors.joining());
    }

    /**
     * Generates a string of random alphanumerical characters
     * @param n the length of the randomly generated string
     * @return random string
     */
    public String randomAlphanumericalString(final int n) {
        return randomString(n, alphaNumericalSource);
    }

}
