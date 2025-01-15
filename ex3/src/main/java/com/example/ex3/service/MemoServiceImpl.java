package com.example.ex3.service;

import com.example.ex3.dto.MemoDTO;
import com.example.ex3.entity.Memo;
import com.example.ex3.repository.MemoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemoServiceImpl implements MemoService {
  public MemoServiceImpl(MemoRepository memoRepository) {
    this.memoRepository = memoRepository;
  }

  private final MemoRepository memoRepository;

  @Override
  public Long register(MemoDTO memoDTO) {
//    Memo memo = new Memo(memoDTO.getMno(), memoDTO.getMemoText());
    Memo memo = dtoToEntity(memoDTO);
    memoRepository.save(memo);
    return memo.getMno();
  }

  @Override
  public MemoDTO read(Long mno) {
    Optional<Memo> result = memoRepository.findById(mno);
    if(result.isPresent())
      return entityToDto(result.get());
    return null;
  }

  @Override
  public void modify(MemoDTO memoDTO) {

  }

  @Override
  public void remove(MemoDTO memoDTO) {

  }
}
