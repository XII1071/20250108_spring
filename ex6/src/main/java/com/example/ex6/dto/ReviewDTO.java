package com.example.ex6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
  private Long reviewnum;
  private Long mno; // Movie
  private Long mid; // Member
  private String nickname;
  private String email;
  private int grade;
  private String text;
  private LocalDateTime regDate, modDate;
}
