package com.example.ex5.service;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.dto.PageResultDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.entity.Member;

public interface BoardService {
  public default Board dtoToEntity(BoardDTO boardDTO) {
    Board board = Board.builder()
        .bno(boardDTO.getBno())
        .title(boardDTO.getTitle())
        .content(boardDTO.getContent())
        .writer(Member.builder().email(boardDTO.getWriterEmail()).build())
        .build();
    return board;
  }

  public default BoardDTO entityToDto(Board board, Member member, Long replyCount) {
    BoardDTO boardDTO = BoardDTO.builder()
        .bno(board.getBno())
        .title(board.getTitle())
        .content(board.getContent())
        .writerEmail(member.getEmail())
        .writerName(member.getName())
        .regDate(board.getRegDate())
        .modDate(board.getModDate())
        .replyCount(replyCount.intValue())
        .build();
    return boardDTO;
  }

  //
  Long register(BoardDTO boardDTO);
  PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
  BoardDTO get(Long bno);
  void modify(BoardDTO boardDTO);
  void removeWithReplies(Long bno);
}
