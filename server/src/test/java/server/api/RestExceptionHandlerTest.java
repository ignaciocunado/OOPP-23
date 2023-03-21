package server.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.exceptions.ServerException;

public class RestExceptionHandlerTest {

    @Test
    public void handleServerExceptionTest() {
        final RestExceptionHandler handler = new RestExceptionHandler();

        final ServerException testException = Mockito.mock(ServerException.class);
        Mockito.when(testException.getStatus()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        Mockito.when(testException.getMessage()).thenReturn("Test Message");
        Mockito.when(testException.getReason()).thenReturn("Good reason");
        final ResponseEntity response = handler.handleServerExceptions(testException);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        final RestExceptionHandler.APIError error = (RestExceptionHandler.APIError) response.getBody();
        Assertions.assertEquals(error.getMessage(), "Test Message");
        Assertions.assertEquals(error.getReason(), "Good reason");
        Assertions.assertNotEquals(error.getTimestamp(), null);
    }

}
