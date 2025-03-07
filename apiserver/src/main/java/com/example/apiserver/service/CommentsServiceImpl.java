package com.example.apiserver.service;

import com.example.apiserver.dto.CommentsDTO;
import com.example.apiserver.entity.Comments;
import com.example.apiserver.entity.Journal;
import com.example.apiserver.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {
  private final CommentsRepository commentsRepository;

  @Override
  public Long register(CommentsDTO commentsDTO) {
    Comments comments = dtoToEntity(commentsDTO);
    commentsRepository.save(comments);
    return comments.getCno();
  }

  @Override
  public List<CommentsDTO> getList(Long jno) {
    List<Comments> result = commentsRepository.findByJournal(Journal.builder().jno(jno).build());
    return result.stream().map(
        comments -> entityToDto(comments)).collect(Collectors.toList()
    );
  }

  @Override
  public void modify(CommentsDTO commentsDTO) {
    Optional<Comments> result = commentsRepository.findById(commentsDTO.getCno());
    if (result.isPresent()) {
      Comments comments = result.get();
      comments.changeLikes(commentsDTO.getLikes());
      comments.changeText(commentsDTO.getText());
      commentsRepository.save(comments);
    }
  }

  @Override
  public void remove(Long cno) {
    commentsRepository.deleteById(cno);
  }
}
