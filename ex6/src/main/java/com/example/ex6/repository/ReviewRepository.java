package com.example.ex6.repository;

import com.example.ex6.entity.Member;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  // EntityGraph.EntityGraphType.FETCH :: 즉각적으로 조인을 해서 가져오게 함.
  @EntityGraph(attributePaths = {"member"},
      type = EntityGraph.EntityGraphType.FETCH)
  List<Review> findByMovie(Movie movie);

  // 총 리뷰갯수만큼 삭제(반복적 삭제)가 되면서 m_member 테이블에서도 삭제가 됨.
  // void deleteByMember(Member member);

  // 위의 비효율적인 방법을 막기 위해서 @query을 사용하고, JPQL에서 삭제, 수정은 반드시 @Modifying 붙일것
  @Modifying
  @Query("delete from Review r where r.member = :member")
  void deleteByMember(Member member);

  @Modifying
  @Query("delete from Review r where r.movie.mno = :mno ")
  void deleteByMno(@Param("mno") Long mno);
}
