package com.example.ex6.repository;

import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {

  @Modifying
  @Query("delete from MovieImage mi where mi.uuid=:uuid ")
  void deleteByUuid(@Param("uuid") String uuid);

  // ✅ 특정 UUID의 이미지를 찾기 위한 쿼리
  Optional<MovieImage> findByUuid(String uuid);

  // ✅ 특정 영화에 연결된 모든 이미지 삭제
  @Transactional
  @Modifying
  @Query("DELETE FROM MovieImage mi WHERE mi.movie = :movie")
  void deleteByMovie(Movie movie);
}
