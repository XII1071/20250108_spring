package com.example.apiserver.repository;

import com.example.apiserver.entity.Journal;
import com.example.apiserver.repository.search.SearchJournalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JournalRepository extends JpaRepository<Journal, Long>, SearchJournalRepository {
  @Query("select j, sum(coalesce(c.likes,0)), count(distinct c) " +
      "from Journal j left outer join Comments c " +
      "on c.journal = j where j.members.mid = :mid group by j ")
  Page<Object[]> getListPage(Pageable pageable, @Param("mid") Long mid);

  @Query("select j, p, sum(coalesce(c.likes,0)), count(distinct c) " +
      "from Journal j " +
      "left outer join Photos p   on p.journal = j " +
      "left outer join Comments c on c.journal = j where j.members.mid = :mid group by j ")
  Page<Object[]> getListPagePhotos(Pageable pageable, @Param("mid") Long mid);

  @Query(value = "select j.jno, p.pno, p.photos_name, " +
      "sum(coalesce(c.likes, 0)), count(c.cno) " +
      "from db7.photos p left outer join db7.journal j on j.jno=p.journal_jno " +
      "left outer join db7.comments c on j.jno=c.journal_jno " +
      "where p.pno = " +
      "(select max(pno) from db7.photos p2 where p2.journal_jno=j.jno) " +
      "and j.members_mid = :mid " +
      "group by j.jno ", nativeQuery = true)
  Page<Object[]> getListPagePhotosNative(Pageable pageable, @Param("mid") Long mid);

  @Query("select j, p, sum(coalesce(c.likes, 0)), count(distinct c) from Journal j " +
      "left outer join Photos p   on p.journal = j " +
      "left outer join Comments c on c.journal = j " +
      "where pno = (select max(p2.pno) from Photos p2 where p2.journal=j) " +
      "and j.members.mid = :mid " +
      "group by j ")
  Page<Object[]> getListPagePhotosJPQL(Pageable pageable, @Param("mid") Long mid);

  @Query("select journal, max(p.pno) from Photos p group by journal")
  Page<Object[]> getMaxQuery(Pageable pageable);

  @Query("select j, p, sum(coalesce(c.likes, 0)), count(c) " +
      "from Journal j left outer join Photos p on p.journal=j " +
      "left outer join Comments c on c.journal = j " +
      "where j.jno = :jno group by p ")
  List<Object[]> getJournalWithAll(@Param("jno") Long jno);
}
