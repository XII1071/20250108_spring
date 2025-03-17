package com.example.ex6.service;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.MovieImageDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.dto.PageResultDTO;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

  PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

  Long register(MovieDTO movieDTO);

  MovieDTO get(Long mno);

  void modify(MovieDTO movieDTO);

  List<String> removeWithReviewsAndMovieImages(Long mno);

  void removeMovieImagebyUUID(String uuid);

  default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
    System.out.println(movieDTO);
    Map<String, Object> entityMap = new HashMap<>();

    Movie movie = Movie.builder().mno(movieDTO.getMno()).title(movieDTO.getTitle()).build();

    entityMap.put("movie", movie);
    List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();
    if (imageDTOList != null && imageDTOList.size() > 0) {
      List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO -> {
        MovieImage movieImage = MovieImage.builder()
            .path(movieImageDTO.getPath())
            .imgName(movieImageDTO.getImgName())
            .uuid(movieImageDTO.getUuid())
            .movie(movie)
            .build();
        return movieImage;
      }).collect(Collectors.toList());
      entityMap.put("imgList", movieImageList);
    }
    return entityMap;
  }

  default MovieDTO entityToDTO(Movie movie, List<MovieImage> movieImages,
                                 Double avg, Long reviewCnt) {
    MovieDTO movieDTO = MovieDTO.builder()
        .mno(movie.getMno())
        .title(movie.getTitle())
        .regDate(movie.getRegDate())
        .modDate(movie.getModDate())
        .build();
    List<MovieImageDTO> movieImageDTOList = new ArrayList<>();
    if (movieImages.size() > 0 && movieImages.get(0) != null) {
      movieImageDTOList = movieImages.stream().map(movieImage -> {
        MovieImageDTO movieImageDTO = MovieImageDTO.builder()
            .imgName(movieImage.getImgName())
            .path(movieImage.getPath())
            .uuid(movieImage.getUuid())
            .build();
        return movieImageDTO;
      }).collect(Collectors.toList());
    }
    movieDTO.setImageDTOList(movieImageDTOList);
    movieDTO.setAvg(avg);
    movieDTO.setReviewCnt(reviewCnt);
    return movieDTO;
  }

}

