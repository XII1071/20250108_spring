package com.example.apiserver.config;

import com.example.apiserver.security.filter.ApiCheckFilter;
import com.example.apiserver.security.filter.ApiLoginFilter;
import com.example.apiserver.security.handler.ApiLoginFailHandler;
import com.example.apiserver.security.util.JWTUtil;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  private static final String[] AUTH_WHITELIST = {
      "/members/register"
  };

  @Bean
  protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

    httpSecurity.authorizeHttpRequests(
        auth -> auth
            //.anyRequest().permitAll() // 모든 주소 허용 :: 단독 사용

            // 회원가입이기 때문에 무조건 수용(나중에 CORS로 지정하면 됨)
            .requestMatchers(AUTH_WHITELIST).permitAll()

            // 조건부 허용::주소는 열어 줬지만, 토큰으로 필터 체크
            .requestMatchers(new AntPathRequestMatcher("/journal/**")).permitAll()
            .requestMatchers("/comments/**").permitAll()
            .requestMatchers("/members/get/**").permitAll()

            // 그 외는 모두 막음.
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
    return new ApiCheckFilter(
        new String[]{"/comments/**", "/journal/**", "/members/get/**"}
        , jwtUtil());
  }

  @Bean
  public ApiLoginFilter apiLoginFilter(
      // AuthenticationConfiguration :: Spring Security에서 모든 인증을 처리(UserDetailsService호출)
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/auth/login", jwtUtil());

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





