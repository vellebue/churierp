package org.bastanchu.churierp.churierpweb.core;

import com.sun.jna.platform.linux.Udev;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

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
}
