package server.api.interceptors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import server.api.interceptors.AdminInterceptor;
import server.exceptions.UnauthorisedException;
import server.services.TextService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminInterceptorTest {

    private AdminInterceptor adminInterceptor;

    @BeforeEach
    public void setup() {
        final TextService textService = mock(TextService.class);
        when(textService.randomAlphanumericalString(10)).thenReturn("password");
        adminInterceptor = new AdminInterceptor(textService);
    }

    @Test
    public void testPreHandle_withValidCredentials_shouldReturnTrue() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("authorization", "Basic YWRtaW46cGFzc3dvcmQ="); // base64 encoded string "admin:password"
        MockHttpServletResponse response = new MockHttpServletResponse();
        boolean result = adminInterceptor.preHandle(request, response, mock(Object.class));
        Assertions.assertTrue(result);
    }

    @Test
    public void testPreHandle_withInvalidCredentials_shouldThrowUnauthorisedException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("authorization", "Basic dXNlcjpwYXNzd29yZA=="); // base64 encoded string "user:password"
        MockHttpServletResponse response = new MockHttpServletResponse();
        Assertions.assertThrows(
            UnauthorisedException.class, () -> adminInterceptor.preHandle(request, response, mock(Object.class)));
    }

    @Test
    public void testPreHandle_withMissingAuthorizationHeader_shouldThrowUnauthorisedException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Assertions.assertThrows(UnauthorisedException.class, () -> adminInterceptor.preHandle(request, response, mock(Object.class)));
    }

}
