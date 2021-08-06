package com.example.GrpSB.repo;

import com.example.GrpSB.model.CreditsMovies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoviesByCreditRepository extends JpaRepository<CreditsMovies, String> {

    @Query(nativeQuery = true, value ="select a.movie_title, a.person_id, m.title_name, p.name from credits a inner join\n" +
            "(select c.person_id, p.name, c.movie_title, r.average_rating\n" +
            "from credits c\n" +
            "inner join (select * from person where person.name = :person_name) p \n" +
            "on c.person_id = p.id\n" +
            "inner join ratings r on\n" +
            "c.movie_title = r.movie_title\n" +
            "order by r.average_rating desc\n" +
            "limit 10) c\n" +
            "on a.movie_title = c.movie_title\n" +
            "inner join movie_titles m\n" +
            "on m.id = a.movie_title\n" +
            "inner join person p\n" +
            "on a.person_id = p.id")
    List<CreditsMovies> findByCreditName(@Param("person_name") String person_name);
}
