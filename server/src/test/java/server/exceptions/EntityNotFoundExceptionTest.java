package server.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public final class EntityNotFoundExceptionTest {

    @Test
    public void entityNotFoundConstructorTest() {
        new EntityNotFoundException("");
    }

    @Test
    public void entityNotFoundGetStatusTest() {
        Assertions.assertEquals(new EntityNotFoundException("").getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void entityNotFoundGetReasonTest() {
        Assertions.assertEquals(new EntityNotFoundException("Good Reason").getReason(), "Good Reason");
    }

}
