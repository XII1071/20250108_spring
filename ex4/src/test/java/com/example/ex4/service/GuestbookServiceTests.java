package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestbookServiceTests {

  @Autowired
  private GuestbookService guestbookService;

  @Test
  public void testRegister() {
    GuestbookDTO guestbookDTO = GuestbookDTO.builder()
        .title("new Title...")
        .content("new Content...")
        .writer("user0")
        .build();
    guestbookService.register(guestbookDTO);
  }

  @Test
  public void testGetList() {
    PageRequestDTO pageRequestDTO =
        PageRequestDTO.builder().page(1).size(10).build();
    PageResultDTO<GuestbookDTO, Guestbook> pageResultDTO =
        guestbookService.getList(pageRequestDTO);

    System.out.println("PREV: " + pageResultDTO.isPrev());
    System.out.println("NEXT: " + pageResultDTO.isNext());
    System.out.println("TOTAL: " + pageResultDTO.getTotalPage());
    System.out.println("============================================");
    for (GuestbookDTO guestbookDTO : pageResultDTO.getDtoList()) {
      System.out.println(guestbookDTO);
    }
    System.out.println("============================================");
    pageResultDTO.getPageList().forEach(i -> System.out.print(i+ ", "));
    System.out.println();
  }

  @Test
  public void testSearch() {
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
        .page(1)
        .size(10)
        .type("tc")
        .keyword("1")
        .build();
    PageResultDTO<GuestbookDTO, Guestbook> resultDTO = guestbookService.getList(pageRequestDTO);
    System.out.println("PREV: " +resultDTO.isPrev());
    System.out.println("NEXT: " +resultDTO.isNext());
    System.out.println("TOTAL: " + resultDTO.getTotalPage());

    System.out.println("----------------------------------------------");
    for (GuestbookDTO dto : resultDTO.getDtoList()) System.out.println(dto);

    resultDTO.getPageList().forEach(integer -> System.out.println(integer));
  }
}