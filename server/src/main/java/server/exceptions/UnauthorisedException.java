package server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import java.util.stream.Collectors;

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
