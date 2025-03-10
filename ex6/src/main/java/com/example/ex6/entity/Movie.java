
package com.example.ex6.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity                /* 이 클래스가 JPA 엔티티임을 나타내며, 데이터베이스의 테이블과 매핑됩니다. */
@Builder               /* 빌더 패턴을 사용하여 Movie 객체를 유연하게 생성할 수 있게 해줍니다. */
@AllArgsConstructor    /* 모든 필드를 매개변수로 받는 생성자를 자동으로 생성합니다. */
@NoArgsConstructor     /* 인자가 없는 기본 생성자를 자동으로 생성합니다. */
@Getter                /* 모든 필드에 대한 getter 메서드를 자동으로 생성합니다. */
@ToString              /* 객체 정보를 문자열로 출력할 수 있는 toString() 메서드를 자동으로 생성합니다. */
public class Movie extends BaseEntity {
  @Id // @Id: mno 필드가 데이터베이스 테이블의 기본키임을 지정합니다.
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  // @GeneratedValue: 데이터베이스에 의해 기본키가 자동 생성됨 (IDENTITY 전략)
  private Long mno;  // mno: 영화(Movie)의 고유 번호(PK)를 나타내며, Long 타입을 사용하여 넓은 범위의 값을 지원

  private String title; //title: 영화의 제목을 저장하는 필드

  public void changeTitle(String title) { // changeTitle: 타이틀은 글자로 바꿔주는
    this.title = title; // 현재 Movie 객체의 title 필드를 의미하며, 전달받은 title 값으로 변경합니다.
  }
}
