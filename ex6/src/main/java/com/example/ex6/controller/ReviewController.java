package com.example.ex6.controller;

import com.example.ex6.dto.ReviewDTO;
import com.example.ex6.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
