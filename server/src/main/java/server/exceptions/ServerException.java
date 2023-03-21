package server.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ServerException extends RuntimeException {

    private final HttpStatus status;
    private final Object reason;

    /**
     * Ancestor for all custom exceptions with the necessary fields
     * @param status the http status to return
     * @param message the message to reply
     * @param reason the reason which is nullable
     */
    public ServerException(final HttpStatus status, final String message, final Object reason) {
        super(message);
        this.status = status;
        this.reason = reason;
    }

    /**
     * Gets the status to return
     * @return the status code
     */
    public HttpStatus getStatus() {
        return this.status;
    }

    /**
     * Gets the nullable reason
     * @return the reason
     */
    public Object getReason() {
        return this.reason;
    }

}
