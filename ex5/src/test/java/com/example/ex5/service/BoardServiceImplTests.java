package com.example.ex5.service;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceImplTests {
  @Autowired
  private BoardService boardService;

  @Test
  void register() {
    BoardDTO boardDTO = BoardDTO.builder()
        .title("new Title")
        .content("new Content")
        .writerEmail("user55@a.a")
        .build();
    Long bno = boardService.register(boardDTO);
    System.out.println(">> bno: " + bno);
  }

  @Test
  void getList() {
    PageRequestDTO pageRequestDTO = new PageRequestDTO();
    PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);
    for (BoardDTO boardDTO : result.getDtoList()) {
      System.out.println(boardDTO);
    }
  }

  @Test
  public void testGet() {
    Long bno = 1l;
    BoardDTO boardDTO = boardService.get(bno);
    System.out.println(boardDTO);
  }

  @Test
  public void testRemoveWithReplies() {
    Long bno = 95l;
    boardService.removeWithReplies(bno);
  }

  @Test
  public void testModify() {
    BoardDTO boardDTO = BoardDTO.builder()
        .bno(1l)
        .title("Change Title..")
        .content("Change Content..")
        .build();
    boardService.modify(boardDTO);
  }
}