package com.example.apiserver.repository.search;

import com.example.apiserver.entity.Journal;
import com.example.apiserver.entity.QComments;
import com.example.apiserver.entity.QJournal;
import com.example.apiserver.entity.QPhotos;
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
import java.util.stream.Collectors;

@Log4j2
public class SearchJournalRepositoryImpl extends QuerydslRepositorySupport
    implements SearchJournalRepository {
  public SearchJournalRepositoryImpl() {
    super(Journal.class);
  }

  @Override
  public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
    //1) q도메인을 확보
    QJournal journal = QJournal.journal;
    QPhotos photos = QPhotos.photos;
    QComments comments = QComments.comments;

    //2) q도메인을 조인
    JPQLQuery<Journal> jpqlQuery = from(journal);
    jpqlQuery.leftJoin(photos).on(photos.journal.eq(journal));
    jpqlQuery.leftJoin(comments).on(comments.journal.eq(journal));

    //3) Tuple생성 : 조인을 한 결과의 데이터를 tuple로 생성
    JPQLQuery<Tuple> tuple =
        jpqlQuery.select(journal, photos, comments.likes.sum().coalesce(0), comments.count());

    //4) 조건절 생성
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    BooleanExpression expression = journal.jno.gt(0L);
    booleanBuilder.and(expression);

    //5) 검색조건 파악
    if (type != null) {
      String[] typeArr = type.split("");
      BooleanBuilder conditionBuilder = new BooleanBuilder();
      for (String t : typeArr) {
        switch (t) {
          case "t":
            conditionBuilder.or(journal.title.contains(keyword)); break;
          case "c":
            conditionBuilder.or(journal.content.contains(keyword)); break;
          case "w":
            conditionBuilder.or(journal.members.email.contains(keyword)); break;
        }
      }
      booleanBuilder.and(conditionBuilder);
    }
    //6) 조인된 tuple에서 조건절에 위에서 생성한 booleanBuilder로 검색한다.
    tuple.where(booleanBuilder);

    //7) 정렬조건 생성
    Sort sort = pageable.getSort();
    sort.stream().forEach(order -> {
      Order direction = order.isAscending() ? Order.ASC : Order.DESC;
      String prop = order.getProperty();
      PathBuilder orderByExpression = new PathBuilder(Journal.class, "journal");
      tuple.orderBy(new OrderSpecifier<Comparable>(direction, orderByExpression.get(prop)));
    });

    //8) 데이터를 출력하기 위한 그룹을 생성
    tuple.groupBy(journal);

    //9) 원하는 데이터를 들고오기 위해서 시작위치즉 offset을 지정
    tuple.offset(pageable.getOffset());

    //10) offset부터 들고올 갯수 지정
    tuple.limit(pageable.getPageSize());

    //11) 최종 결과를 tuple의 fetch()함수를 통해서 컬렉션에 저장
    List<Tuple> result = tuple.fetch();
    log.info(result);

    //12) 최종결과의 갯수 출력
    long count = tuple.fetchCount();
    log.info("COUNT: " + count);

    //13) 출력하고자 하는 Page객체를 위한 PageImpl객체로 생성
    return new PageImpl<Object[]>(result.stream().map(
        t -> t.toArray()).collect(Collectors.toList()), pageable, count);

  }

}
