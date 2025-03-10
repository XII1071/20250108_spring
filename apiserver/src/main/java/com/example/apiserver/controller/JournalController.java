package com.example.apiserver.controller;

import com.example.apiserver.dto.JournalDTO;
import com.example.apiserver.service.JournalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/journal")

public class JournalController {
  private final JournalService journalService;

  @GetMapping(value = "")
  public void register() {
  }

  @PostMapping(value = "/{jno}")
  public String registerPost(JournalDTO journalDTO, RedirectAttributes ra) {
    Long jno = JournalService.register(journalDTO);
    ra.addFlashAttribute("msg", jno);
    return "redirect:/journal/list";
  }
}
