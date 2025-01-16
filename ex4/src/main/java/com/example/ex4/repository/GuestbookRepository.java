package com.example.ex4.repository;

import com.example.ex4.entity.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long>,
    QuerydslPredicateExecutor<Guestbook> {

}
