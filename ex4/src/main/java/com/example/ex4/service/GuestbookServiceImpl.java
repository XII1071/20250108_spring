package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;
import com.example.ex4.entity.QGuestbook;
import com.example.ex4.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {
  private final GuestbookRepository guestbookRepository;

  @Override
  public Long register(GuestbookDTO guestbookDTO) {
    log.info("guestbookDTO: "+guestbookDTO);
    Guestbook guestbook = dtoToEntity(guestbookDTO);
    guestbookRepository.save(guestbook);
    return guestbook.getGno();
  }

  @Override
  public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO) {
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("gno").descending());

    // 검색 조건 처리
    BooleanBuilder booleanBuilder = getSearch(pageRequestDTO);
    // queryDSL 지정
    Page<Guestbook> page = guestbookRepository.findAll(booleanBuilder, pageable);

    Function<Guestbook, GuestbookDTO> fn = new Function<Guestbook, GuestbookDTO>() {
      @Override
      public GuestbookDTO apply(Guestbook guestbook) {
        return entityToDto(guestbook);
      }
    };
    return new PageResultDTO<>(page, fn);
  }

  @Override
  public GuestbookDTO read(Long gno) {
    Optional<Guestbook> result = guestbookRepository.findById(gno);
    return entityToDto(result.get());
  }

  @Override
  public Long modify(GuestbookDTO guestbookDTO) {
    Long result = 0L;
    Optional<Guestbook> find = guestbookRepository.findById(guestbookDTO.getGno());
    if (find.isPresent()) {
      Guestbook guestbook = find.get();
      guestbook.changeTitle(guestbookDTO.getTitle());
      guestbook.changeContent(guestbookDTO.getContent());
      guestbookRepository.save(guestbook);
      result = guestbook.getGno();
    }
    return result;
  }

  @Override
  public Long remove(GuestbookDTO guestbookDTO) {
    guestbookRepository.deleteById(guestbookDTO.getGno());
    return guestbookDTO.getGno();
  }

  // 검색 조건 지정을 위한 메서드
  private BooleanBuilder getSearch(PageRequestDTO pageRequestDTO) {
    String type = pageRequestDTO.getType();
    String keyword = pageRequestDTO.getKeyword();

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    // 동적 검색을 위한 객체를 생성
    QGuestbook qGuestbook = QGuestbook.guestbook;
    //전체를 조건정의, 검색없을경우 전체를 지정
    BooleanExpression expression = qGuestbook.gno.gt(0L);
    booleanBuilder.and(expression); //첫번째 조건을 적용

    BooleanBuilder conditionBuilder = new BooleanBuilder();

    // 검색 조건이 없는 경우
    if(type==null || type.trim().length() == 0) return booleanBuilder;
    if(keyword==null || keyword.trim().length() == 0) return booleanBuilder;

    if(type.contains("t")) conditionBuilder.or(qGuestbook.title.contains(keyword));
    if(type.contains("c")) conditionBuilder.or(qGuestbook.content.contains(keyword));
    if(type.contains("w")) conditionBuilder.or(qGuestbook.writer.contains(keyword));
    booleanBuilder.and(conditionBuilder);
    return booleanBuilder;
  }
}
