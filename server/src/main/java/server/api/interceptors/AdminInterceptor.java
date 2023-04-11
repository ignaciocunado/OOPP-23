package server.api.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import server.exceptions.UnauthorisedException;
import server.services.TextService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminInterceptor implements HandlerInterceptor {

    private final Logger logger = Logger.getLogger("Admins");
    private final TextService textService;
    private final String password;

    /**
     * Constructor to get the text service for generating the password
     * @param textService the text service
     */
    public AdminInterceptor(final TextService textService) {
        this.textService = textService;
        this.password = this.textService.randomAlphanumericalString(10);

        this.logger.log(Level.INFO, "The current admin password is: "+this.password);
    }

    /**
     * Handles admin requests to authorise and reject if necessary
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return whether the request was accepted
     */
    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response, final Object handler) {
        final String authHeader = request.getHeader("authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            throw new UnauthorisedException("Invalid authorisation header");
        }
        String base64Credentials = authHeader.substring("Basic ".length());
        byte[] credentialsBytes = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credentialsBytes, StandardCharsets.UTF_8);

        String[] parts = credentials.split(":");
        String username = parts[0];
        String password = parts[1];

        // validate username and password against a user store or authentication provider
        if (!"admin".equals(username) || !this.password.equals(password)) {
            throw new UnauthorisedException("Invalid username or password");
        }
        return true;
    }


}
