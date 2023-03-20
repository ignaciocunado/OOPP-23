package server.api;

import server.exceptions.ServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public final class RestExceptionHandler {

    class APIError {
        private String message;
        private Object reason;
        private Instant timestamp;
        public APIError(final ServerException exception) {
            this.message = exception.getMessage();
            this.reason = exception.getReason();
            this.timestamp = Instant.now();
        }

        public String getMessage() {
            return this.message;
        }

        public Object getReason() {
            return this.reason;
        }

        public Instant getTimestamp() {
            return this.timestamp;
        }

    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Object> handleServerExceptions(final ServerException exception) {
        return new ResponseEntity<>(new APIError(exception), exception.getStatus());
    }

}
