package br.com.kevensouza.server.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
//                        .requestMatchers(HttpMethod.POST, "/users/checkPass/**").authenticated()
//                        .requestMatchers(HttpMethod.GET, "/users/id/**").authenticated()
//                        .requestMatchers(HttpMethod.GET, "/users/all").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PATCH, "/users/update/**").authenticated()
//                        .requestMatchers(HttpMethod.PATCH, "/users/update/password/**").authenticated()
//                        .requestMatchers(HttpMethod.PATCH, "/users/update/role/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/users/delete/**").authenticated()
//
//                        // menus
//                        .requestMatchers(HttpMethod.POST, "/menus/create/**").authenticated()
//                        .requestMatchers(HttpMethod.GET, "/menus/id/**").authenticated()
//                        .requestMatchers(HttpMethod.PATCH, "/menus/update/**").authenticated()
//                        .requestMatchers(HttpMethod.DELETE, "/menus/delete/**").authenticated()
//
//                        // options
//                        .requestMatchers(HttpMethod.POST, "/options/create/**").authenticated()
//                        .requestMatchers(HttpMethod.PATCH, "/options/update/**").authenticated()
//                        .requestMatchers(HttpMethod.DELETE, "/options/delete/**").authenticated()

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