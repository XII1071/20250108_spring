package com.example.ex6.repository;

import com.example.ex6.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/* 접근 2가지. 쿼리 메서드 접근, 쿼리 어노테이션 */
public interface MovieRepository extends JpaRepository<Movie, Long> {

  // 영화에 대한 리뷰의 평점과 댓글 갯수를 출력
  @Query("select m, avg(coalesce(r.grade,0)), count(distinct r) " +
      "from Movie m left outer join Review r " +
      "on r.movie = m group by m ")
  Page<Object[]> getListPage(Pageable pageable);
  /* Object[]를 쓴이유  복수개의 엔티티(movie, review, MovieImage)를 받기위해서 */

  // 영화와 영화이미지,리뷰의 평점과 댓글 갯수 출력
  @Query("select m, mi, avg(coalesce(r.grade,0)), count(distinct r) " +
          "from Movie m " +
          "left outer join MovieImage mi on mi.movie = m " +
          "left outer join Review      r on r.movie  = m group by m ")
  Page<Object[]> getListPageImg(Pageable pageable);

  // 영화와 영화이미지,리뷰의 평점과 댓글 갯수 출력
  // spring 3.x 버전 이상은 실행 안됨
  @Query("select m, max(mi), avg(coalesce(r.grade,0)), count(distinct r) " +
          "from Movie m " +
          "left outer join MovieImage mi on mi.movie = m " +
          "left outer join Review      r on r.movie  = m group by m ")
  Page<Object[]> getListPageMaxImg(Pageable pageable);

  // Native Query = SQL
  @Query(value = "select m.mno, mi.inum, mi.img_name, " +
          "avg(coalesce(r.grade, 0)), count(r.reviewnum) " +
          "from db7.movie_image mi left outer join db7.movie m on m.mno=mi.movie_mno " +
          "left outer join db7.review r on m.mno=r.movie_mno " +
          "where mi.inum = " +
          "(select max(inum) from db7.movie_image mi2 where mi2.movie_mno=m.mno) " +
          "group by m.mno ", nativeQuery = true)
  Page<Object[]> getListPageImgNative(Pageable pageable);

  // JPQL
  @Query("select m, mi, avg(coalesce(r.grade, 0)), count(distinct r) from Movie m " +
          "left outer join MovieImage mi on mi.movie = m " +
          "left outer join Review     r  on r.movie  = m " +
          "where inum = (select max(mi2.inum) from MovieImage mi2 where mi2.movie=m) " +
          "group by m ")
  Page<Object[]> getListPageImgJPQL(Pageable pageable);

  @Query("select movie, max(mi.inum) from MovieImage mi group by movie")
  Page<Object[]> getMaxQuery(Pageable pageable);

  @Query("select m, mi, avg(coalesce(r.grade, 0)), count(r) " +
          "from Movie m left outer join MovieImage mi on mi.movie=m " +
          "left outer join Review r on r.movie = m " +
          "where m.mno = :mno group by mi ")
  List<Object[]> getMovieWithAll(Long mno); //특정 영화 조회

}
