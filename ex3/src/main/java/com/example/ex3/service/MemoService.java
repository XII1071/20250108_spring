package com.example.ex3.service;

import com.example.ex3.dto.MemoDTO;
import com.example.ex3.entity.Memo;

public interface MemoService {
  public default Memo dtoToEntity(MemoDTO memoDTO){
    return new Memo(memoDTO.getMno(), memoDTO.getMemoText());
  }

  public default MemoDTO entityToDto(Memo memo) {
    return new MemoDTO(memo.getMno(), memo.getMemoText());
  }

  Long register(MemoDTO memoDTO);

  MemoDTO read(Long mno);

  void modify(MemoDTO memoDTO);

  void remove(MemoDTO memoDTO);
}
