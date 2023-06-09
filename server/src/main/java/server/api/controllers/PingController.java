package server.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ping")
public class PingController {

    /**
     * Handler for checking server responsivity
     *
     * @return request to signify server status
     */
    @GetMapping(path = {"", "/"})
    public ResponseEntity<String> ping() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
