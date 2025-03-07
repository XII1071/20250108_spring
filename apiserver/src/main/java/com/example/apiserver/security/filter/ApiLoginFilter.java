package com.example.apiserver.security.filter;

import com.example.apiserver.security.dto.MembersAuthDTO;
import com.example.apiserver.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {
  private JWTUtil jwtUtil;

  public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
    super(defaultFilterProcessesUrl);
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    log.info("ApiLoginFilter.....................................");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    if (email == null) {
      throw new BadCredentialsException("email cannot be null");
    }

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(email, password);
    return getAuthenticationManager().authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    log.info("ApiLoginFilter:: successfulAuthentication : " + authResult);
    log.info(authResult.getPrincipal());
    String email = ((MembersAuthDTO) authResult.getPrincipal()).getEmail();
    String token = null;
    try {
      token = jwtUtil.generateToken(email);
      response.setContentType("text/plain");
      response.getOutputStream().write(token.getBytes()); // token 발행
      log.info("generated token: " + token);
    } catch (Exception e) {e.printStackTrace();}
  }
}
