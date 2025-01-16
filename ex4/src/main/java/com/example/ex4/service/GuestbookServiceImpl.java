package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.entity.Guestbook;
import com.example.ex4.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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
}
