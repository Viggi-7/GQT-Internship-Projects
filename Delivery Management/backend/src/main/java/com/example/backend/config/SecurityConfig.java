package com.example.backend.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; // Import HttpMethod
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Import Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource; // Import CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Import CorsConfigurationSource

import com.example.backend.service.CustomUserDetailsService; // Import UrlBasedCorsConfigurationSource

import lombok.RequiredArgsConstructor; // Import Arrays

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults()) // Enable CORS using the bean defined below
            .authorizeHttpRequests(auth -> auth
                // Allow all requests to the authentication endpoints
                .requestMatchers("/api/auth/**").permitAll()
                // Allow OPTIONS requests for all paths (CORS preflight)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // IMPORTANT for CORS preflight
                // Secure seller-specific endpoints
                .requestMatchers("/api/seller/**").hasRole("SELLER")
                // Secure delivery-specific endpoints
                .requestMatchers("/api/delivery/**").hasRole("DELIVERY_BOY")
                // Any other request must be authenticated
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures the global CORS policy for the application.
     * This bean allows cross-origin requests from the React frontend development server.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow requests from your React application's origin (http://localhost:3000).
        // Add other frontend origins if necessary, e.g., "http://127.0.0.1:3000"
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000"));
        // Allow specific HTTP methods required by your API
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // Allow specific headers to be sent with the request (Authorization for JWT)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        // Allow credentials (like cookies, HTTP authentication, or authorization headers) to be sent
        configuration.setAllowCredentials(true);
        // How long the preflight request (OPTIONS) can be cached by the client
        configuration.setMaxAge(3600L); // 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this CORS configuration to all paths (/**)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    //@SuppressWarnings("deprecation")
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
