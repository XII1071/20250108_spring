package com.example.ex6.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
  private Long reviewnum;
  private Long mno;
  private Long mid;
  private String nickname;
  private String email;
  private int grade;
  private String text;
  private LocalDateTime regDate, modDate;
}
