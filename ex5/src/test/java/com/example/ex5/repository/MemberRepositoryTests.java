package com.example.ex5.repository;

import com.example.ex5.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTests {
  @Autowired
  MemberRepository memberRepository;

  @Test
  public void insertMembers() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      Member member = Member.builder()
          .email("user"+i+"@a.a")
          .password("1")
          .name("USER" + i)
          .build();
      memberRepository.save(member);
    });
  }
}