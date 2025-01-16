package com.example.ex4.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guestbook")
@Log4j2
public class GuestbookController {

  @GetMapping({"/list", "", "/"})
  public String list() {

    return "/guestbook/list";
  }

  @GetMapping("/regist")
  public void regist() {

  }
  @PostMapping("/regist")
  public String registPost() {

    return "/guestbook/list";
  }
  @GetMapping({"/read", "/modify"})
  public void read() {

  }

  @PostMapping("/remove")
  public String remove() {
    return "/guestbook/list";
  }
}
