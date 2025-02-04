package com.example.ex6.controller;

import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {
  private final MovieService movieService;

  @GetMapping({"", "/", "/list"})
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    model.addAttribute("result", movieService.getList(pageRequestDTO));
    return "/board/list";
  }

  @GetMapping("/register")
  public void register() {  }



}




