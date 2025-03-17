package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;

public interface GuestbookService {
  default Guestbook dtoToEntity(GuestbookDTO guestbookDTO) {
    Guestbook guestbook = Guestbook.builder()
        .gno(guestbookDTO.getGno())
        .title(guestbookDTO.getTitle())
        .content(guestbookDTO.getContent())
        .writer(guestbookDTO.getWriter())
        .build();
    return guestbook;
  }

  default GuestbookDTO entityToDto(Guestbook guestbook) {
    GuestbookDTO guestbookDTO = GuestbookDTO.builder()
        .gno(guestbook.getGno())
        .title(guestbook.getTitle())
        .content(guestbook.getContent())
        .writer(guestbook.getWriter())
        .regDate(guestbook.getRegDate())
        .modDate(guestbook.getModDate())
        .build();
    return guestbookDTO;
  }
  Long register(GuestbookDTO guestbookDTO);
  PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO);
  GuestbookDTO read(Long gno);

  Long modify(GuestbookDTO guestbookDTO);
  Long remove(GuestbookDTO guestbookDTO);
}
