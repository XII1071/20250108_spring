package com.example.ex5.controller;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * 게시판과 관련된 웹 요청을 처리하는 컨트롤러 클래스입니다.
 * "/board"로 시작하는 URL을 이 컨트롤러가 담당합니다.
 */
@Controller                  // (1) 스프링 MVC에서 웹 요청을 처리하는 컨트롤러로 등록
@RequestMapping("/board")   // (2) "/board"로 시작하는 모든 요청을 이 클래스가 처리
@Log4j2                    // (3) Lombok을 사용해, Log4j2 로그 객체를 자동으로 만들어 준다 (log.info, log.debug 등 활용 가능)
@RequiredArgsConstructor    // (4) 'final' 필드를 파라미터로 받는 생성자를 자동으로 생성 (생성자 주입 방식)
public class BoardController {

  // (5) 게시판 관련 비즈니스 로직을 담당하는 서비스
  //     @RequiredArgsConstructor 에 의해 자동으로 생성자 주입된다.
  private final BoardService boardService;

  /*
   * 게시판 목록을 조회하여 뷰에 전달하는 메서드입니다.
   * "/board", "/board/", "/board/list" 로 GET 요청이 들어오면 실행됩니다.
   *
   * @param pageRequestDTO 페이징 처리나 검색 조건 등을 담고 있는 DTO
   * @param model          뷰(JSP, Thymeleaf 등)에 데이터를 담아 전달하기 위한 객체
   * @return               렌더링할 뷰(HTML/JSP)의 논리적 경로
   */
  @GetMapping({"", "/", "/list"})
  public String list(PageRequestDTO pageRequestDTO, Model model) {

    // (6) 서비스 계층의 getList 메서드를 호출하여
    //     페이지 정보에 맞는 게시글 목록을 가져오고,
    //     "result"라는 이름으로 모델에 담아 뷰로 넘긴다.
    model.addAttribute("result", boardService.getList(pageRequestDTO));

    // (7) "/board/list" 라는 문자열을 반환하면,
    //     스프링의 뷰 리졸버가 알맞은 HTML/JSP 파일(예: /templates/board/list.html)을 찾아서 렌더링한다.
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
