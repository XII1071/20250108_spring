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
// 스프링이 이 클래스를 "서비스 계층"의 컴포넌트로 인식하게 하는 어노테이션
@RequiredArgsConstructor
// Lombok을 사용해, 클래스의 모든 'final' 필드를 파라미터로 받는 생성자를 자동 생성
@Log4j2
// Lombok을 사용해, Log4j2 로깅 기능을 쉽게 사용할 수 있도록 해주는 어노테이션
public class BoardServiceImpl implements BoardService {

  // Board(게시판) 데이터 처리를 위한 레포지토리
  private final BoardRepository boardRepository;

  // Reply(댓글) 데이터 처리를 위한 레포지토리
  private final ReplyRepository replyRepository;

  /*
   * 게시글 등록 메서드
   * @param boardDTO : 등록하고자 하는 게시글 정보를 담은 DTO
   * @return 등록 후 생성된 게시글의 PK(식별자, bno)
   */
  @Override
  public Long register(BoardDTO boardDTO) {
    // 1) DTO를 엔티티로 변환
    //    DTO(화면이나 다른 계층에서 받은 데이터)를 JPA에서 사용하기 위해 엔티티로 바꿔야 합니다.
    Board board = dtoToEntity(boardDTO);

    // 2) 엔티티를 DB에 저장
    //    JPA의 save() 메서드는 엔티티를 전달하면, DB 테이블에 insert 쿼리를 실행해줍니다.
    boardRepository.save(board);

    // 3) 등록된 엔티티의 bno(Primary Key) 값을 반환
    //    save가 완료되면, JPA가 board 엔티티에 PK 값을 설정해줍니다.
    return board.getBno();
  }



  @Override
  public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
    // 1. 레포지토리 메서드 호출
    // boardRepository.getBoardWithReplyCount(...)가 무엇을 반환하느냐에 따라
    // 'Page<Object[]>' 형태로 결과를 받습니다.
    // 즉, 조회 시 Board 엔티티, Member 엔티티, 그리고 댓글 수(Long)와 같은
    // 여러 데이터(컬럼)를 함께 얻기 위해 하나의 'Object[]'에 담아 리턴하는 방식입니다.
    Page<Object[]> result = boardRepository.getBoardWithReplyCount(
            pageRequestDTO.getPageable(Sort.by("bno").descending())
    );
    // -> pageable(...) 메서드로 PageRequestDTO에 들어있는 페이지/사이즈 정보를 꺼내고,
    //    정렬 기준으로 "bno"를 내림차순(descending) 정렬하도록 설정했습니다.

    // 2. 조회된 'Page<Object[]>'를 우리가 원하는 'Page<BoardDTO>' 형태로 바꿔주는 역할
    // Function<Object[], BoardDTO> 인터페이스를 익명 클래스(anonymous class)로 구현
    // Object[] -> BoardDTO 로 변환하는 로직을 담는 함수(Function).
    Function<Object[], BoardDTO> fn = new Function<Object[], BoardDTO>() {
      @Override
      public BoardDTO apply(Object[] arr) {
        // arr[0] : Board 엔티티
        // arr[1] : Member 엔티티 (게시물 작성자 정보)
        // arr[2] : 게시물에 달린 댓글 개수(Long)
        // 이들을 이용해 'BoardDTO' 객체를 만들어 반환해야 합니다.
        return entityToDto((Board) arr[0], (Member) arr[1], (Long) arr[2]);
      }
    };

    // 3. PageResultDTO에 'Page<Object[]>' 와 Function<Object[], BoardDTO>를 전달
    //    PageResultDTO는 내부적으로 result의 각 원소(Object[])를
    //    fn 함수로 처리하여 새로운 DTO 리스트를 만들고,
    //    페이징 관련 정보까지 함께 관리합니다.
    return new PageResultDTO<>(result, fn);
  }

  @Override
  public BoardDTO get(Long bno) {
    // 1) 레포지토리 메서드를 호출하여, 특정 bno에 해당하는 데이터(게시글, 작성자, 댓글 수 등)를 조회합니다.
    //    getBoardByBno(bno)는 DB에서 Board, Member, 댓글 수(Long) 등을 조인해서
    //    한 번에 가져오는 커스텀 쿼리 메서드라고 가정합니다.
    //    반환 타입을 Object로 받아서, 실제로는 한 행(row)에 해당하는 여러 칼럼이나 엔티티 정보가
    //    Object[] 배열 형태로 리턴되는 상황입니다.
    Object result = boardRepository.getBoardByBno(bno);

    // 2) 반환된 result(Object)를 Object[]로 캐스팅합니다.
    //    배열의 인덱스를 통해 각각 Board 엔티티, Member 엔티티, 댓글 수(Long)를 꺼낼 예정이기 때문입니다.
    Object[] arr = (Object[]) result;

    // 3) entityToDto 메서드를 이용해 Object[] 배열에 들어있는 엔티티와 정보를
    //    BoardDTO로 변환합니다.
    //    arr[0] : Board 엔티티
    //    arr[1] : Member 엔티티 (작성자)
    //    arr[2] : 댓글 개수(Long)
    //    이런 순서대로 캐스팅을 하고, 이를 DTO로 만드는 로직이 entityToDto(...) 내부에 있습니다.
    return entityToDto((Board) arr[0], (Member) arr[1], (Long) arr[2]);
  }


  @Override
  public void modify(BoardDTO boardDTO) {
    // 1) DB에서 수정 대상이 될 Board(게시글) 엔티티를 찾아옵니다.
    //    findById(식별자값)은 Optional<T>를 반환하므로, 데이터를 찾지 못하면 빈 Optional이 돌아올 수 있습니다.
    Optional<Board> result = boardRepository.findById(boardDTO.getBno());

    // 2) 해당 게시글이 DB에 실제로 존재하는지 체크합니다.
    if (result.isPresent()) {
      // 3) Optional 에서 Board 엔티티를 꺼내옵니다.
      Board board = result.get();

      // 4) 엔티티(게시글)의 제목을 수정합니다.
      //    Board 엔티티 내부에 정의된 changeTitle() 메서드를 통해 제목을 변경하도록 구현해 놓았습니다.
      board.changeTitle(boardDTO.getTitle());

      // 5) 엔티티(게시글)의 내용을 수정합니다.
      board.changeContent(boardDTO.getContent());

      // 6) 수정된 엔티티를 다시 저장합니다.
      //    JPA에서 영속성 컨텍스트(변경 감지)에 의해 자동으로 변경이 반영되기도 하지만,
      //    save()를 호출하면 더욱 명시적으로 DB에 반영된다고 볼 수 있습니다.
      boardRepository.save(board);
    }
    // 7) if 조건에 해당하지 않으면(=게시글을 못 찾은 경우), 별도의 처리 없이 메서드를 종료합니다.
  }

  @Transactional  // 트랜잭션 처리
  @Override
  public void removeWithReplies(Long bno) {
    // 1) 댓글(repository)에서 해당 bno(게시글 번호)에 달려 있는 모든 댓글을 먼저 삭제합니다.
    replyRepository.deleteByBno(bno);

    // 2) 댓글이 모두 삭제된 뒤, 게시글 자체(board)도 삭제합니다.
    boardRepository.deleteById(bno);
  }

}
