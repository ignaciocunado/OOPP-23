package server.exceptions;

import org.junit.jupiter.api.Test;

public final class EntityNotFoundExceptionTest {

    @Test
    public void testEntityNotFoundConstructor() {
        new EntityNotFoundException("");
    }

}
