package server.api.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PingControllerTest {

    @Test
    public void pingTest() {
        final PingController ctrl = new PingController();
        final ResponseEntity<String> response = ctrl.ping();
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

}
