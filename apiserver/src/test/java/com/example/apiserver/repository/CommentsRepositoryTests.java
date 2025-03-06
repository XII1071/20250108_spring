package com.example.apiserver.repository;

import com.example.apiserver.entity.Comments;
import com.example.apiserver.entity.Journal;
import com.example.apiserver.entity.Members;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentsRepositoryTests {
  @Autowired
  JournalRepository journalRepository;

  @Autowired
  MembersRepository memberRepository;

  @Test
  public void insertJournalComments() {
    IntStream.rangeClosed(1, 200).forEach(i -> {
      Long jno = (long) (Math.random() * 100) + 1;
      Long mid = (long) (Math.random() * 100) + 1;

    Comments comments = Comments.builder()
        .journal(Journal.builder().jno(jno).build())
        .members(Members.builder().build())
        .movie(Movie.builder().mno(mno).build())
        .grade((int)(Math.random()*5)+1)
        .text("이 영화에 대하여..."+i)
        .build();
    reviewRepository.save(review);
  });

}