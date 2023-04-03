package server.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorisedException extends ServerException{
    /**
     * Constructs a new exception for when there is a problem validating
     * an object
     * @param errors wrapping object for potential validating errors
     */
    public UnauthorisedException(final String errors) {
        super(
            HttpStatus.UNAUTHORIZED,
            "Invalid password, check reason",
            errors);
    }

}
