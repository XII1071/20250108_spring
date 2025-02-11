package com.example.ex6.service;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.MovieImageDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.dto.PageResultDTO;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;
import com.example.ex6.repository.MovieImageRepository;
import com.example.ex6.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
  private final MovieRepository movieRepository;
  private final MovieImageRepository movieImageRepository;
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
  public void modify(MovieDTO movieDTO) {
    Optional<Movie> result = movieRepository.findById(movieDTO.getMno());
    if (result.isPresent()) {
      Movie movie = result.get();
      movie.changeTitle(movieDTO.getTitle());
        movieRepository.save(movie);

      List<MovieImageDTO> movieImageList = movieDTO.getImageDTOList();
      List<MovieImage> imgs = movieImageRepository.findAll();
      imgs.forEach(movieImage -> {
        if(Objects.equals(movie.getMno(), movieImage.getMovie().getMno()))
        {
          movieImageRepository.deleteByUuid(movieImage.getUuid());
        }
      });

      movieImageList.forEach(movieimage -> {
//        movieImageRepository.save(dtoToEntity());

      });
    }
  }

  @Override
  public void removeMovieImagebyUUID(String uuid) {
    movieImageRepository.deleteByUuid(uuid);
    movieRepository.deleteById(Long.valueOf(uuid));
  }
}