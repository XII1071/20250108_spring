package com.example.ex6.service;

import com.example.ex6.dto.ReviewDTO;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.Review;
import com.example.ex6.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
  private final ReviewRepository reviewRepository;

  @Override
  public Long register(ReviewDTO reviewDTO) {
    Review review = dtoToEntity(reviewDTO);
    reviewRepository.save(review);
    return review.getReviewnum();
  }

  @Override
  public List<ReviewDTO> getList(Long mno) {
    List<Review> result = reviewRepository
        .findByMovie(Movie.builder().mno(mno).build());
    return result.stream().map(
        review -> entityToDto(review)).collect(Collectors.toList()
    );
  }

  @Override
  public void modify(ReviewDTO reviewDTO) {
    Optional<Review> result = reviewRepository.findById(reviewDTO.getReviewnum());
    if (result.isPresent()) {
      Review review = result.get();
      /* 리뷰에서 변경할 텍스트, 별점(grade) */
      review.changeGrade(reviewDTO.getGrade());
      review.changeText(reviewDTO.getText());
      reviewRepository.save(review);
    }
  }

  @Override
  public void remove(Long reviewnum) {
    reviewRepository.deleteById(reviewnum);
  }
  @Override
  @Transactional
  public void deleteReviewsByMovie(Long mno) {
    log.info("🔴 해당 영화의 리뷰 삭제: mno = {}", mno);
    reviewRepository.deleteByMovieMno(mno);
    log.info("✅ 리뷰 삭제 완료");
  }
}