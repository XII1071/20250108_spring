package com.example.ex6.controller;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.service.MovieService;
import com.example.ex6.service.ReviewService;
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
  private final ReviewService reviewService;

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
  public String modify(MovieDTO movieDTO, RedirectAttributes redirectAttributes) {
    log.info("Modify Movie: " + movieDTO);

    movieService.modify(movieDTO);

    redirectAttributes.addAttribute("mno", movieDTO.getMno());
    return "redirect:/movie/read";
  }


  @DeleteMapping("/delete/{mno}")
  public ResponseEntity<String> deleteMovie(@PathVariable("mno") Long mno) {
    try {
      log.info("🎬 영화 삭제 요청: mno = {}", mno);

      // 1️⃣ 관련된 리뷰 삭제
      reviewService.deleteReviewsByMovie(mno);
      log.info("✅ 관련된 리뷰 삭제 완료");

      // 2️⃣ 영화 삭제
      movieService.deleteMovie(mno);
      log.info("✅ 영화 삭제 완료: mno = {}", mno);

      return ResponseEntity.ok("영화 삭제 완료");
    } catch (Exception e) {
      log.error("❌ 영화 삭제 중 오류 발생: ", e);
      return ResponseEntity.internalServerError().body("영화 삭제 실패");
    }
  }

}
