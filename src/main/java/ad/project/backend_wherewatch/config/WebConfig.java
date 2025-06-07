package ad.project.backend_wherewatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class to set up CORS (Cross-Origin Resource Sharing) settings.
 * <p>
 * This configuration allows requests from "<a href="http://localhost:3000">...</a>" to access all endpoints,
 * supporting common HTTP methods and any headers.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Configure CORS mappings for the application.
     *
     * @param registry the CorsRegistry to configure CORS settings
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}

