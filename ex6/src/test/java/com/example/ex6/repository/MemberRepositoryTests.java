package com.example.ex6.repository;

import com.example.ex6.entity.Member;
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
    IntStream.rangeClosed(1, 100).forEach(i->{
      Member member = Member.builder()
          .email("r"+i+"@r.r")
          .pw("1")
          .nickname("reviewer" + i)
          .build();
      memberRepository.save(member);
    });
  }

}