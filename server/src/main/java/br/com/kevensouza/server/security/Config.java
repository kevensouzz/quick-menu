package br.com.kevensouza.server.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class Config {
    private final Filter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // users
                        .requestMatchers(HttpMethod.POST, "/users/check-password/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/users/password/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/users/picture/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/users/role/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").authenticated()

                        // eataries
                        .requestMatchers(HttpMethod.POST, "/users/**/eataries").authenticated()
                        .requestMatchers(HttpMethod.POST, "/eataries/**/host/**/add/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/eataries/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/eataries/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/eataries/picture/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/eataries/**/host/**/remove/**").authenticated()

                        // menus
                        .requestMatchers(HttpMethod.POST, "/eataries/**/menus").authenticated()
                        .requestMatchers(HttpMethod.GET, "/menus/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/menus/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/menus/picture/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/eataries/**/menus/**").authenticated()

                        // options
                        .requestMatchers(HttpMethod.POST, "/menus/**/options").authenticated()
                        .requestMatchers(HttpMethod.GET, "/options/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/options/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/options/picture/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/menus/**/options/**").authenticated()

                        //
                        .anyRequest().permitAll()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfigurations) throws Exception {
        return authConfigurations.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}