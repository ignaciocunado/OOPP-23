package server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import server.api.interceptors.AdminInterceptor;
import server.api.interceptors.GeneralInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * Adds an interceptor
     * @param registry the interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GeneralInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/api/admin/**");
    }

}
