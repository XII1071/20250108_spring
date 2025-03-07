package com.example.ex6.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"movie", "member"})
public class Review extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewnum;

  @ManyToOne(fetch = FetchType.LAZY)
  private Movie movie;

  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  private int grade; //별점
  private String text; //한줄평
  public void changeGrade(int grade) {this.grade = grade;}
  public void changeText(String text) {this.text = text;}
}
