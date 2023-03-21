package server.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;

public final class InvalidRequestExceptionTest {

    @Test
    public void testInvalidRequestExceptionTestConstructor() {
        new InvalidRequestException(Mockito.mock(BindingResult.class));
    }

    @Test
    public void entityNotFoundGetStatusTest() {
        Assertions.assertEquals(new InvalidRequestException(Mockito.mock(BindingResult.class)).getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void entityNotFoundGetReasonTest() {
        final FieldError error = new FieldError("Object Name", "invalidField", "must be valid");
        final BindingResult errors = Mockito.mock(BindingResult.class);
        Mockito.when(errors.getFieldErrors()).thenReturn(Arrays.asList(error));
        Assertions.assertEquals(new InvalidRequestException(errors).getReason(), Arrays.asList("invalidField must be valid"));
    }

}
