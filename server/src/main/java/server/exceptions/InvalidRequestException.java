package server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

public final class InvalidRequestException extends ServerException {

    public InvalidRequestException(final BindingResult errors) {
        super(
                HttpStatus.BAD_REQUEST,
                "Invalid request, check reason",
                errors.getAllErrors()
                        .stream()
                        // This should be safe as the only annotations are field annotations
                        .map(error -> (FieldError) error)
                        .map(error -> error.getField() + " " + error.getDefaultMessage())
                        .collect(Collectors.toList()));
    }

}
