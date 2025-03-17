package com.example.ex6.controller;

import com.example.ex6.dto.ReviewDTO;
import com.example.ex6.repository.ReviewRepository;
import com.example.ex6.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {
  private final ReviewService reviewService;

  @GetMapping(value = "/{mno}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ReviewDTO>> getListByMovie(@PathVariable("mno") Long mno) {
    return new ResponseEntity<>(reviewService.getList(mno), HttpStatus.OK);
  }
  
  @PostMapping(value = {"", "/"})
  public ResponseEntity<String> register(@RequestBody ReviewDTO reviewDTO) {
    return new ResponseEntity<>(reviewService.register(reviewDTO)+"번 리뷰 등록", HttpStatus.OK);
  }

  @PutMapping(value = {"", "/"})
  public ResponseEntity<String> modify(@RequestBody ReviewDTO reviewDTO) {
    reviewService.modify(reviewDTO);
    return new ResponseEntity<>(reviewDTO.getReviewnum()+ "번 리뷰 수정", HttpStatus.OK);
  }

  @DeleteMapping(value = "/{reviewnum}")
  public ResponseEntity<String> delete(@PathVariable Long reviewnum) {
    reviewService.remove(reviewnum);
    return new ResponseEntity<>(reviewnum + "번 리뷰 삭제", HttpStatus.OK);
  }
}
