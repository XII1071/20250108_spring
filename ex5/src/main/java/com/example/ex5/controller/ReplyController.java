package com.example.ex5.controller;

import com.example.ex5.dto.ReplyDTO;
import com.example.ex5.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {
  private final ReplyService replyService;

  @GetMapping(value = "/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno) {
    return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
  }

  @PostMapping(value = {"", "/"})
  public ResponseEntity<String> register(@RequestBody ReplyDTO replyDTO) {
    String result = "";
    result = replyService.register(replyDTO) + "번 댓글 등록";
    return new ResponseEntity<>(result , HttpStatus.OK);
  }

  @PutMapping(value = {"", "/"})
  public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {
    String result = "";
    replyService.modify(replyDTO);
    result =  replyDTO.getRno()+ "번 댓글 수정";
    return new ResponseEntity<>(result , HttpStatus.OK);
  }

  @DeleteMapping(value = {"", "/"})
  public ResponseEntity<String> delete(@RequestBody ReplyDTO replyDTO) {
    String result = "";
    replyService.remove(replyDTO.getRno());
    result =  replyDTO.getRno()+ "번 댓글 삭제";
    return new ResponseEntity<>(result , HttpStatus.OK);
  }

}
