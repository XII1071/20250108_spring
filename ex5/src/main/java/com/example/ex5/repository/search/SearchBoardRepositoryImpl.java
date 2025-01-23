package com.example.ex5.repository.search;

import com.example.ex5.entity.Board;
import com.example.ex5.entity.QBoard;
import com.example.ex5.entity.QMember;
import com.example.ex5.entity.QReply;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
// 개발자가 원하는 대로 동작하는 Repository를 작성하는데 가장 즁요한 QuerydslRepositorySupport
/* QuerydslRepositorySupport는 Spring Data JPA에 포함된 클래스로
  QueryDSL 라이브러리를 이용해서 직접 무언가를 구현할 때 사용 */
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport
      implements SearchBoardRepository {

  // QuerydslRepositorySupport 반드시 super를 호출하여 매개변수로 Board.class 추가.
  public SearchBoardRepositoryImpl() {
    super(Board.class);
  }

  @Override
  public Board search1() {
    log.info("Search1......................");
    // 여러 객체를 가져오기 위함.
    QBoard board = QBoard.board;
    QReply reply = QReply.reply;
    QMember member = QMember.member;

    JPQLQuery<Board> jpqlQuery = from(board);
    jpqlQuery.leftJoin(member).on(board.writer.eq(member));
    jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

    // 여러 객체를 담기 위하여 Tuple이라는 객체를 이용.
    JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
    tuple.groupBy(board);

    log.info("=================================================================");
    log.info(tuple);
    log.info("=================================================================");

    List<Tuple> result = tuple.fetch();
    log.info("result" +result);
    return null;
  }

  @Override
  public Page<Object[]> searchpage(String type, String keyword, Pageable pageable) {
    return null;
  }
}
