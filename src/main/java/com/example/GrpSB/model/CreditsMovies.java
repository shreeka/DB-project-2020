package com.example.GrpSB.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@IdClass(MovieCreditsId.class)
@Table(name = "credits")
public class CreditsMovies implements Serializable {

    @Column
    @Id
    private String movieTitle;

    @Column
    private String name;

    @Column
    @Id
    private String personId;

    @Column
    private String titleName;

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }


}
