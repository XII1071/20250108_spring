package com.example.apiserver.repository;

import com.example.apiserver.entity.Members;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MembersRepository extends JpaRepository<Members, Long> {

  @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
  @Query("select m from Members m where m.fromSocial = :fromSocial and m.email = :email")
  Optional<Members> findByEmail(@Param("email") String email, @Param("fromSocial") boolean fromSocial);

  @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
  @Query("select m from Members m where m.email = :email")
  Optional<Members> findByEmail(@Param("email") String email);

}
