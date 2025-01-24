package com.example.ex5.repository;

import com.example.ex5.entity.Board;
import com.example.ex5.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
class ReplyRepositoryTests {
  @Autowired
  ReplyRepository replyRepository;

  @Test
  public void insertReply() {
    IntStream.rangeClosed(1, 300).forEach(i -> {
      long bno = (long)(Math.random()*100)+1;
      Board board = Board.builder().bno(bno).build();
      Reply reply = Reply.builder()
          .text("Reply.." + i)
          .board(board)
          .replyer("guest")
          .build();
      replyRepository.save(reply);
    });
  }

  @Test
  public void testListByBoard() {
    List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(
        Board.builder().bno(97L).build()
    );
    replyList.forEach(reply -> System.out.println(reply));
  }
}