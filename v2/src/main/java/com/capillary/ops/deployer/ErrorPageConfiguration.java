package com.capillary.ops.deployer;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
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
        registry.addViewController("/capc/{spring:[^\\.]*}/**").setViewName("forward:/capc/index.html");
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

    public static void main(String[] args){
        PathMatcher pathMatcher = new AntPathMatcher();
        String pattern = "/capc/{spring:[^\\.]*}/**";
        boolean match = pathMatcher.match(pattern, "/capc/index.html");
        boolean match1 = pathMatcher.match(pattern, "/capc/home");
        boolean match2 = pathMatcher.match(pattern, "/capc/home/a");
        boolean match3 = pathMatcher.match(pattern, "/capc/abc.css");
        boolean match4 = pathMatcher.match(pattern, "/capc/abc.js");
        boolean match5 = pathMatcher.match(pattern, "/capc/abc/ss.js");

        System.out.println(match);
        System.out.println(match1);
        System.out.println(match2);
        System.out.println(match3);
        System.out.println(match4);
        System.out.println(match5);
    }
}
