package com.example.ex5.repository;

import com.example.ex5.entity.Board;
import com.example.ex5.repository.search.SearchBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>
    , SearchBoardRepository {  //SearchBoardRepository는 복수개의 엔티티에 대한 동적 검색 위함.

  /*
  JPQL  :: JPA(Java Persistence API)에서 사용하는 객체지향 쿼리 언어
  select b, r from Board b left join Reply r on r.board = b where b.bno=100;

  SQL :: RDBMS에서 사용하는 구조화된 쿼리 언어
  select b.*, r.* from db7.board b, db7.reply r where b.bno = r.board_bno and b.bno =100;
  */

  // Board와 Member 관계는 직접적으로 연관 되고 있기 때문에 left join에서 on을 생략가능.
  @Query("select b, w from Board b left join b.writer w where b.bno=:bno")
  Object getBoardWithWriter(@Param("bno") Long bno);

  // Board와 Reply 관계는 직접적인 연관관계가 없기 때문에 left join에서 on을 반드시 표기.
  @Query("select b, r from Board b left join Reply r on r.board = b where b.bno=:bno ")
  List<Object[]> getBoardWithReply(@Param("bno") Long bno);

  // Board, Reply, Member 셋중에 주로 기준되어서 데이터를 가져오는 것은 Board이고
  // Board를 중심으로 조인관계를 작성한다. Page로 리턴할 경우 countQuery 필요
  @Query(value = "select b, w, count(r) from Board b " +
      "left join b.writer w " +
      "left join Reply    r on r.board = b group by b",
      countQuery = "select count(b) from Board b")
  Page<Object[]> getBoardWithReplyCount(Pageable pageable);

  @Query("select b, w, count(r) from Board b " +
      "left join b.writer w " +
      "left join Reply    r on r.board = b where b.bno = :bno")
  Object getBoardByBno(@Param("bno") Long bno);


}
