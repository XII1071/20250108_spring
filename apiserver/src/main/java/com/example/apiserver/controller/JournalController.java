package com.example.apiserver.controller;

import com.example.apiserver.dto.JournalDTO;
import com.example.apiserver.dto.PageRequestDTO;
import com.example.apiserver.service.JournalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/journal")
public class JournalController {
  private final JournalService journalService;

  @Value("${com.example.upload.path}")
  private String uploadPath;

  private void typeKeywordInit(PageRequestDTO pageRequestDTO) {
    if (pageRequestDTO.getType().equals("null")) pageRequestDTO.setType("");
    if (pageRequestDTO.getKeyword().equals("null")) pageRequestDTO.setKeyword("");
  }

  @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> list(PageRequestDTO pageRequestDTO) {
    Map<String, Object> result = new HashMap<>();
    result.put("pageResultDTO", journalService.getList(pageRequestDTO));
    result.put("pageRequestDTO", pageRequestDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
  public ResponseEntity<Long> registerJournal(@RequestBody JournalDTO journalDTO) {
    Long jno = journalService.register(journalDTO);
    return new ResponseEntity<>(jno, HttpStatus.OK);
  }

  @GetMapping(value = {"/read/{jno}", "/modify/{jno}"}, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, JournalDTO>> getJournal(@PathVariable("jno") Long jno) {
    JournalDTO journalDTO = journalService.get(jno);
    Map<String, JournalDTO> result = new HashMap<>();
    result.put("journalDTO", journalDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping(value = "/modify", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> modify(@RequestBody JournalDTO dto,
                                                    @RequestBody PageRequestDTO pageRequestDTO) {
    log.info("modify post... dto: " + dto);
    journalService.modify(dto);
    typeKeywordInit(pageRequestDTO);
    Map<String, String> result = new HashMap<>();
    result.put("msg", dto.getJno() + " 수정");
    result.put("jno", dto.getJno() + "");
    result.put("page", pageRequestDTO.getPage() + "");
    result.put("type", pageRequestDTO.getType());
    result.put("keyword", pageRequestDTO.getKeyword());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping(value = "/remove/{jno}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> remove(
      @PathVariable Long jno, @RequestBody PageRequestDTO pageRequestDTO) {

    Map<String, String> result = new HashMap<>();
    List<String> photoList = journalService.removeWithCommentsAndPhotos(jno);
    photoList.forEach(fileName -> {
      try {
        log.info("removeFile............" + fileName);
        String srcFileName = URLDecoder.decode(fileName, "UTF-8");
        File file = new File(uploadPath + File.separator + srcFileName);
        file.delete();
        File thumb = new File(file.getParent(), "s_" + file.getName());
        thumb.delete();
      } catch (Exception e) {
        log.info("remove file : " + e.getMessage());
      }
    });
    if (journalService.getList(pageRequestDTO).getDtoList().size() == 0 && pageRequestDTO.getPage() != 1) {
      pageRequestDTO.setPage(pageRequestDTO.getPage() - 1);
    }
    typeKeywordInit(pageRequestDTO);
    result.put("msg", jno + " 삭제");
    result.put("page", pageRequestDTO.getPage() + "");
    result.put("type", pageRequestDTO.getType() + "");
    result.put("keyword", pageRequestDTO.getKeyword() + "");
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
