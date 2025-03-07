package com.example.apiserver.security.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JWTUtilTests {
  private JWTUtil jwtUtil;

  @BeforeEach
  public void testBefore() {
    System.out.println("testBefore..........");
    jwtUtil = new JWTUtil();
  }

  @Test
  public void testEncode() throws Exception {
    String email = "m90@m.m";
    String str = jwtUtil.generateToken(email);
    System.out.println(str);
  }
}