package com.example.ex5.repository.search;

import com.example.ex5.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Spring Data JPA의 여러 엔티티에 대하여 동적인 검색을
// 위한 Repository 확장하기 위한 단계
// 1) 쿼리메서드나 @Query 등으로 처리할 수 없는 기능을 인터페이스로 설계
// 2) 별도의 인터페이스에 대한 구현 클래스를 작성한다.
// 3) 구현클래스에서 Q도메인 클래스와 JPQLQuery를 이용해서 구현

public interface SearchBoardRepository {
  Board search1();

  // Board, Member, Reply에서 가져온 내용을 출력하기 위해서 Page<Object[]>로 처리
  Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
