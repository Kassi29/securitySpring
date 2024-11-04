package com.kass.backend.security;

import com.kass.backend.security.filter.JwtAutheticationFilter;
import com.kass.backend.security.filter.JwtValidationFilter;
import com.kass.backend.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SpringSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserService userService;

    public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration ,  @Lazy UserService userService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.userService=userService;
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.GET, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categories").permitAll()
                        .requestMatchers(HttpMethod.POST, "/categories").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categories/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/categories/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/categories/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.GET,"/products").permitAll()
                                .requestMatchers(HttpMethod.GET, "/by-location/{location}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/products//seller/{userId}").hasRole("SELLER")
                        .requestMatchers(HttpMethod.GET, "/users/findByEmail/{email}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/roles").hasRole("SELLER")
                        .requestMatchers(HttpMethod.DELETE,"/users/eliminar/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users/change-password").authenticated()

                                /*
                                                      .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                                                      .requestMatchers(HttpMethod.DELETE, "/users/eliminar/{id}").hasRole("ADMIN")
                                                              .requestMatchers(HttpMethod.GET, "/empresas").hasRole("ADMIN")
                                                              .requestMatchers(HttpMethod.POST, "/empresas").hasRole("ADMIN")
                                                              .requestMatchers(HttpMethod.GET, "/empresas/{id}").hasRole("ADMIN")
                                                              .requestMatchers(HttpMethod.DELETE, "/empresas/{id}").hasRole("ADMIN")
                                                              .requestMatchers(HttpMethod.GET, "/communities").hasRole("ADMIN")
                                                              .requestMatchers(HttpMethod.POST, "/communities").hasRole("ADMIN")
                                                              .requestMatchers(HttpMethod.GET, "/communities/{id}").hasRole("ADMIN")
                                                              .requestMatchers(HttpMethod.DELETE, "/communities/{id}").hasRole("ADMIN")
                                                      .requestMatchers(HttpMethod.GET, "/users/roles").hasRole("SELLER")




                                                       */
                             .anyRequest().permitAll()

                       // .anyRequest().authenticated()
                )
                .addFilter(new JwtAutheticationFilter(authenticationManager(),userService))
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );


        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // Permitir cualquier origen
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
