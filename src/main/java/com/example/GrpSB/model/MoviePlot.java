package com.example.GrpSB.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class MoviePlot implements Serializable {

    @Id
    private String movie_title;

    @Column
    private String title_type;

    @Column(name = "plot_summary")
    private String plotSummary;

    @Column(name = "plot_synopsis")
    private String plotSynopsis;

    @Column
    private String rank;

    @Column
    private String title_name;


    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movieTitle) {
        this.movie_title = movieTitle;
    }

    public String getPlotSummary() {
        return plotSummary;
    }

    public void setPlotSummary(String plotSummary) {
        this.plotSummary = plotSummary;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getTitle_type() {
        return title_type;
    }

    public void setTitle_type(String title_type) {
        this.title_type = title_type;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
