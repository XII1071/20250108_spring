package com.example.ex4.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
// 밑에 3개는 setter 생성자
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Guestbook extends BaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long gno;

  @Column(length = 100, nullable = false)
  private String title;

  @Column(length = 1500, nullable = false)
  private String content;

  @Column(length = 50, nullable = false)
  private String writer;

}