package com.example.ex8.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {
  // ApiCheckFilter의 기능 2가지
  // 1) 요청된 주소와 패턴이 일치하는 비교
  // 2) 일치하면 조건을 걸 수 있다.

  private String[] pattern;
  private AntPathMatcher antPathMatcher;

  public ApiCheckFilter(String[] pattern) {
    this.pattern = pattern;
    antPathMatcher = new AntPathMatcher();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // client 요청 주소와 패턴을 비교후 같으면 header에 Authorization에 값이 있는지 확인하는 메서드
    log.info("ApiCheckFilter................................");
    log.info("REQUEST URI: " + request.getRequestURI());

    boolean check = false;
    for (int i = 0; i < pattern.length; i++) {
      if (antPathMatcher.match(request.getContextPath() + pattern[i], request.getRequestURI())) {
        check = true;
        break;
      }
    }
    if (check) { // 요청주소와 패턴이 일치한 경우
      log.info("check:" + check);
      if (checkAuthHeader(request)) { // header에 Authorization 값이 있는 경우
        filterChain.doFilter(request, response);
        return;
      } else {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "403");
        jsonObject.put("message", "Fail check API token");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonObject);
        return;
      }
    }
    filterChain.doFilter(request, response);

  }

  private boolean checkAuthHeader(HttpServletRequest request) {
    boolean chkResult = false;
    String authHeader = request.getHeader("Authorization");
    if (StringUtils.hasText(authHeader)) {
      log.info("Authorization exist: " + authHeader);
      if (authHeader.equals("12345678")) {
        chkResult = true;
      }
    }
    return chkResult;
  }
}