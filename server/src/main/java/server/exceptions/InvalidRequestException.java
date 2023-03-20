package server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public final class InvalidRequestException extends ServerException {

    public InvalidRequestException(final BindingResult errors) {
        super(
                HttpStatus.BAD_REQUEST,
                "Invalid request, check reason",
                errors.getAllErrors()
                        .stream()
                        .map(error -> error.getObjectName() + " " + error.getDefaultMessage())
                        .collect(Collectors.toList()));
    }
}
