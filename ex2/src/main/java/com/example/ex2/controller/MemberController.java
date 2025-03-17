package com.example.ex2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// template engine을 이용하면 @controller를 필수로 사용!
@Controller
@RequestMapping("/member")
public class MemberController {

  @RequestMapping("/join")
  public void join() {
    // void 일 경우 요청된 url이 resource 위치와 동일
  }
  @RequestMapping("/login")
  public String login() {
    // String 일 경우 요청된 url을 대신하여 임의지정 가능
    return "/member/login";
  }
  @RequestMapping("/static")
  public String getStatic(){
    // 요청된 url(/member/static)과 다른 위치 지정
    return "/static";
  }
}
