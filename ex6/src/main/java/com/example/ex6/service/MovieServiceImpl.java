package com.example.ex6.service;

/* 필요한 클래스 및 인터페이스 import */
import com.example.ex6.dto.MovieDTO;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/*
*  @Service: 이 클래스가 서비스 역할을 하는 클래스임을 나타냄 (Spring이 관리함)
*  @Log4j2: 로그를 남길 수 있도록 함 (디버깅 및 오류 확인용)
*  @RequiredArgsConstructor: final이 붙은 필드를 자동으로 생성자에서 주입해줌
*/

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

  /* 레포지토리(데이터베이스와 연결된 객체)를 주입받음 */
  private final MovieRepository movieRepository;  // 영화 정보를 다루는 레포지토리
  private final MovieImageRepository movieImageRepository;   // 영화 이미지 정보를 다루는 레포지토리

  @Override
  public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

    /* 정렬 조건 설정 (영화 번호(mno) 기준으로 내림차순 정렬) */
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("mno").descending());
    // Page<Movie> result = movieRepository.findAll(pageable);
    // Page<Object[]> result = movieRepository.getListPageImg(pageable);

    /* 데이터베이스에서 검색 조건에 맞는 영화 목록을 가져옴 */
    Page<Object[]> result = movieRepository.searchPage(
        pageRequestDTO.getType(),       // 검색 타입 (제목, 감독 등)
        pageRequestDTO.getKeyword(),    // 검색어
        pageable                        // 페이징 정보
    );

    /* Object[] 배열을 MovieDTO 객체로 변환하는 함수 정의 */
    Function<Object[], MovieDTO> fn = objects -> entityToDTO(
        (Movie) objects[0],                                           // Movie 엔티티
        (List<MovieImage>) (Arrays.asList((MovieImage) objects[1])),  // 영화 이미지 리스트
        (Double) objects[2],                                          // 평균 평점
        (Long) objects[3]                                             // 리뷰 개수
    );

    /* 변환된 데이터를 PageResultDTO 객체로 반환 */
    return new PageResultDTO<>(result, fn);
  }

  @Override
  public Long register(MovieDTO movieDTO) {

    /* DTO를 엔티티로 변환 (Map 형태로 저장) */
    Map<String, Object> entityMap = dtoToEntity(movieDTO);

    /* 변환된 영화 엔티티 가져오기 */
    Movie movie = (Movie) entityMap.get("movie");

    /* 변환된 영화 이미지 리스트 가져오기 */
    List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

    /* 영화 정보를 데이터베이스에 저장 */
    movieRepository.save(movie);

    /* 영화 이미지 정보도 데이터베이스에 저장 */
    movieImageList.forEach(movieImage -> {
      movieImageRepository.save(movieImage);
    });
    /* 저장된 영화의 고유 번호(mno) 반환 */
    return movie.getMno();
  }

  @Override
  public MovieDTO get(Long mno) {

    /* 데이터베이스에서 특정 영화의 정보를 가져옴 */
    List<Object[]> result = movieRepository.getMovieWithAll(mno);

    /* 첫 번째 결과에서 Movie 엔티티 가져오기 */
    Movie movie = (Movie) result.get(0)[0];

    /* 영화 이미지를 저장할 리스트 생성 */
    List<MovieImage> movieImages = new ArrayList<>();

    /* 결과 리스트를 돌면서 이미지 정보 추가 */
    result.forEach(new Consumer<Object[]>() {

      @Override
      public void accept(Object[] objects) {
        movieImages.add((MovieImage) objects[1]);
      }
    });
    /* 평균 평점 가져오기 */
    Double avg = (Double) result.get(0)[2];

    /* 리뷰 개수 가져오기 */
    Long reviewCnt = (Long) result.get(0)[3];

    /* 데이터를 MovieDTO 객체로 변환하여 반환 */
    return entityToDTO(movie, movieImages, avg, reviewCnt);
  }

  /* 업로드할 파일이 저장될 경로를 application.properties에서 가져옴 */
  @Value("${com.example.upload.path}")
  private String uploadPath;


}