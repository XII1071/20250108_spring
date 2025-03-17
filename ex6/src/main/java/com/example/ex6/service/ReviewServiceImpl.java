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
      review.changeGrade(reviewDTO.getGrade());
      review.changeText(reviewDTO.getText());
      reviewRepository.save(review);
    }
  }

  @Override
  public void remove(Long reviewnum) {
    reviewRepository.deleteById(reviewnum);
  }
}
