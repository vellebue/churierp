package org.bastanchu.churierp.churierpweb.core;

import org.bastanchu.churierp.churierpback.dto.LanguageDto;
import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto;
import org.bastanchu.churierp.churierpback.service.LanguageService;
import org.bastanchu.churierp.churierpback.service.administration.UserService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.LocaleResolver;


import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Locale;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(WebSecurityConfiguration.class);
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.inMemoryAuthentication()
        //        .withUser("angel").password(passwordEncoder().encode("angel8")).roles("USER");
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select login as username, password as password, 'true' as enabled " +
                        " from USERS where login = ? ")
                .authoritiesByUsernameQuery("select login as username, 'AUTH0' as authority " +
                        " from USERS where login = ? ");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterAfter(new CustomLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout*").permitAll()
                .antMatchers("/VAADIN/**").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/main", true)
                .failureUrl("/login?error=true")
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID");
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MessageDigestPasswordEncoder("SHA-256");
    }

    private class CustomLoginFilter extends HttpFilter {

        private static final String LANGUAGE_SET = "LANG_SET";

        private LocaleResolver localeResolver;
        private UserService userService;
        private LanguageService languageService;


        @Override
        protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            HttpSession session = request.getSession();
            if ((auth != null) && auth.isAuthenticated() && (session.getAttribute(LANGUAGE_SET) == null)) {
                initApplicationContext(request);
                String login = auth.getName();
                UserDto userDto =  userService.getUserByLogin(login);
                if (userDto != null) {
                    Integer languageId = Integer.parseInt(userDto.getLanguageId());
                    LanguageDto languageDto = languageService.getLanguageById(languageId);
                    if (languageDto.getCountryId() != null) {
                        localeResolver.setLocale(request, response, new Locale(languageDto.getLangId(), languageDto.getCountryId()));
                    } else {
                        localeResolver.setLocale(request, response, new Locale(languageDto.getLangId()));
                    }
                    session.setAttribute(LANGUAGE_SET, LANGUAGE_SET);
                }
            }
            super.doFilter(request, response, chain);
        }

        private void initApplicationContext(HttpServletRequest request) {
            ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            localeResolver = (LocaleResolver) applicationContext.getBean("localeResolver");
            userService = (UserService) applicationContext.getBean("userService");
            languageService = (LanguageService) applicationContext.getBean("languageService");
        }
    }
}
