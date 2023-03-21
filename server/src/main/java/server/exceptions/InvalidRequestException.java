package server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public final class InvalidRequestException extends ServerException {

    /**
     * Constructs a new exception for when there is a problem validating
     * an object
     * @param errors wrapping object for potential validating errors
     */
    public InvalidRequestException(final BindingResult errors) {
        super(
                HttpStatus.BAD_REQUEST,
                "Invalid request, check reason",
                errors.getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + " " + error.getDefaultMessage())
                        .collect(Collectors.toList()));
    }

}
