package com.example.apiserver.repository;

import com.example.apiserver.entity.Journal;
import com.example.apiserver.entity.Members;
import com.example.apiserver.entity.Photos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JournalRepositoryTests {
  @Autowired
  private JournalRepository journalRepository;
  @Autowired
  private PhotosRepository photosRepository;

  @Test
  public void insertJournals() {
    IntStream.rangeClosed(0, 99).forEach(i -> {

      Journal journal = Journal.builder()
          .title("Title..." + i)
          .content("Content..." + i)
          .members(Members.builder().mid(Long.valueOf(i)).build())
          .build();
      journalRepository.save(journal);

      int count = (int) (Math.random() * 5) + 1;
      for (int j = 0; j < count; j++) {
        Photos photos = Photos.builder()
            .uuid(UUID.randomUUID().toString())
            .photosName("photo" +j+ ".jpg")
            .build();
      }
    });
  }
}