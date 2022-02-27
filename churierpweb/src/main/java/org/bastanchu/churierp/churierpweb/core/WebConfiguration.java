package org.bastanchu.churierp.churierpweb.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@EnableWebMvc
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public LocaleChangeInterceptor localeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeInterceptor());
    }
}
