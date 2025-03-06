package com.example.apiserver.security.filter;

import com.example.apiserver.security.util.JWTUtil;
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
  private String[] pattern;
  private AntPathMatcher antPathMatcher;
  private JWTUtil jwtUtil;

  public ApiCheckFilter(String[] pattern, JWTUtil jwtUtil) {
    this.pattern = pattern;
    this.jwtUtil = jwtUtil;
    antPathMatcher = new AntPathMatcher();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // client요청 주소와 패턴이 같은지 비교후 같으면 header에 Authorization에 값이 있는지 확인하는 메서드
    log.info("ApiCheckFilter................................");
    log.info("REQUEST URI: " + request.getRequestURI());

    boolean check = false;
    for (int i = 0; i < pattern.length; i++) {
      if (antPathMatcher.match(request.getContextPath() + pattern[i], request.getRequestURI())) {
        check = true;
        break;
      }
    }
    if (check) {  // 요청주소와 패턴이 일치한 경우
      log.info("check:" + check);
      if (checkAuthHeader(request)) { // header에 Authorization값이 있는 경우
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

    // Authorization 헤더는 일반적으로 Basic으로 시작, JWT으로 시작할 경우 Bearer 사용
    if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
      log.info("Authorization exist: " + authHeader);
      // if (authHeader.equals("12345678")) chkResult = true;
      try {
        String email = jwtUtil.validateAndExtract(authHeader.substring(7));
        log.info("Validate result: " + email);
        chkResult = email.length() > 0;
      } catch (Exception e) {e.printStackTrace();}
    }
    return chkResult;
  }
}
