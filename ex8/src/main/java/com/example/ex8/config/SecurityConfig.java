package com.example.ex8.config;

import com.example.ex8.security.filter.ApiCheckFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    // 지정하려는 filter가 어느 필터 앞에서 진행되는지를 지정
    httpSecurity.addFilterBefore(apiCheckFilter(),
//        BasicAuthenticationFilter.class
        UsernamePasswordAuthenticationFilter.class //아이디와 비번기반으로 동작하는 필터
    );

    return httpSecurity.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ApiCheckFilter apiCheckFilter() {
    return new ApiCheckFilter(new String[]{"/notes/**"});
  }

}




