package com.capillary.ops.deployer;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ErrorPageConfiguration implements WebMvcConfigurer {


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/notFound").setViewName("forward:/index.html");
        registry.addViewController("/pages").setViewName("forward:/index.html");
        registry.addViewController("/pages/**").setViewName("forward:/index.html");
        registry.addViewController("/").setViewName("redirect:/index.html");
        registry.addViewController("/capc").setViewName("forward:/capc/index.html");
        registry.addViewController("/capc/").setViewName("forward:/capc/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

    }


    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return container -> {
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
                    "/notFound"));
        };
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/index.html")
//                .addResourceLocations("classpath:/static/")
//                .setCacheControl(CacheControl.noCache());
//    }
}
