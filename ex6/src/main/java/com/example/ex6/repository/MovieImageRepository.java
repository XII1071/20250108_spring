package com.example.ex6.repository;

import com.example.ex6.entity.MovieImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {

  @Modifying
  @Query("delete from MovieImage mi where mi.uuid=:uuid ")
  void deleteByUuid(@Param("uuid") String uuid);
}
