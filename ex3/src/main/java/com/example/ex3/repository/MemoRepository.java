package com.example.ex3.repository;

import com.example.ex3.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
  // Query Method : 메서드 이름 자체가 질의문.
  List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

  // 값이 일치할 때
  // List<Memo> findByMno(Long mno);
  // List<Memo> findByMnoNotLike(Long mno);

  // 값을 포함 할 때
  List<Memo> findByMemoTextContaining(String search);
  List<Memo> findByMemoTextNotContaining(String search);

  Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

  //void deleteMemoByMnoLessThan(Long num);

  // query annotation : 직접 쿼리를 작성.
  @Query("select m from Memo m order by m.mno desc")
  List<Memo> getMemoListDesc();


}
