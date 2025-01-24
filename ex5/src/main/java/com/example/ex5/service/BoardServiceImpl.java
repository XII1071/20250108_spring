package com.example.ex5.service;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.dto.PageResultDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.entity.Member;
import com.example.ex5.repository.BoardRepository;
import com.example.ex5.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {
  private final BoardRepository boardRepository;
  private final ReplyRepository replyRepository;

  @Override
  public Long register(BoardDTO boardDTO) {
    Board board = dtoToEntity(boardDTO);
    boardRepository.save(board);
    return board.getBno();
  }

  @Override
  public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

    log.info(">>"+pageRequestDTO);
    // Querydsl(동적검색)이 없는 페이징 처리
    // Page<Object[]> result = boardRepository.getBoardWithReplyCount(
    //     pageRequestDTO.getPageable(Sort.by("bno").descending())
    // );

    // Querydsl(동적검색)이 적용된 페이징 처리
    Page<Object[]> result = boardRepository.searchPage(
        pageRequestDTO.getType(),
        pageRequestDTO.getKeyword(),
        pageRequestDTO.getPageable(Sort.by("bno").descending())
    );

    Function<Object[], BoardDTO> fn = new Function<Object[], BoardDTO>() {
      @Override
      public BoardDTO apply(Object[] arr) {
        return entityToDto((Board) arr[0], (Member) arr[1], (Long) arr[2]);
      }
    };
    return new PageResultDTO<>(result, fn);
  }

  @Override
  public BoardDTO get(Long bno) {
    Object result = boardRepository.getBoardByBno(bno);
    Object[] arr = (Object[]) result;
    return entityToDto((Board) arr[0], (Member) arr[1], (Long) arr[2]);
  }

  @Override
  public void modify(BoardDTO boardDTO) {
    Optional<Board> result = boardRepository.findById(boardDTO.getBno());
    if (result.isPresent()) {
      Board board = result.get();
      board.changeTitle(boardDTO.getTitle());
      board.changeContent(boardDTO.getContent());
      boardRepository.save(board);
    }

  }

  @Transactional  // 트랜잭션 처리한다는 표시
  @Override
  public void removeWithReplies(Long bno) {
    replyRepository.deleteByBno(bno);
    boardRepository.deleteById(bno);
  }
}
