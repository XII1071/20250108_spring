package com.example.ex2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// template engine을 이용하면 @controller를 필수로 사용!
@Controller
  @RequestMapping("/member")
  public class MemberController {
    @RequestMapping("/join")
    public void join(){ }
  @RequestMapping("/login")
  public void login(){ }


}
