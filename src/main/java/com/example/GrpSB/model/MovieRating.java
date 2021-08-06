package com.example.GrpSB.model;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
public class MovieRating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "movie_title")
    private String movieTitle;

    @Column(name = "average_rating")
    private String averageRating;

    @Column(name = "no_votes")
    private String noVotes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getNoVotes() {
        return noVotes;
    }

    public void setNoVotes(String noVotes) {
        this.noVotes = noVotes;
    }
}
