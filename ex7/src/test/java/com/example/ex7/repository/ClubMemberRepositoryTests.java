package com.example.ex7.repository;

import com.example.ex7.entity.ClubMember;
import com.example.ex7.entity.ClubMemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClubMemberRepositoryTests {

  @Autowired
  ClubMemberRepository clubMemberRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void insertDummies() {
    IntStream.rangeClosed(1, 100).forEach(i->{
      ClubMember clubMember = ClubMember.builder()
          .email("u" + i + "@a.a")
          .name("사용자" + i)
          .fromSocial(false)
          .password(passwordEncoder.encode("1"))
          .build();
      clubMember.addMemberRole(ClubMemberRole.USER);
      if(i>80) clubMember.addMemberRole(ClubMemberRole.MANAGER);
      if(i>90) clubMember.addMemberRole(ClubMemberRole.ADMIN);
      clubMemberRepository.save(clubMember);
    });
  }

  @Test
  public void testRead() {
    Optional<ClubMember> result = clubMemberRepository.findByEmail("u95@a.a", false);
    System.out.println(result.get());
  }
}