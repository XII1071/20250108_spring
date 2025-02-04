package com.example.ex6.service;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.MovieImageDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

  //  PageRequestDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
  Long register(MovieDTO movieDTO);

  default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
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

//}

//  PageRequestDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
}
