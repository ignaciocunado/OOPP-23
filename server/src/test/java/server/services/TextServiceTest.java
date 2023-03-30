package server.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public final class TextServiceTest {

    @Test
    public void constructorTest() {
        new TextService();
    }
    @Test
    public void randomStringTest() {
        final TextService service = new TextService(new Random(1));
        Assertions.assertEquals(service.randomAlphanumericalString(10), "naVzUgesOi");
        Assertions.assertEquals(service.randomAlphanumericalString(10), "j7HBQoiSav");
        Assertions.assertEquals(service.randomAlphanumericalString(10), "4IwTA9QH1Y");
        Assertions.assertEquals(service.randomAlphanumericalString(10), "P4IUHrXKRA");
        Assertions.assertEquals(service.randomAlphanumericalString(10), "bQ7zfyEoin");
    }

}
