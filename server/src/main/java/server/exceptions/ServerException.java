package server.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ServerException extends RuntimeException {

    private final HttpStatus status;
    private final Object reason;

    public ServerException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
        this.reason = null;
    }

    public ServerException(final HttpStatus status, final String message, final Object reason) {
        super(message);
        this.status = status;
        this.reason = reason;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public Object getReason() {
        return this.reason;
    }

}
