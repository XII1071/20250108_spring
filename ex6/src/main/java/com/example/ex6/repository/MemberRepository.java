package com.example.ex6.repository;

import com.example.ex5.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,String> {

}
