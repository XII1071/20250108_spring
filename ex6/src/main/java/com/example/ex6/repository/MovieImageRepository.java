package com.example.ex6.repository;

import com.example.ex6.entity.MovieImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {

  // ✅ 특정 영화(mno)의 모든 이미지 삭제
  @Transactional
  @Modifying
  @Query("DELETE FROM MovieImage mi WHERE mi.movie.mno = :mno")
  void deleteByMovieMno(@Param("mno") Long mno);
}

