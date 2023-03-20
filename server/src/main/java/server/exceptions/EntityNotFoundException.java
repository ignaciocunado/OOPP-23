package server.exceptions;

import org.springframework.http.HttpStatus;

public final class EntityNotFoundException extends ServerException {

    public EntityNotFoundException(final String reason) {
        super(HttpStatus.NOT_FOUND, "Entity couldn't be found in the database", reason);
    }

}
