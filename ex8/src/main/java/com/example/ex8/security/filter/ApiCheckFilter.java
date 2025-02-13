package com.example.ex8.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {
  // ApiCheckFilter의 기능 2가지
  // 1) 요청된 주소와 패턴이 일치하는 비교
  // 2) 일치하면 조건을 걸 수 있다.

  private String[] pattern;
  private AntPathMatcher antPathMatcher;

  public ApiCheckFilter(String[] pattern) {
    this.pattern = pattern;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("ApiCheckFilter................................");
    log.info("ApiCheckFilter................................");
    log.info("ApiCheckFilter................................");
    log.info("REQUEST URI: " + request.getRequestURI());

    boolean check = false;
    for (int i = 0; i < pattern.length; i++) {
      log.info(
          antPathMatcher.match(request.getContextPath() + pattern[i], request.getRequestURI())
      );
      if (antPathMatcher.match(request.getContextPath() + pattern[i], request.getRequestURI())) {
        check = true;
        break;
      }
    }
    if (check) {
      log.info("check:" + check);

    }
    filterChain.doFilter(request, response);

  }
}