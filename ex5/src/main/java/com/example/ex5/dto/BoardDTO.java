package com.example.ex5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//@Data=@ToString+@Getter+@Setter+@RequiredArgsConstructor+@EqualsAndHashCode
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
  private Long bno;
  private String title;
  private String content;
  private String writerEmail;
  private String writerName;
  private LocalDateTime regDate;
  private LocalDateTime modDate;
  private int replyCount;
}
