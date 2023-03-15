package com.commerce.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.commerce.util.CustomResponseUtil;

@Slf4j
@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        log.debug("Password Encoder : BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("Filter Chain");
        return http
                .headers().frameOptions().disable().and()
                .csrf().disable()
                .cors().configurationSource(null)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling().authenticationEntryPoint((request, response, authenticationException) -> {
                    CustomResponseUtil.unAuthentication(response, "로그인이 필요합니다.");
                })
                .and()
                .authorizeHttpRequests()
                .anyRequest().permitAll()
                .and().build();
    }

    @Bean
    CorsConfigurationSource configurationSource() {
        log.debug("configurationSource");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
