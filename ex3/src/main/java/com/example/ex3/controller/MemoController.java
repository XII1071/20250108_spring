package com.example.ex3.controller;

import com.example.ex3.dto.MemoDTO;
import com.example.ex3.entity.Memo;
import com.example.ex3.service.MemoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/memo")

//get 페이지 이동 post는 전송
public class MemoController {
  public MemoController(MemoService memoService) {
    this.memoService = memoService;
  }

  private final MemoService memoService;

  @GetMapping("/regist")
  public String get() {
    return "/memo/regist";
  }

  @PostMapping("/post")
  public String post(MemoDTO memoDTO, Model model) {
    // model 뷰단으로 보내는것.
    System.out.println(">> "+memoDTO);
    Long mno = memoService.register(memoDTO);
    model.addAttribute("mno", mno);
    return "/memo/memo";
  }
  @GetMapping("/read")
  public String read() {
    return "/memo/read";
  }

}
