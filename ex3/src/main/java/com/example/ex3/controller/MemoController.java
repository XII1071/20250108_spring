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

public class MemoController {
  public MemoController(MemoService memoService) {
    this.memoService = memoService;
  }

  private final MemoService memoService;

  @GetMapping("/regist")
  public String get() {
    return "/memo/regist";
  }

  @PostMapping("/regist")
  public String post(MemoDTO memoDTO, Model model) {
    System.out.println(">> "+memoDTO);
    Long mno = memoService.register(memoDTO);
    model.addAttribute("mno", mno);
    return "/memo/regist";
  }

  // 단순히 읽기 페이지로만 이동
  @GetMapping("/read")
  public void read() {  }

  // 읽기 페이지에서 글번호를 글내용 가져오기
  @GetMapping("/get")
  public String readGet(Long mno, Model model) {
    System.out.println("get");
    MemoDTO memoDTO = memoService.read(mno);
    System.out.println(memoDTO);
    model.addAttribute("memoDto", memoDTO);
    return "/memo/read";
  }

}
