package com.example.apiserver.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"journal", "members"})
public class Comments extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cno;

  @ManyToOne(fetch = FetchType.LAZY)
  private Journal journal;

  @ManyToOne(fetch = FetchType.LAZY)
  private Members members;

  private Long likes; //별점
  private String text; //한줄평
  public void changeLikes(Long likes) {this.likes = likes;}
  public void changeText(String text) {this.text = text;}
}