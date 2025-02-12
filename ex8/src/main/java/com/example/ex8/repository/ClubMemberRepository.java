package com.example.ex8.repository;

import com.example.ex8.entity.ClubMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {

  // attributePaths에 정의된 속성은 eager로 패치하고, 나머지는 lazy 패치
  @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
  @Query("select m from ClubMember m where m.fromSocial = :fromSocial and m.email = :email ")
  Optional<ClubMember> findByEmail(
      @Param("email") String email, @Param("fromSocial") boolean fromSocial);
}
