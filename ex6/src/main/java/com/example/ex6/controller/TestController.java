package com.example.ex6.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
  @GetMapping("/uploadEx")
  public void uploadEx() {
  }
}
