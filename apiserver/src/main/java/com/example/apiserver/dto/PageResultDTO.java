package com.example.apiserver.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {
  private List<DTO> dtoList; // 한 페이지당 목록
  private int totalPage; // 총페이지수
  private int page; // 현재 페이지 번호
  private int size; // 페이지당 목록 개수
  private int start, end; // page의 시작 번호와 끝번호
  private boolean prev, next;
  private List<Integer> pageList; // 페이지 번호의 목록

  public PageResultDTO(Page<EN> page, Function<EN, DTO> fn) {
    dtoList = page.stream().map(fn).collect(Collectors.toList());
    totalPage = page.getTotalPages();
    makePageList(page.getPageable());
  }

  private void makePageList(Pageable pageable) {
    this.page = pageable.getPageNumber() + 1;
    this.size = pageable.getPageSize();

    int tmpEndPage = (int) (Math.ceil(page / 10.0)) * 10;
    start = tmpEndPage - 9;
    prev = start > 1;
    end = totalPage > tmpEndPage ? tmpEndPage : totalPage;
    next = totalPage > tmpEndPage;
    pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
  }
}
