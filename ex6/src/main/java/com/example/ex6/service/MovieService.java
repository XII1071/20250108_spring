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

/*
 * 영화(Movie)와 관련된 비즈니스 로직을 처리하기 위한 서비스 인터페이스입니다.
 * 목록 조회, 등록, 단일 조회 등의 메서드를 정의하고,
 * 엔티티 ↔ DTO 변환 로직을 default 메서드로 제공하여
 * 구현 클래스(MovieServiceImpl)에서 재사용하도록 구성합니다.
 */

public interface MovieService {

  PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

  Long register(MovieDTO movieDTO);

  MovieDTO get(Long mno);

  void removeMovieImagebyUUID(String uuid);
  default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
    System.out.println(movieDTO);
    Map<String, Object> entityMap = new HashMap<>();

    /* 1) Movie 엔티티 변환 (필요한 필드만 우선 세팅) */
    Movie movie = Movie.builder().mno(movieDTO.getMno()).title(movieDTO.getTitle()).build();

    // entityMap에 "movie"라는 키로 Movie 엔티티 추가
    entityMap.put("movie", movie);

    /* 2) 이미지 리스트(MovieImageDTO) -> MovieImage 엔티티 리스트 */
    List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();
    if (imageDTOList != null && imageDTOList.size() > 0) {
      List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO -> {
        // MovieImageDTO -> MovieImage 엔티티 변환
        MovieImage movieImage = MovieImage.builder()
            .path(movieImageDTO.getPath())
            .imgName(movieImageDTO.getImgName())
            .uuid(movieImageDTO.getUuid())
            .movie(movie)   // Movie와 연관관계 설정
            .build();
        return movieImage;
      }).collect(Collectors.toList());

      // entityMap에 "imgList"라는 키로 이미지 엔티티 리스트 추가
      entityMap.put("imgList", movieImageList);
    }
    return entityMap;
  }

  default MovieDTO entityToDTO(Movie movie, List<MovieImage> movieImages,
                               Double avg, Long reviewCnt) {
    /* 1) Movie 엔티티 -> MovieDTO로 기본 정보 세팅 */
    MovieDTO movieDTO = MovieDTO.builder()
        .mno(movie.getMno())
        .title(movie.getTitle())
        .regDate(movie.getRegDate())
        .modDate(movie.getModDate())
        .build();

    /* 2) MovieImage 엔티티 리스트 -> MovieImageDTO 리스트 */
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
    /* 3) DTO에 이미지 리스트, 평점, 리뷰 개수 세팅 */
    movieDTO.setImageDTOList(movieImageDTOList);
    movieDTO.setAvg(avg);
    movieDTO.setReviewCnt(reviewCnt);
    return movieDTO;
  }

}