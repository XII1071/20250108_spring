package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;
import com.example.ex4.repository.GuestbookRepository;
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
    log.info("guestbookDTO: " + guestbookDTO);
    Guestbook guestbook = dtoToEntity(guestbookDTO);
    guestbookRepository.save(guestbook);
    return guestbook.getGno();
  }

  @Override
  public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO) {
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("gno").descending());
    Page<Guestbook> page = guestbookRepository.findAll(pageable);

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
}