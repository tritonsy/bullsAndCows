package com.bullsncows.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.time.Duration;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/login", "/resources/**", "/registration", "/rating")
                .permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))

                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("login")
                .passwordParameter("pass")
                //Redirect principal to his page
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    final String
                            login =
                            ((User) SecurityContextHolder.getContext()
                                    .getAuthentication()
                                    .getPrincipal()).getUsername();
                    httpServletResponse.sendRedirect(getRedirectString(httpServletRequest, "/person/" + login));
                })
                .failureHandler((httpServletRequest, httpServletResponse, e) -> httpServletResponse.sendRedirect(
                        getRedirectString(httpServletRequest, "/login?error")))


                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> httpServletResponse.sendRedirect(
                        getRedirectString(httpServletRequest, "/login?logout")))

                .and()
                .rememberMe()
                .tokenValiditySeconds((int) Duration.ofDays(1).toMillis())

                .and()
                .csrf()
                .csrfTokenRepository(csrfTokenRepository());
    }

    private String getRedirectString(HttpServletRequest httpServletRequest, String redirect) {
        return httpServletRequest.getRequestURL()
                .replace(httpServletRequest.getRequestURL().length() -
                                httpServletRequest.getServletPath().length(),
                        httpServletRequest.getRequestURL().length(),
                        redirect)
                .toString();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }
}
