
package com.example.ex6.controller;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller                           // 이 클래스가 웹 요청을 처리하는 컨트롤러임을 스프링에 알려줍니다.
@RequestMapping("/movie")            // "/movie"로 시작하는 모든 요청(URL)을 이 컨트롤러에서 처리합니다.
@Log4j2                             // Log4j2를 사용해서 로그를 남길 수 있게 해줍니다.
@RequiredArgsConstructor            // final 필드를 위한 생성자를 자동으로 생성합니다 (여기서는 movieService가 주입됨).
public class MovieController {
  // MovieService는 영화 관련 비즈니스 로직을 담당하는 서비스입니다.
  private final MovieService movieService;

  @GetMapping({"", "/", "/list"})
  // 이 메서드는 "/movie", "/movie/", "/movie/list"로 들어오는 GET 요청을 처리합니다.
  public String list(PageRequestDTO pageRequestDTO, Model model) {
    // pageRequestDTO: 사용자가 요청한 "페이지 번호", "한 페이지에 보여줄 데이터 수", "검색어" 등의 정보를 담은 객체입니다.
    // model: 컨트롤러가 뷰(화면)에 데이터를 전달할 때 사용하는 상자와 같습니다.

    // movieService.getList(pageRequestDTO)를 호출하여,
    // pageRequestDTO에 담긴 정보를 바탕으로 영화 목록(및 페이징 정보를) 조회합니다.
    // 조회한 결과를 "result"라는 이름으로 model에 넣습니다.
    model.addAttribute("result", movieService.getList(pageRequestDTO));

    // "/movie/list"라는 문자열을 반환합니다.
    // 이는 스프링 MVC에게 "movie/list"라는 이름의 뷰(예: movie/list.html 또는 movie/list.jsp)를 찾아 화면에 보여달라는 뜻입니다.
    return "/movie/list";
  }

  @GetMapping("/register")
  public void register() {  }

  @PostMapping("/register")
  public String register(MovieDTO movieDTO, RedirectAttributes ra) {
    // movieDTO: 사용자가 영화 등록 폼을 제출하면서 입력한 영화 정보를 담은 객체입니다.
    // RedirectAttributes ra: 리다이렉트 시, 일회성으로 데이터를 전달할 때 사용하는 객체입니다.

    // movieService.register(movieDTO)를 호출하여,
    // 영화 정보를 데이터베이스에 저장하고, 저장된 영화의 식별자(mno)를 반환받습니
    Long mno = movieService.register(movieDTO);

    // ra.addFlashAttribute("msg", mno + "번 등록");
    // "msg"라는 이름으로, 등록된 영화의 번호와 "번 등록"이라는 문자열을 합친 메시지를
    // 리다이렉트 대상 페이지로 일회성(Flash)으로 전달합니다.
    // 예를 들어, "1번 등록" 같은 메시지가 될 수 있습니다.
    ra.addFlashAttribute("msg", mno + "번 등록");

    // "redirect:/movie/list"를 반환합니다.
    // 이는 클라이언트를 "/movie/list" URL로 리다이렉트 시키라는 의미입니다.
    // 즉, 영화 등록 후 영화 목록 페이지로 이동하도록 합니다.
    return "redirect:/movie/list";
  }

  @GetMapping({"/read", "/modify"})
  // 이 메서드는 "/movie/read" 또는 "/movie/modify" URL로 들어오는 GET 요청을 처리합니다.
  // 두 URL이 모두 이 메서드에 매핑되어, 영화 상세 정보 조회나 수정 화면으로 이동할 때 사용됩니다.
  public void get(Long mno, PageRequestDTO pageRequestDTO, Model model) {

    // 매개변수 설명:
    //  Long mno: 조회할 영화의 식별자(번호)를 나타내는 값입니다.
    //   이 값은 URL 쿼리스트링(예: ?mno=123)으로 전달됩니다.
    //
    //  PageRequestDTO pageRequestDTO: 영화 목록 페이지에서 사용한
    //   페이지 번호, 페이지 크기, 검색 조건 등 페이징 정보를 담은 객체입니다.
    //   이 정보는 주로 "목록" 페이지와 "상세" 페이지 사이에 사용자가 이전 페이지로 돌아갈 때 도움이 됩니다.
    //
    //  Model model: 컨트롤러에서 뷰(View)로 데이터를 전달할 때 사용하는 객체입니다.
    //   데이터를 key-value 형태로 저장해서, 뷰에서 해당 데이터를 사용할 수 있도록 합니다.

    // 조회한 영화 상세 정보를 "movieDTO"라는 이름으로 Model에 추가합니다.
    // 뷰(View)는 이 모델에서 "movieDTO"라는 키를 통해 영화 상세 정보를 받아와 화면에 표시합니다.
    model.addAttribute("movieDTO", movieService.get(mno));
  }
}
