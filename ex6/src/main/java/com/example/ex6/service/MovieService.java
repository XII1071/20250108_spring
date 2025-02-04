package com.example.ex6.service;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.PageRequestDTO;

public interface MovieService {

  PageRequestDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
}