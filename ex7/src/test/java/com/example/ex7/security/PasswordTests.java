package com.example.ex7.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PasswordTests {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void testEncode() {
    String password = "1";
    String enPw = passwordEncoder.encode(password);

    System.out.println("enPw: " + enPw);

    boolean matchResult = passwordEncoder.matches(password, enPw);
    System.out.println("결과 : " + matchResult);

  }
}