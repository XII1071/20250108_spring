package com.example.ex6.controller;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
  public void register() {
  }

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

    // ✅ `mno`를 URL 파라미터로 넘겨주기
    ra.addAttribute("mno", movieDTO.getMno());
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/movie/read"; // ✅ read로 이동
  }

  @DeleteMapping("/delete/{mno}")
  public ResponseEntity<String> deleteMovie(@PathVariable("mno") Long mno) {
    log.info("🔴 영화 삭제 요청: " + mno);
    try {
      movieService.deleteMovie(mno);
      return ResponseEntity.ok("✅ 삭제 성공: " + mno);
    } catch (Exception e) {
      log.error("❌ 삭제 실패: ", e);
      return ResponseEntity.status(500).body("❌ 삭제 실패: " + e.getMessage());
    }
  }

}








