package com.example.apiserver.repository;

import com.example.apiserver.entity.Comments;
import com.example.apiserver.entity.Journal;
import com.example.apiserver.entity.Members;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

  @EntityGraph(attributePaths = {"members"}, type = EntityGraph.EntityGraphType.FETCH)
  List<Comments> findByJournal(Journal journal);

  @Modifying
  @Query("delete from Comments c where c.members = :members")
  void deleteByMembers(Members members);

  @Modifying
  @Query("delete from Comments c where c.journal.jno = :jno ")
  void deleteByJno(@Param("jno") Long jno);

}
