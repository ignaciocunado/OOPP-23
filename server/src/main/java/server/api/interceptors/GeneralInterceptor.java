package server.api.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import server.exceptions.UnauthorisedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GeneralInterceptor implements HandlerInterceptor {

    /**
     * Handles all requests to add and read default values
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return whether the request was accepted
     */
    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {
        response.setHeader("Server", "Talio V1");
        return true;
    }


}
