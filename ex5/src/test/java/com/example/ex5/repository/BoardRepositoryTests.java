package com.example.ex5.repository;

import com.example.ex5.entity.Board;
import com.example.ex5.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardRepositoryTests {

  @Autowired
  BoardRepository boardRepository;

  @Test
  public void insertBoards() {
    IntStream.rangeClosed(1, 100).forEach( i -> {
      Member member = Member.builder().email("user"+i+"@a.a").build();
      Board board = Board.builder()
          .title("Title.."+i)
          .content("Content.."+i)
          .writer(member)
          .build();
      boardRepository.save(board);
    });
  }

  // Transactional은 FetchType.LAZY를 보완하기 위한 조치
  // board를 열람한 후, 필요할 때 한번 더 여는 역할.
  @Transactional
  @Test
  public void testRead1() {
    Optional<Board> result = boardRepository.findById(100L);
    Board board = result.get();
    System.out.println(board);
    System.out.println(board.getWriter());
  }

  @Test
  public void testReadWithWriter() {
    Object result = boardRepository.getBoardWithWriter(100L);
    Object[] arr = (Object[]) result;
    System.out.println(Arrays.toString(arr));
  }

  @Test
  public void testGetBoardWithReply() {
    List<Object[]> result = boardRepository.getBoardWithReply(10L);
    for (Object[] arr : result) {
      System.out.println(Arrays.toString(arr));
    }
  }
}