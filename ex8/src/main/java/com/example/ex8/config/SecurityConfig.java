package com.example.ex8.config;

import com.example.ex8.security.filter.ApiCheckFilter;
import com.example.ex8.security.filter.ApiLoginFilter;
import com.example.ex8.security.handler.ApiLoginFailHandler;
import com.example.ex8.security.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  private static final String[] AUTH_WHITELIST = {
      "/"
      //,"/notes/**"
  };

  @Bean
  protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

    httpSecurity.authorizeHttpRequests(
        auth -> auth
            //.requestMatchers(AUTH_WHITELIST).permitAll()
            //.requestMatchers(new AntPathRequestMatcher("/notes/**")).permitAll()
            .requestMatchers("/notes/**").permitAll()
            .anyRequest().denyAll()
    );

    httpSecurity.addFilterBefore(
        apiCheckFilter(),
        UsernamePasswordAuthenticationFilter.class //아이디,비번 기반 필터 실행 전 apiCheckFilter호출
    );

    httpSecurity.addFilterBefore(
        apiLoginFilter(httpSecurity.getSharedObject(AuthenticationConfiguration.class)),
        UsernamePasswordAuthenticationFilter.class
    );

    return httpSecurity.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ApiCheckFilter apiCheckFilter() {
    return new ApiCheckFilter(new String[]{"/notes/**"}, jwtUtil());
  }

  @Bean
  public ApiLoginFilter apiLoginFilter(
      // AuthenticationConfiguration :: Spring Security에서 모든 인증을 처리(UserDetailsService호출)
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login", jwtUtil());

    apiLoginFilter.setAuthenticationManager(
        authenticationConfiguration.getAuthenticationManager()
    );
    apiLoginFilter.setAuthenticationFailureHandler(
        getApiLoginFailHandler()
    );
    return apiLoginFilter;
  }

  @Bean
  public ApiLoginFailHandler getApiLoginFailHandler() {
    return new ApiLoginFailHandler();
  }

  @Bean
  public JWTUtil jwtUtil() {
    return new JWTUtil();
  }
}





