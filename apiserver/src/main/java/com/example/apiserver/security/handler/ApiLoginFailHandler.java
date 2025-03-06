package com.example.apiserver.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiLoginFailHandler implements AuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(HttpServletRequest request
      , HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    log.info("login fail handler...............");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=utf-8");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("code", "401");
    jsonObject.put("message", exception.getMessage());
    PrintWriter printWriter = response.getWriter();
    printWriter.println(jsonObject);
  }
}



