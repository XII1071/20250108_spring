package com.example.apiserver.service;

import com.example.apiserver.dto.JournalDTO;
import com.example.apiserver.dto.PageRequestDTO;
import com.example.apiserver.dto.PageResultDTO;
import com.example.apiserver.entity.Journal;
import com.example.apiserver.entity.Photos;
import com.example.apiserver.repository.CommentsRepository;
import com.example.apiserver.repository.JournalRepository;
import com.example.apiserver.repository.PhotosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class JournalServiceImpl implements JournalService {
  private final JournalRepository journalRepository;
  private final PhotosRepository photosRepository;
  private final CommentsRepository commentsRepository;

  @Value("${com.example.upload.path}")
  private String uploadPath;

  @Override
  public PageResultDTO<JournalDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("mno").descending());
    Page<Object[]> result = journalRepository.searchPage(pageRequestDTO.getType(),
        pageRequestDTO.getKeyword(),
        pageable);

    Function<Object[], JournalDTO> fn = objects -> entityToDTO(
        (Journal) objects[0],
        (List<Photos>) (Arrays.asList((Photos) objects[1])),
        (Double) objects[2],
        (Long) objects[3]
    );
    return new PageResultDTO<>(result, fn);
  }

  @Override
  public Long register(JournalDTO journalDTO) {
    Map<String, Object> entityMap = dtoToEntity(journalDTO);
    Journal journal = (Journal) entityMap.get("journal");
    List<Photos> photosList = (List<Photos>) entityMap.get("photosList");
    journalRepository.save(journal);
    photosList.forEach(photos -> {
      photosRepository.save(photos);
    });
    return journal.getJno();
  }
}