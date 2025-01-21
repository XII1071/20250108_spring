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
  // controller에서 해야 할 3가지
  // 1) 처리해야 할 데이터를 확인한다.
  // 2) 데이터를 처리할 핵심 로직으로 보낸다.
  // 3) 다음 페이지에 보내야 할 자료를 처리한다.
  private final GuestbookService guestbookService;

  @GetMapping({"/list", "", "/"})
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    log.info("list..." + pageRequestDTO);
    // result에 getList의 결과인 pageResultDTO를 리턴
    model.addAttribute("result", guestbookService.getList(pageRequestDTO));
    return "/guestbook/list";
  }

  @GetMapping("/register")
  public void register() {
  }

  @PostMapping("/register")
  public String registerPost(GuestbookDTO guestbookDTO, RedirectAttributes ra) {
    Long gno = guestbookService.register(guestbookDTO);
    // RedirectAttributes는 뷰에 데이터를 전송하되 한번만 전송할수 있다.
    ra.addFlashAttribute("msg", gno + "번 게시물이 등록");
    // redirect는 컨트롤러의 또 다른 주소로 이동시킴
    return "redirect:/guestbook/list";
  }

  @GetMapping({"/read", "/modify"})
  public void read(Long gno, PageRequestDTO pageRequestDTO, Model model) {
    GuestbookDTO guestbookDTO = guestbookService.read(gno);
    model.addAttribute("guestbookDTO", guestbookDTO);
  }

  @PostMapping("/modify")
  public String modify(GuestbookDTO guestbookDTO,
                       PageRequestDTO pageRequestDTO, RedirectAttributes ra) {
    Long gno = guestbookService.modify(guestbookDTO);
    ra.addFlashAttribute("msg", gno + "번 게시물이 수정");
    ra.addAttribute("gno", gno);
    ra.addAttribute("page", pageRequestDTO.getPage());
    return "redirect:/guestbook/read";
  }

  @PostMapping("/remove")
  public String remove(GuestbookDTO guestbookDTO,
                       PageRequestDTO pageRequestDTO, RedirectAttributes ra) {
    Long gno = guestbookService.remove(guestbookDTO);
    ra.addFlashAttribute("msg", gno + "번 게시물이 삭제");
    ra.addAttribute("page", pageRequestDTO.getPage());
    return "redirect:/guestbook/list";
  }
}