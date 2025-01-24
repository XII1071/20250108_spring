package com.example.ex5.service;

import com.example.ex5.dto.ReplyDTO;
import com.example.ex5.repository.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyServiceTests {

  @Autowired
  ReplyService replyService;

  @Test
  public void testGetList() {
    Long bno = 10L;
    List<ReplyDTO> replyDTOList = replyService.getList(bno);
    replyDTOList.forEach(dto -> System.out.println(dto));
  }

}