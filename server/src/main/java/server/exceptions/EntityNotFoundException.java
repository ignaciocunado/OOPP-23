package server.exceptions;

import org.springframework.http.HttpStatus;

public final class EntityNotFoundException extends ServerException {

    /**
     * Constructs a new exception for when there is a problem finding
     * an entity in the repository
     * @param reason potential reason, could be null
     */
    public EntityNotFoundException(final String reason) {
        super(HttpStatus.NOT_FOUND, "Entity couldn't be found in the database", reason);
    }

}
