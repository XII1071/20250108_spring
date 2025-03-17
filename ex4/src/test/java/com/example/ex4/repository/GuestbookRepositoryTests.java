package com.example.ex4.repository;

import com.example.ex4.entity.Guestbook;
import com.example.ex4.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestbookRepositoryTests {

  @Autowired
  private GuestbookRepository guestbookRepository;

  @Test
  public void insertDummies() {
    IntStream.rangeClosed(1, 300).forEach(new IntConsumer() {
      @Override
      public void accept(int i) {
        Guestbook guestbook = Guestbook.builder()
            .title("Title..."+i)
            .content("Content..."+i)
            .writer("user"+(i%10))
            .build();
        System.out.println(guestbookRepository.save(guestbook));
      }
    });
  }

  @Test
  public void updateTest() {
    Optional<Guestbook> result = guestbookRepository.findById(300L);
    if (result.isPresent()) {
      Guestbook guestbook = result.get();
      guestbook.changeTitle("Changed Title...");
      guestbook.changeContent("Changed Content");
      guestbookRepository.save(guestbook);
    }
  }

  @Test
  // QueryDsl 테스트
  public void testQuery1() {
    // Pageable :: 페이지 정보를 가진 객체
    Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

    // 동적 질의를 위한 Qdomain 생성.
    QGuestbook qGuestbook = QGuestbook.guestbook;

    // 검색 변수
    String keyword = "1";

    //검색을 실행한 객체
    BooleanBuilder builder = new BooleanBuilder();

    //검색 조건 처리를 위한 객체
    BooleanExpression expression = qGuestbook.title.contains(keyword);

    // 검색 조건을 검색을 실행할 객체가 적용
    builder.and(expression);

    // 검색 조건의 결과를 Page 객체에 담음.
    Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
    result.stream().forEach(new Consumer<Guestbook>() {
      @Override
      public void accept(Guestbook guestbook) {
        System.out.println(guestbook);
      }
    });
  }

  @Test
  public void testQuery2() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
    QGuestbook qGuestbook = QGuestbook.guestbook;
    String keyword = "1";
    BooleanBuilder builder = new BooleanBuilder();
    BooleanExpression exTitle = qGuestbook.title.contains(keyword);
    BooleanExpression exContent = qGuestbook.content.contains(keyword);
    BooleanExpression exAll = exTitle.or(exContent);
    builder.and(exAll);
    builder.and(qGuestbook.gno.gt(0L)); //형식적이지만 추가해서 조건을 온전하게 함.
    Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
    result.stream().forEach(guestbook -> System.out.println(guestbook));
  }
}