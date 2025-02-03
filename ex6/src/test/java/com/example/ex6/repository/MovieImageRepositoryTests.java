package com.example.ex6.repository;

import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieImageRepositoryTests {

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private MovieImageRepository movieImageRepository;

  @Test
  @Transactional
  @Commit
  public void insertMovies() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      Movie movie = Movie.builder().title("Movie..." + i).build();
      movieRepository.save(movie);
      int count = (int) (Math.random() * 5) + 1;
      for (int j = 0; j < count; j++) {
        MovieImage movieImage = MovieImage.builder()
            .uuid(UUID.randomUUID().toString())
            .movie(movie)
            .imgName("test"+j+"jpg")
            .build();
        movieImageRepository.save(movieImage);
      }
    });
  }
}