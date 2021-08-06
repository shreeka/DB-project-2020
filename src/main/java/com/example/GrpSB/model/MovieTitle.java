package com.example.GrpSB.model;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "movie_titles", indexes = {@Index(name = "idx_movie_name", columnList = "title_name")})
public class MovieTitle implements Serializable {

    @Id
    private String id;

    @Column(name = "title_type")
    private String titleType;

    @Column(name = "title_name")
    private String titleName;

    @Column(name = "original_title")
    private String originalTitle;

    @Column(name = "adult_movie")
    private Integer adultMovie;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "end_year")
    private Integer endYear;

    @Column(name = "run_time")
    private Integer runTime;

    @Column(name = "genre")
    private String genre;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String title_type) {
        this.titleType = title_type;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String title_name) {
        this.titleName = title_name;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String original_title) {
        this.originalTitle = original_title;
    }

    public Integer getAdultMovie() {
        return adultMovie;
    }

    public void setAdultMovie(Integer adult_movie) {
        this.adultMovie = adult_movie;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
