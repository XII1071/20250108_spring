package com.example.apiserver.repository;

import com.example.apiserver.entity.Members;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MembersRepositoryTests {

  @Autowired
  MembersRepository membersRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void insertMembers() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      Members members = Members.builder()
          .email("m" + i + "@m.m")
          .password(passwordEncoder.encode("1"))
          .name(generateName())
          .nickname("writer" + i)
          .mobile("010-1111-1" + return3Digit(i))
          .build();
      membersRepository.save(members);
    });
  }

  private String return3Digit(int i) {
    return (i < 10) ? "00" + i : (i < 100) ? "0" + i : "" + i;
  }

  private String generateName() {
    String[] arr1 = new String[]{"김", "이", "박", "강", "최"};
    String[] arr2 = new String[]{"일", "이", "삼", "사", "오"};
    String[] arr3 = new String[]{"진", "은", "영", "호", "원"};
    return arr1[(int) (Math.random() * arr1.length)]
        + arr2[(int) (Math.random() * arr1.length)]
        + arr3[(int) (Math.random() * arr1.length)];
  }

  @Test
  public void testFindByEmail() {
    Optional<Members> result = membersRepository.findByEmail("m1@m.m", false);
    if (result.isPresent()) {
      System.out.println(result.get());
    }
  }
}



