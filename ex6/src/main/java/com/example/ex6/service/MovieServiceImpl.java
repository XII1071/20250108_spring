  package com.example.ex6.service;

  import com.example.ex6.dto.MovieDTO;
  import com.example.ex6.dto.MovieImageDTO;
  import com.example.ex6.dto.PageRequestDTO;
  import com.example.ex6.dto.PageResultDTO;
  import com.example.ex6.entity.Movie;
  import com.example.ex6.entity.MovieImage;
  import com.example.ex6.repository.MovieImageRepository;
  import com.example.ex6.repository.MovieRepository;
  import jakarta.transaction.Transactional;
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
    public void addMovieImage(Long mno, MovieImageDTO movieImageDTO) {

    }

    @Override
    public void deleteMovieImage(String uuid) {

    }

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
    @Transactional
    public void modify(MovieDTO movieDTO) {
      Optional<Movie> result = movieRepository.findById(movieDTO.getMno());

      if (result.isPresent()) {
        Movie movie = result.get();
        movie.changeTitle(movieDTO.getTitle()); // 제목 변경

        // ✅ 기존 이미지 삭제 후 새로운 이미지 저장
        movieImageRepository.deleteByMovieMno(movieDTO.getMno());

        List<MovieImage> newImageList = new ArrayList<>();
        if (movieDTO.getImageDTOList() != null) {
          for (MovieImageDTO imgDTO : movieDTO.getImageDTOList()) {
            MovieImage newImage = MovieImage.builder()
                .path(imgDTO.getPath())
                .imgName(imgDTO.getImgName())
                .uuid(imgDTO.getUuid())
                .movie(movie)
                .build();
            newImageList.add(newImage);
          }
          movieImageRepository.saveAll(newImageList);
        }

        movieRepository.save(movie);
      }
    }


    @Override
      @Transactional
      public void deleteMovie(Long mno) {
        log.info("🔴 deleteMovie 호출됨: mno = " + mno);

        Optional<Movie> result = movieRepository.findById(mno);
        if (result.isPresent()) {
          Movie movie = result.get();

          // ✅ 관련 이미지 삭제
          log.info("🟡 관련 이미지 삭제 시작...");
          movieImageRepository.deleteByMovieMno(mno);
          log.info("✅ 관련 이미지 삭제 완료");

          // ✅ 영화 삭제
          movieRepository.delete(movie);
          log.info("✅ 영화 삭제 완료: " + mno);
        } else {
          throw new IllegalArgumentException("해당 영화가 존재하지 않습니다: " + mno);
        }
      }

    @Override
    public void removeMovieImagebyUUID(String uuid) {

    }
  }