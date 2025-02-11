package com.example.ex6.controller;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {
  private final MovieService movieService;

  @GetMapping({"", "/", "/list"})
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    model.addAttribute("result", movieService.getList(pageRequestDTO));
    return "/movie/list";
  }

  @GetMapping("/register")
  public void register() {  }

  @PostMapping("/register")
  public String register(MovieDTO movieDTO, RedirectAttributes ra) {
    Long mno = movieService.register(movieDTO);
    ra.addFlashAttribute("msg", mno + "번 등록");
    return "redirect:/movie/list";
  }

  @GetMapping({"/read", "/modify"})
  public void get(Long mno, PageRequestDTO pageRequestDTO, Model model) {
    model.addAttribute("movieDTO", movieService.get(mno));
  }

  @PostMapping("/modify")
  public String modify(MovieDTO movieDTO,
                       PageRequestDTO pageRequestDTO, RedirectAttributes ra) {
    movieService.modify(movieDTO);
    ra.addFlashAttribute("msg", movieDTO.getMno() + "번 게시물이 수정");
    ra.addAttribute("bno", movieDTO.getMno());
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/movie/list";
  }
}



