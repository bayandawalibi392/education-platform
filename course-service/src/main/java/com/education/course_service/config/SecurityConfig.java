package com.education.course_service.config;

import com.education.course_service.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableMethodSecurity

public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/courses").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/courses/create").hasRole("TEACHER")
                        .requestMatchers("/courses/approve/**").hasRole("ADMIN")
                        .requestMatchers("/courses/edit/**").hasRole("TEACHER")
                        .requestMatchers("/courses/delete/**").hasRole("TEACHER")
                        .requestMatchers("/courses/{courseId}/add_lessons").hasRole("TEACHER")
                        .requestMatchers("/courses/{courseId}/lessons").authenticated()
                        .requestMatchers("/courses/my-courses-with-students").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/courses/**").permitAll()

                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
