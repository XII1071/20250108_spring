package com.example.ex4.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageResultDTO<DTO, EN> {
  private List<DTO> dtoList; // 한 페이지당 목록
  private  int totalPage; // 총 페이지 수
  private int page; //현재 페이지 번호
  private int size; // 페이지당 목록 개수
  private int start, end; // page의 시작번호와 끝번호
  private boolean prev, next;
  private List<Integer> pageList; // 페이지 번호의 목록

  public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
    dtoList = result.stream().map(fn).collect(Collectors.toList());
  }
}
