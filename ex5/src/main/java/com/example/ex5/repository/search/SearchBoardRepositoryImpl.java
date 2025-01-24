package com.example.ex5.repository.search;

import com.example.ex5.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
// 개발자가 원하는 대로 동작하는 Repository를 작성하는데 가장 중요한 QuerydslRepositorySupport
// QuerydslRepositorySupport는 Spring Data JPA에 포함된 클래스로 QueryDSL 라이브러리를
// 이용해서 직접 무언가를 구현할 때 사용
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport
    implements SearchBoardRepository {

  // QuerydslRepositorySupport는 반드시 super를 호출하여 매개변수로 Board.class 추가.
  public SearchBoardRepositoryImpl() {
    super(Board.class);
  }

  @Override
  public Board search1() {
    log.info("search1........................");
    // 여러 객체를 가져오기 위함.
    QBoard board = QBoard.board;
    QReply reply = QReply.reply;
    QMember member = QMember.member;

    JPQLQuery<Board> jpqlQuery = from(board);
    jpqlQuery.leftJoin(member).on(board.writer.eq(member));
    jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

    // 정해진 엔티티 객체 단위가 아니라 여러 객체를 담기 위하여 JPQLQuery에서는 Tuple이라는 객체를 이용.
    JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
    tuple.groupBy(board);

    log.info("================================================================");
    log.info(tuple);
    log.info("================================================================");

    // tuple.fetch() 결과 값을 받을 때 List<Tuple>로 받음
    List<Tuple> result = tuple.fetch();
    log.info("result: " + result);
    return null;
  }

  // JPQLQuery 로 Page<Object[]> 처리
  @Override
  public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
    log.info("searchPage...............");

    // 1) 도메인을 확보
    QBoard board = QBoard.board;
    QReply reply = QReply.reply;
    QMember member = QMember.member;

    // 2) Q도메인을 조인
    JPQLQuery<Board> jpqlQuery = from(board);
    jpqlQuery.leftJoin(member).on(board.writer.eq(member));
    jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

    // 3) Tuple 생성:조인한 객체와 select()를 활용해서 필요한 데이터를 tuple로 생성
    JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

    // 4) 조건절 생성을 위한 객체 생성
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    BooleanExpression expression = board.bno.gt(0L);
    booleanBuilder.and(expression);

    // 5) 검색조건 추가
    if (type != null) {
      String[] arr = type.split("");
      BooleanBuilder condition = new BooleanBuilder();
      for (String t : arr) {
        switch (t) {
          case "t":
            condition.or(board.title.contains(keyword));
            break;
          case "c":
            condition.or(board.content.contains(keyword));
            break;
          case "w":
            condition.or(member.email.contains(keyword));
            break;
        }
      }
      booleanBuilder.and(condition);
    }

    // 6) 조인된 tuple에 추가된 조건절을 적용.
    tuple.where(booleanBuilder);

    // 7) 정렬 조건 추가
    Sort sort = pageable.getSort();
    sort.stream().forEach(new Consumer<Sort.Order>() {
      @Override
      public void accept(Sort.Order order) {
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
        String prop = order.getProperty();
        PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
        tuple.orderBy(new OrderSpecifier<Comparable>(direction, orderByExpression));
      }
    });

    // 8) 데이터 select를 위한 그룹 생성
    tuple.groupBy(board);

    // 9) tuple의 데이터를 가져오기 위한 시작위치 즉 offset을 지정
    tuple.offset(pageable.getOffset());

    // 10) offset으로 부터 들고올 개수 지정
    tuple.limit(pageable.getPageSize());

    // 11) 최종 결과를 tuple의 fetch()를 통해서 컬렉션으로 변환
    List<Tuple> result = tuple.fetch();

    // 12) 최종 결과의 개수 출력
    long count = tuple.fetchCount();
    log.info("COUNT: " + count);

    // 13) 출력하고자 하는 Page 객체를 위한 PageImpl객체로 생성
    return new PageImpl<Object[]>(result.stream().map(new Function<Tuple, Object[]>() {
      @Override
      public Object[] apply(Tuple tuple) {
        return tuple.toArray();
      }
    }).collect(Collectors.toList()), pageable, count);
  }
}
