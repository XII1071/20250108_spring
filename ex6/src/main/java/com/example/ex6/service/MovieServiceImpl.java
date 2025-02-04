
package com.example.ex6.service;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.dto.PageResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.function.Function;

public class MovieServiceImpl implements MovieService {
  @Override
  public PageRequestDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("mno").descending());
    Page<Object[]> result = null;
    Function<Object[], MovieDTO> fn = null;
    return new PageResultDTO<>(result, fn);
  }
}
