package com.example.ex4.controller;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

  private final GuestbookService guestbookService;

  @GetMapping({"/list", "", "/"})
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    log.info("list...." + pageRequestDTO);

    model.addAttribute("result", guestbookService.getList(pageRequestDTO));
    return "/guestbook/list";
  }

  @GetMapping("/register")
  public void register() { }

  @PostMapping("/register")
  public String registerPost(GuestbookDTO guestbookDTO, RedirectAttributes ra) {
    Long gno = guestbookService.register(guestbookDTO);
    ra.addFlashAttribute("msg", gno + "번 등록");
    return "redirect:/guestbook/list";
  }

  @GetMapping({"/read", "/modify"})
  public void read() {

  }

  @PostMapping("/remove")
  public String remove() {
    return "/guestbook/list";
  }
}
