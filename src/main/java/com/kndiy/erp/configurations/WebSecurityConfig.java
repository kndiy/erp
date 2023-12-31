package com.kndiy.erp.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .anonymous()
                .and()
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/", "/login-page", "/resume", "/files/**", "/styles/**", "/fonts/**", "/scripts/**", "/images/**", "/register").permitAll()
                                .anyRequest().hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") //has to manually set this up to let users with ROLE_ADMIN access the application after logging in
                )
                .formLogin(
                        form -> form
                                .loginPage("/login-page").permitAll()
                                .loginProcessingUrl("/login/login").permitAll()
                                .defaultSuccessUrl("/welcome", true).permitAll()
                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                                .logoutSuccessUrl("/")
                );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
