package com.example.ex6.repository;

import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieRepositoryTests {

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private MovieImageRepository movieImageRepository;

  @Test
  @Transactional
  @Commit
  public void insertMovies() {
    IntStream.rangeClosed(1,100).forEach(i ->{
      Movie movie = Movie.builder().title("Movie..." + i).build();
      movieRepository.save(movie);
      int count = (int)(Math.random()*5) + 1;
      for (int j = 0; j < count; j++) {
        MovieImage movieImage = MovieImage.builder()
            .uuid(UUID.randomUUID().toString())
            .movie(movie)
            .imgName("test"+j+".jpg")
            .build();
        movieImageRepository.save(movieImage);
      }
    });
  }

  @Test
  public void testGetListPage() {
    PageRequest pageRequest =
        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
    Page<Object[]> result = movieRepository.getListPage(pageRequest);
    for (Object[] objects : result.getContent()) {
      System.out.println(Arrays.toString(objects));
    }
  }
  @Test
  public void testGetListPageImg() {
    PageRequest pageRequest =
        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
    Page<Object[]> result = movieRepository.getListPageImg(pageRequest);
    for (Object[] objects : result.getContent()) {
      System.out.println(Arrays.toString(objects));
    }
  }

//  @Test
//  public void testGetListPageMaxImg() {
//    PageRequest pageRequest =
//        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
//    Page<Object[]> result = movieRepository.getListPageMaxImg(pageRequest);
//    for (Object[] objects : result.getContent()) {
//      System.out.println(Arrays.toString(objects));
//    }
//  }

  @Test
  public void testGetListPageImgNative() {
    PageRequest pageRequest =
        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "movie_mno"));
    Page<Object[]> result = movieRepository.getListPageImgNative(pageRequest);
    for (Object[] objArr : result.getContent()) {
      System.out.println(Arrays.toString(objArr));
    }
  }

  @Test
  public void testGetListPageImgJPQL() {
    PageRequest pageRequest =
        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
    Page<Object[]> result = movieRepository.getListPageImgJPQL(pageRequest);
    for (Object[] objArr : result.getContent()) {
      System.out.println(Arrays.toString(objArr));
    }
  }

  @Test
  public void testGetMaxQuery() {
    PageRequest pageRequest =
        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "movie"));
    Page<Object[]> result = movieRepository.getMaxQuery(pageRequest);
    for (Object[] objArr : result.getContent()) {
      System.out.println(Arrays.toString(objArr));
    }
  }
}