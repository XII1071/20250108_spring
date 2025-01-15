package com.example.ex3.cotroller;

import com.example.ex3.dto.MemoDTO;
import com.example.ex3.entity.Memo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
public class MemoController {
  @GetMapping("/get")
  public String get() {
    return "/memo/memo";

  }

  @PostMapping("/post")
  public void post(MemoDTO memoDTO) {
  }
}
