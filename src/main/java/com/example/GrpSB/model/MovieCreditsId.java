package com.example.GrpSB.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class MovieCreditsId implements Serializable {

    private String movieTitle;
    private String personId;

    public MovieCreditsId() {
    }

    public MovieCreditsId(String movieTitle, String personId) {
        this.movieTitle = movieTitle;
        this.personId = personId;
    }

}
