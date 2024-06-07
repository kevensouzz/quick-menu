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
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/role/**").hasRole("ADMIN")
                        .requestMatchers("/users/**").authenticated()

                        // eataries
                        .requestMatchers("/eataries").permitAll()
                        .requestMatchers("/eataries/**").authenticated()

                        // menus
                        .requestMatchers(HttpMethod.GET, "/menus").permitAll()
                        .requestMatchers("/menus/**").authenticated()

                        // options
                        .requestMatchers(HttpMethod.GET, "/options").permitAll()
                        .requestMatchers("/options/**").authenticated()

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