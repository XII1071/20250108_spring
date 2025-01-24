package com.example.ex5.controller;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {
  private final BoardService boardService;

  @GetMapping({"", "/", "/list"})
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    model.addAttribute("result", boardService.getList(pageRequestDTO));
    return "/board/list";
  }

  @GetMapping("/register")
  public void register() {  }

  @PostMapping("/register")
  public String registerPost(BoardDTO boardDTO, RedirectAttributes ra) {
    Long bno = boardService.register(boardDTO);
    ra.addFlashAttribute("msg", bno + "번 게시물이 등록");
    return "redirect:/board/list";
  }

  @GetMapping({"/read", "/modify"})
  public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
    BoardDTO boardDTO = boardService.get(bno);
    model.addAttribute("boardDTO", boardDTO);
  }
  @PostMapping("/modify")
  public String modify(BoardDTO boardDTO,
                       PageRequestDTO pageRequestDTO, RedirectAttributes ra) {
    boardService.modify(boardDTO);
    ra.addFlashAttribute("msg", boardDTO.getBno() + "번 게시물이 수정");
    ra.addAttribute("bno", boardDTO.getBno());
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/board/read";
  }

  @PostMapping("/remove")
  public String remove(BoardDTO boardDTO,
                       PageRequestDTO pageRequestDTO, RedirectAttributes ra) {
    boardService.removeWithReplies(boardDTO.getBno());
    // 지우는 페이지에 목록 개수가 하나일 때 다음페이지로 보냄
    // 목록 가져와서 좋은 코드 아님. 페이지 목록 개수는 pageRequestDTO에 별도 저장 필요
    if (boardService.getList(pageRequestDTO).getDtoList().size() == 0
        && pageRequestDTO.getPage() != 1) {
      pageRequestDTO.setPage(pageRequestDTO.getPage() - 1);
    }
    ra.addFlashAttribute("msg", boardDTO.getBno() + "번 게시물이 삭제");
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/board/list";
  }

}
