package com.example.apiserver.repository;

import com.example.apiserver.entity.Photos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotosRepository extends JpaRepository<Photos, Long> {
  @Modifying
  @Query("delete from Photos p where p.uuid=:uuid ")
  void deleteByUuid(@Param("uuid") String uuid);

  @Query("select p from Photos p where p.journal.jno=:jno")
  List<Photos> findByJno(@Param("jno") Long jno);

  @Modifying
  @Query("delete from Photos p where p.journal.jno=:jno")
  void deleteByJno(@Param("jno") long jno);
}
