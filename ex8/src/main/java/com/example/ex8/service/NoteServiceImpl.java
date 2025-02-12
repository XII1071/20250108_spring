package com.example.ex8.service;

import com.example.ex8.dto.NoteDTO;
import com.example.ex8.entity.Note;
import com.example.ex8.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

  private final NoteRepository noteRepository;

  @Override
  public Long register(NoteDTO noteDTO) {
    Note note = noteRepository.save(dtoToEntity(noteDTO));
    return note.getNum();
  }

  @Override
  public NoteDTO get(Long num) {
    Optional<Note> result = noteRepository.getWithWriter(num);
    return result.isPresent() ? entityToDTO(result.get()) : null;
  }

  @Override
  public void modify(NoteDTO noteDTO) {
    Optional<Note> result = noteRepository.findById(noteDTO.getNum());
    if (result.isPresent()) {
      Note note = result.get();
      note.changeTitle(noteDTO.getTitle());
      note.changeContent(noteDTO.getContent());
      noteRepository.save(note);
    }
  }

  @Override
  public void remove(Long num) {
    noteRepository.deleteById(num);
  }

  @Override
  public List<NoteDTO> getAllWithWriter(String writerEmail) {
    List<Note> noteList = noteRepository.getList(writerEmail);
    return noteList.stream().map(note -> entityToDTO(note)).collect(Collectors.toList());
  }
}
