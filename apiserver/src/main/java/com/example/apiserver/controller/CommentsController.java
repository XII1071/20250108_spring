package com.example.apiserver.controller;

import com.example.apiserver.dto.CommentsDTO;
import com.example.apiserver.service.CommentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentsController {
  private final CommentsService commentsService;

  @GetMapping(value = "/all/{jno}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CommentsDTO>> getList(@PathVariable("jno") Long jno) {
    List<CommentsDTO> commentsDTOList = commentsService.getList(jno);
    return new ResponseEntity<>(commentsDTOList, HttpStatus.OK);
  }

  @PostMapping("/{jno}")
  public ResponseEntity<Long> register(@RequestBody CommentsDTO commentsDTO) {
    Long cno = commentsService.register(commentsDTO);
    return new ResponseEntity<>(cno, HttpStatus.OK);
  }

  @PutMapping("/{jno}/{cno}")
  public ResponseEntity<Long> modify(@RequestBody CommentsDTO commentsDTO) {
    commentsService.modify(commentsDTO);
    return new ResponseEntity<>(commentsDTO.getCno(), HttpStatus.OK);
  }

  @DeleteMapping("/{jno}/{cno}")
  public ResponseEntity<Long> delete(@PathVariable Long cno) {
    commentsService.remove(cno);
    return new ResponseEntity<>(cno, HttpStatus.OK);
  }
}
