package com.example.ex5.service;

import com.example.ex5.dto.ReplyDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.entity.Reply;

import java.util.List;

public interface ReplyService {
  Long register(ReplyDTO replyDTO);

  List<ReplyDTO> getList(Long bno);

  void modify(ReplyDTO replyDTO);

  void remove(Long rno);

  default Reply dtoToEntity(ReplyDTO replyDTO) {
    Reply reply = Reply.builder()
        .rno(replyDTO.getRno())
        .text(replyDTO.getText())
        .replyer(replyDTO.getReplyer())
        .board(Board.builder().bno(replyDTO.getBno()).build())
        .build();
    return reply;
  }

  // Board 객체를 게시할 이유가 없어서 bno는 제외
  default ReplyDTO entityToDto(Reply reply) {
    ReplyDTO replyDTO = ReplyDTO.builder()
        .rno(reply.getRno())
        .text(reply.getText())
        .replyer(reply.getReplyer())
        .regDate(reply.getRegDate())
        .modDate(reply.getModDate())
        .build();
    return replyDTO;
  }
}
