
package com.example.forum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Fotoğrafların kaydedildiği dizin
        registry.addResourceHandler("/profileImages/**")
                .addResourceLocations("file:src/main/resources/static/profileImages/");
    }
}
