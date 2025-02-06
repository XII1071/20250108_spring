package com.example.ex6.service;

import com.example.ex6.dto.ReviewDTO;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.Review;
import com.example.ex6.repository.ReviewRepository;
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
  // 클라이언트 들고올 땐 dto
  // Repository 들고올 땐 entity


  @Override
  public Long register(ReviewDTO reviewDTO) {
    Review review = dtoToEntity(reviewDTO);
    reviewRepository.save(review);
    return review.getReviewnum();
  }

  @Override
  public List<ReviewDTO> getList(Long mno) {
    List<Review> result = reviewRepository.findByMovie(Movie.builder()
        .mno(mno).build());
    return result.stream().map(review -> entityToDto(review))
        .collect(Collectors.toList());

  }

  @Override
  public void modify(ReviewDTO reviewDTO) {
    log.info(reviewDTO);
    Optional<Review> result = reviewRepository.findById(reviewDTO.getMno());
    if (result.isPresent()) {
      Review review = result.get();
      /* 리뷰에서 변경할 텍스트, 별점(grade) */
      review.changeText(reviewDTO.getText());
      review.changeGrade(reviewDTO.getGrade());
      reviewRepository.save(review);
    }

  }

  @Override
  public void remove(Long reviewnum) {
    reviewRepository.deleteById(reviewnum);
  }
}
