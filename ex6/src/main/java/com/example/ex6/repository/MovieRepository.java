package com.example.ex6.repository;


import com.example.ex6.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/* 접근 2가지. 쿼리 메서드 접근, 쿼리 어노테이션 */
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /* Object[]를 쓴이유  복수개의 엔티티(movie, review, MovieImage)를 받기위해서 */
//    @Query("select m, avg(coalesce(r.grade, 0)), count(distinct r) " +
//        "from Movie m left outer join Review r " +
//        "on r.movie = group by m ")
        @Query("select m, avg(coalesce(r.grade, 0)), count(distinct r) " +
        "from Movie m " +
        " left outer join Review r on r.movie = group by m ")
    Page<Object[]> getListPage(Pageable pageable);
}
