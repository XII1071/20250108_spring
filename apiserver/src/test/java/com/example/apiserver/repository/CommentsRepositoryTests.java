package com.example.apiserver.repository;

import com.example.apiserver.entity.Comments;
import com.example.apiserver.entity.Journal;
import com.example.apiserver.entity.Members;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
class CommentsRepositoryTests {
  @Autowired
  CommentsRepository commentsRepository;

  @Autowired
  MembersRepository membersRepository;

  @Test
  public void insertJournalComments() {
    IntStream.rangeClosed(1, 200).forEach(i -> {
      Long mid = (long) (Math.random() * 100) + 1;
      Long jno = (long) (Math.random() * 100) + 1;

      Comments comments = Comments.builder()
          .journal(Journal.builder().jno(jno).build())
          .members(Members.builder().mid(mid).build())
          .likes((int)(Math.random() * 5) + 1)
          .text("이 글에 대하여..." + i)
          .build();
      commentsRepository.save(comments);
    });
  }

  @Test
  public void testGetJournalComments() {
    List<Comments> result = commentsRepository.findByJournal(Journal.builder().jno(1L).build());
    result.forEach(c -> {
      System.out.println(c.getCno());
      System.out.println(c.getText());
      System.out.println(c.getLikes());
      System.out.println(c.getMembers().getEmail());
      System.out.println("=".repeat(30));
    });
  }

  @Transactional
  @Commit
  @Test
  public void testDeleteMembers() {
//    commentsRepository.deleteByMembers(Members.builder().mid(2L).build());
    membersRepository.deleteById(2L);
  }
}