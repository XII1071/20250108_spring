package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestbookServiceTests {
  @Autowired
  private GuestbookService guestbookService;

  @Test
  public void testRegist() {
    GuestbookDTO guestbookDTO = GuestbookDTO.builder()
        .title("new Title...")
        .content("new Content...")
        .writer("user0")
        .build();
    guestbookService.register(guestbookDTO);
  }


}