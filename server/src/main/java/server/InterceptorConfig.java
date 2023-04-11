package server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import server.api.interceptors.AdminInterceptor;
import server.api.interceptors.GeneralInterceptor;
import server.services.TextService;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final TextService textService;

    /**
     * Constructor to get the text service for generating the password
     * @param textService the text service
     */
    public InterceptorConfig(final TextService textService) {
        this.textService = textService;
    }

    /**
     * Adds an interceptor
     * @param registry the interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GeneralInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new AdminInterceptor(this.textService))
                .addPathPatterns("/api/admin/**");
    }

}
