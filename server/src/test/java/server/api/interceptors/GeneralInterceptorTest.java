package server.api.interceptors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import server.exceptions.UnauthorisedException;

import static org.mockito.Mockito.mock;

public class GeneralInterceptorTest {

    private GeneralInterceptor generalInterceptor;

    @BeforeEach
    public void setup() {
        generalInterceptor = new GeneralInterceptor();
    }

    @Test
    public void testPreHandleShouldHaveHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        boolean result = this.generalInterceptor.preHandle(request, response, mock(Object.class));
        Assertions.assertTrue(result);
        Assertions.assertEquals(response.getHeader("Server"), "Talio V1");
    }

}
