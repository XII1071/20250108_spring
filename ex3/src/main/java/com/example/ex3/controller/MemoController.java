package com.example.ex3.controller;

import com.example.ex3.dto.MemoDTO;
import com.example.ex3.entity.Memo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/memo")
public class MemoController {
  @GetMapping("/get")
  public String get() {
    return "/memo/memo";
  }

  @PostMapping("/post")
  public String post(MemoDTO memoDTO) {
    System.out.println(">> "+memoDTO);

    return "/memo/memo";
  }
}
