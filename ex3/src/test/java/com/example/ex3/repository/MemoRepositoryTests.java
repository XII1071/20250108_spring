package com.example.ex3.repository;



import com.example.ex3.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemoRepositoryTests {

  @Autowired
  MemoRepository memoRepository;

  @Test
  public void testClass() {
    System.out.println("memoRepository: " + memoRepository.getClass().getName());
  }

  @Test
  public void insertDummies() {
    IntStream.rangeClosed(1, 100).forEach(new IntConsumer() {
      @Override
      public void accept(int value) {
//        Memo memo = Memo.builder()
//            .memoText("Simple memo... " + value)
//            .build();
        Memo memo = new Memo();
        memo.setMemoText("Simple memo... " + value);
        // insert 에 해당되는 save
        memoRepository.save(memo);
      }
    });
  }
  @Test
  public void testUpdate() {
    Memo memo = new Memo();
    memo.setMno(100L); memo.setMemoText("update 100");
    // update에 해당하는 save
    memoRepository.save(memo);

    // 객체의 속성이 많을 경우 먼저 찾고 난 뒤에 변경해야할 컬럼만 수정.
    Optional<Memo> result = memoRepository.findById(100L);
    if (result.isPresent()) {
      Memo m = result.get();
      m.setMemoText("update twice 100");
      memoRepository.save(m);
      System.out.println(m);
    }
  }
@Test
public void testFind() {
// select에 해당되는 findId()
  Optional<Memo> result = memoRepository.findById(100L);
  if (result.isPresent()) System.out.println(result.get());
    else System.out.println("없습니다");
  }

  @Test
  public void testDelete() {
// delete 해당되는 deleteId()
    memoRepository.deleteById(100L);
  }
}


