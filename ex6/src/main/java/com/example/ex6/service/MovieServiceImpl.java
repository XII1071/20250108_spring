package com.example.ex6.service;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.dto.PageResultDTO;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;
import com.example.ex6.repository.MovieImageRepository;
import com.example.ex6.repository.MovieRepository;
import com.example.ex6.repository.ReviewRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
  private final MovieRepository movieRepository;
  private final MovieImageRepository movieImageRepository;
  private final ReviewRepository reviewRepository;

  @Value("${com.example.upload.path}")
  private String uploadPath;

  @Override
  public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("mno").descending());
    // Page<Movie> result = movieRepository.findAll(pageable);
    // Page<Object[]> result = movieRepository.getListPageImg(pageable);
    Page<Object[]> result = movieRepository.searchPage(pageRequestDTO.getType(),
        pageRequestDTO.getKeyword(),
        pageable);

    Function<Object[], MovieDTO> fn = objects -> entityToDTO(
        (Movie) objects[0],
        (List<MovieImage>) (Arrays.asList((MovieImage) objects[1])),
        (Double) objects[2],
        (Long) objects[3]
    );
    return new PageResultDTO<>(result, fn);
  }

  @Override
  public Long register(MovieDTO movieDTO) {
    Map<String, Object> entityMap = dtoToEntity(movieDTO);
    Movie movie = (Movie) entityMap.get("movie");
    List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");
    movieRepository.save(movie);
    movieImageList.forEach(movieImage -> {
      movieImageRepository.save(movieImage);
    });
    return movie.getMno();
  }

  @Override
  public MovieDTO get(Long mno) {
    List<Object[]> result = movieRepository.getMovieWithAll(mno);
    Movie movie = (Movie) result.get(0)[0];
    List<MovieImage> movieImages = new ArrayList<>();
    result.forEach(new Consumer<Object[]>() {
      @Override
      public void accept(Object[] objects) {
        movieImages.add((MovieImage) objects[1]);
      }
    });
    Double avg = (Double) result.get(0)[2];
    Long reviewCnt = (Long) result.get(0)[3];

    return entityToDTO(movie, movieImages, avg, reviewCnt);
  }

  @Override
  public void removeMovieImagebyUUID(String uuid) {
    movieImageRepository.deleteByUuid(uuid);
  }

  @Transactional
  @Override
  public void modify(MovieDTO movieDTO) {
    Optional<Movie> result = movieRepository.findById(movieDTO.getMno());
    if (result.isPresent()) {
      Map<String, Object> entityMap = dtoToEntity(movieDTO);
      Movie movie = (Movie) entityMap.get("movie");
      movie.changeTitle(movieDTO.getTitle());
      movieRepository.save(movie);
      // movieImageList :: 수정창에서 이미지 수정할 게 있는 경우의 목록
      List<MovieImage> newMovieImageList =
          (List<MovieImage>) entityMap.get("movieImageList");
      List<MovieImage> oldMovieImageList =
          movieImageRepository.findByMno(movie.getMno());
      if(newMovieImageList == null || newMovieImageList.size() == 0) {
        // 수정창에서 이미지 모두를 지웠을 때
        movieImageRepository.deleteByMno(movie.getMno());
        for (int i = 0; i < oldMovieImageList.size(); i++) {
          MovieImage oldMovieImage = oldMovieImageList.get(i);
          String fileName = oldMovieImage.getPath() + File.separator
              + oldMovieImage.getUuid() + "_" + oldMovieImage.getImgName();
          deleteFile(fileName);
        }
      } else { // newMovieImageList에 일부 변화 발생
        newMovieImageList.forEach(movieImage -> {
          boolean result1 = false;
          for (int i = 0; i < oldMovieImageList.size(); i++) {
            result1 = oldMovieImageList.get(i).getUuid().equals(movieImage.getUuid());
            if(result1) break;
          }
          if(!result1) movieImageRepository.save(movieImage);
        });
        oldMovieImageList.forEach(oldMovieImage -> {
          boolean result1 = false;
          for (int i = 0; i < newMovieImageList.size(); i++) {
            result1 = newMovieImageList.get(i).getUuid().equals(oldMovieImage.getUuid());
            if(result1) break;
          }
          if(!result1) {
            movieImageRepository.deleteByUuid(oldMovieImage.getUuid());
            String fileName = oldMovieImage.getPath() + File.separator
                + oldMovieImage.getUuid() + "_" + oldMovieImage.getImgName();
            deleteFile(fileName);
          }
        });
      }
    }
  }

  private void deleteFile(String fileName) {
    // 실제 파일도 지우기
    String searchFilename = null;
    try {
      searchFilename = URLDecoder.decode(fileName, "UTF-8");
      File file = new File(uploadPath + File.separator + searchFilename);
      file.delete();
      new File(file.getParent(), "s_" + file.getName()).delete();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  @Transactional
  @Override
  public List<String> removeWithReviewsAndMovieImages(Long mno) {
    List<MovieImage> list = movieImageRepository.findByMno(mno);
    List<String> result = new ArrayList<>();
    list.forEach(new Consumer<MovieImage>() {
      @Override
      public void accept(MovieImage t) {
        result.add(t.getPath() + File.separator + t.getUuid() + "_" + t.getImgName());
      }
    });
    movieImageRepository.deleteByMno(mno);
    reviewRepository.deleteByMno(mno);
    movieRepository.deleteById(mno);
    return result;
  }

}
