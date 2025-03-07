package com.example.ex6.repository;

import com.example.ex6.entity.Member;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.Review;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewRepositoryTests {

  @Autowired
  ReviewRepository reviewRepository;

  @Autowired
  MemberRepository memberRepository;

  @Test
  public void insertMovieReviews() {
    IntStream.rangeClosed(1, 200).forEach(i -> {
      Long mno = (long) (Math.random() * 100) + 1;
      Long mid = (long) (Math.random() * 100) + 1;

      Member member = Member.builder().mid(mid).build();
      Review review = Review.builder().member(member)
          .movie(Movie.builder().mno(mno).build())
          .grade((int)(Math.random()*5)+1)
          .text("이 영화에 대하여..."+i)
          .build();
      reviewRepository.save(review);
    });
  }

  @Test
  public void testGetMovieReviews() {
    List<Review> result = reviewRepository.findByMovie(Movie.builder().mno(12L).build());
    result.forEach(r -> {
      System.out.println(r.getReviewnum());
      System.out.println(r.getGrade());
      System.out.println(r.getText());
      System.out.println(r.getMember().getEmail());
      System.out.println("=".repeat(30));
    });
  }

  @Transactional
  @Commit
  @Test
  public void testDeleteMember() {
    reviewRepository.deleteByMember(Member.builder().mid(1L).build());
    memberRepository.deleteById(1L);
  }
}




