package com.example.ex3.service;

import com.example.ex3.dto.MemoDTO;

public interface MemoService {
  Long register(MemoDTO memoDTO);

  MemoDTO read(Long mno);

  void modify(MemoDTO memoDTO);

  void remove(MemoDTO memoDTO);
}
