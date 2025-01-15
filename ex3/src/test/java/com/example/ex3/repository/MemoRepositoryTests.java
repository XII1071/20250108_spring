package com.example.ex3.repository;


import com.example.ex3.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
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
        // insert에 해당되는 save
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
    // select에 해당되는 findById()
    Optional<Memo> result = memoRepository.findById(100L);
    if (result.isPresent()) System.out.println(result.get());
    else System.out.println("없습니다.");
  }

  @Test
  public void testDelete() {
    // delte 해당되는 deleteById()
    memoRepository.deleteById(100L);
  }

  @Test
  public void testFindByMnoBetweenOrderByMnoDesc() {
    List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(1l, 10l);
    for (Memo m : list) {
      System.out.println(m);
    }
  }

  @Test
  public void testFindByMemoTextContaining() {
    List<Memo> list = memoRepository.findByMemoTextContaining("7");
    for (Memo memo : list) {
      System.out.println(memo);
    }
  }
  @Test
  public void testFindByMemoTextNotContaining() {
    List<Memo> list = memoRepository.findByMemoTextNotContaining("7");
    for (Memo memo : list) {
      System.out.println(memo);
    }
  }

  @Test
  public void testFindByMnoBetween() {
    Pageable pageable = PageRequest.of(5, 10, Sort.by("mno").descending());
    Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
    result.get().forEach(new Consumer<Memo>() {
      @Override
      public void accept(Memo memo) {
        System.out.println(memo);
      }
    });
  }

  @Test
  public void getListDesc() {
    List<Memo> list = memoRepository.getMemoListDesc();
    for(Memo m : list) System.out.println(m);
  }

}