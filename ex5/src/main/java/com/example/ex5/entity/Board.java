package com.example.ex5.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer")
public class Board extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bno;

  private String title;
  private String content;

  // has-a 관계를 이용하여 Foreign Key 적용
  // 지연 로딩을 기본 사용하고 상황에 맞게 필요할 때 재호출 함.
  @ManyToOne (fetch = FetchType.LAZY)
  private Member writer;

  public void changeTitle(String title) {this.title = title;}
  public void changeContent(String content) {this.content = content;}
}
