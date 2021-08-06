package com.example.GrpSB.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class MoviesByGenre implements Serializable {
    @Id
    private String title_name;

    @Column
    private String average_rating;

    @Column
    private String no_votes;

    @Column
    private String genre;

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public String getNo_votes() {
        return no_votes;
    }

    public void setNo_votes(String no_votes) {
        this.no_votes = no_votes;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
