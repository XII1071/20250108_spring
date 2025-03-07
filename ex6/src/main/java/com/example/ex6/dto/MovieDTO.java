package com.example.ex6.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
  private Long mno;
  private String title;

  // 속성을 사용하거나 선언 시점에 또는 생성자에서 기본값으로 초기화
  @Builder.Default  //@AllArgsConstructor없으면 에러발생
  private List<MovieImageDTO> imageDTOList = new ArrayList<>();

  private double avg;
  private Long reviewCnt;
  private LocalDateTime regDate;
  private LocalDateTime modDate;

}
