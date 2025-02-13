package com.example.ex8.controller;

import com.example.ex8.dto.NoteDTO;
import com.example.ex8.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {
  private final NoteService noteService;

  @PostMapping(value = "")
  public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO) {
    log.info("register.....................");
    return new ResponseEntity<>(noteService.register(noteDTO), HttpStatus.OK);
  }

  @GetMapping(value = "/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<NoteDTO> read(@PathVariable("num") Long num) {
    return new ResponseEntity<>(noteService.get(num), HttpStatus.OK);
  }

  @DeleteMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> remove(@PathVariable("num") Long num) {
    noteService.remove(num);
    return new ResponseEntity<>("removed", HttpStatus.OK);
  }

  @PutMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> modify(@RequestBody NoteDTO dto) {
    noteService.modify(dto);
    return new ResponseEntity<>("modified", HttpStatus.OK);
  }
}
