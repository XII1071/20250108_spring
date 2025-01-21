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

@Controller  // Spring MVC에서 컨트롤러 역할을 하는 클래스임을 나타냄
@RequestMapping("/guestbook")  // 이 클래스의 URL 기본 경로를 설정
@Log4j2  // 로그를 기록하기 위한 어노테이션
@RequiredArgsConstructor  // 생성자를 자동으로 만들어주는 어노테이션 (주로 final 멤버 변수 초기화에 사용)
public class GuestbookController {

  // 이 컨트롤러는 GuestbookService와 협력해서 요청을 처리함
  private final GuestbookService guestbookService;

  // 1. 방명록 목록 페이지를 보여주는 메서드
  @GetMapping({"/list", "", "/"})  // URL "list", 기본 URL, 또는 "/" 요청 처리
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    log.info("list..." + pageRequestDTO);  // 요청된 페이지 정보 로그 기록

    // 방명록 목록 데이터를 가져와서 "result"라는 이름으로 모델에 추가
    model.addAttribute("result", guestbookService.getList(pageRequestDTO));

    // "guestbook/list"라는 뷰 파일을 반환 (HTML 경로)
    return "/guestbook/list";
  }

  // 2. 방명록 등록 페이지를 보여주는 메서드
  @GetMapping("/register")
  public void register() {
    // 별도 반환값이 없으므로 URL에 해당하는 뷰 파일을 직접 찾음 ("guestbook/register")
  }

  // 3. 방명록 등록 처리 메서드 (사용자가 등록 폼을 제출했을 때 동작)
  @PostMapping("/register")
  public String registerPost(GuestbookDTO guestbookDTO, RedirectAttributes ra) {
    // guestbookDTO 데이터를 저장하고 생성된 게시물 번호를 반환
    Long gno = guestbookService.register(guestbookDTO);

    // 한 번만 전송될 메시지를 Flash 속성으로 설정
    ra.addFlashAttribute("msg", gno + "번 게시물이 등록");

    // 방명록 목록 페이지로 리다이렉트
    return "redirect:/guestbook/list";
  }

  // 4. 방명록 읽기 및 수정 페이지를 보여주는 메서드
  @GetMapping({"/read", "/modify"})  // "read" 또는 "modify" 요청 처리
  public void read(Long gno, PageRequestDTO pageRequestDTO, Model model) {
    // 특정 게시물을 읽어서 guestbookDTO에 저장
    GuestbookDTO guestbookDTO = guestbookService.read(gno);

    // 모델에 게시물 정보를 추가하여 뷰에 전달
    model.addAttribute("guestbookDTO", guestbookDTO);
  }

  // 5. 방명록 수정 처리 메서드
  @PostMapping("/modify")
  public String modify(GuestbookDTO guestbookDTO,
                       PageRequestDTO pageRequestDTO, RedirectAttributes ra) {
    // 게시물 수정 후 수정된 게시물 번호 반환
    Long gno = guestbookService.modify(guestbookDTO);

    // 수정 완료 메시지를 Flash 속성으로 설정
    ra.addFlashAttribute("msg", gno + "번 게시물이 수정");

    // 읽기 페이지로 리다이렉트하면서 게시물 번호와 페이지 정보 전달
    ra.addAttribute("gno", gno);
    ra.addAttribute("page", pageRequestDTO.getPage());

    return "redirect:/guestbook/read";
  }

  // 6. 방명록 삭제 처리 메서드
  @PostMapping("/remove")
  public String remove(GuestbookDTO guestbookDTO,
                       PageRequestDTO pageRequestDTO, RedirectAttributes ra) {
    // 게시물 삭제 후 삭제된 게시물 번호 반환
    Long gno = guestbookService.remove(guestbookDTO);
    // 지우는 페이지에 목록 개수가 하나일 떄 다음페이지로 보냄
    // 목록을 가져오는 코드라서 좋은 코드가 아님. 페이지 목록 개수는 pageRequestDTO에 별도 저장
    if (guestbookService.getList(pageRequestDTO).getDtoList().size() == 0
        && pageRequestDTO.getPage() != 1) {
      pageRequestDTO.setPage(pageRequestDTO.getPage() - 1);
    }
    // 삭제 완료 메시지를 Flash 속성으로 설정
    ra.addFlashAttribute("msg", gno + "번 게시물이 삭제");

    // 목록 페이지로 리다이렉트하면서 페이지 정보 전달
    ra.addAttribute("page", pageRequestDTO.getPage());

    return "redirect:/guestbook/list";
  }
}
