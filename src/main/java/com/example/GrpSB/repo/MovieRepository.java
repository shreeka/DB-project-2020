package com.example.GrpSB.repo;

import com.example.GrpSB.model.CreditsMovies;
import com.example.GrpSB.model.MovieTitle;
import com.example.GrpSB.model.MoviesByGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MovieRepository extends JpaRepository<MovieTitle, String> {

    @Query(nativeQuery = true, value ="select m.id, m.title_type, m.title_name, m.original_title, m.adult_movie,\n" +
            "r.release_year, r.end_year, g.genre, r.run_time\n" +
            "from movie_titles m inner join release_information r\n" +
            "on r.title_id = m.id\n" +
            "inner join genre g on g.title_id = m.id\n" +
            "where m.title_name = :title_name")
    List<MovieTitle> findByTitleName(@Param("title_name") String title_name);

    @Query(nativeQuery = true, value ="select m.id, m.title_type, m.title_name, m.original_title, m.adult_movie,\n" +
            "r.release_year, r.end_year, g.genre, r.run_time\n" +
            "from movie_titles m inner join release_information r\n" +
            "on r.title_id = m.id\n" +
            "inner join genre g on g.title_id = m.id\n" +
            "where m.id = :id")
    List<MovieTitle> findByTitleId(@Param("id") String id);

}
