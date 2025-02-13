package com.example.ex8.security.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilTests {
  private JWTUtil jwtUtil;

  @BeforeEach
  public void testBefore() {
    System.out.println("testBefore..........");
    jwtUtil = new JWTUtil();
  }

  @Test
  public void testEncode() throws Exception {
    String email = "u95@a.a";
    String str = jwtUtil.generateToken(email);
    System.out.println(str);
  }
}