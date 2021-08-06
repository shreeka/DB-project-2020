package com.example.GrpSB.repo;

import com.example.GrpSB.model.MovieAlias;
import com.example.GrpSB.model.MoviesByGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoviesByGenreRepository extends JpaRepository<MoviesByGenre, String> {

    @Query(nativeQuery = true, value ="select m.title_name, g.genre, g.average_rating, g.no_votes from movie_titles m\n" +
            "inner join (\n" +
            "select * from genre g \n" +
            "inner join ratings r\n" +
            "on g.title_id = r.movie_title\n" +
            "where g.genre = :movie_genre) g\n" +
            "on g.title_id = m.id\n" +
            "order by no_votes desc\n" +
            "limit 10")
    List<MoviesByGenre> findByMovieGenre(@Param("movie_genre") String movie_genre);
}
