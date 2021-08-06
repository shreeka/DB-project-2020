package com.example.GrpSB.controller;

import com.example.GrpSB.model.*;
import com.example.GrpSB.service.ExcerciseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/worksheet3")
public class Excercise3 {

    private final ExcerciseService excerciseService;
    private static final Logger logger = LogManager.getLogger(Excercise3.class);

    public Excercise3(ExcerciseService excerciseService) {
        this.excerciseService = excerciseService;
    }

    @PostMapping(value = "/excercise")
    public String getPostExcecise(@RequestBody Excercise excercise) {
        String path = MessageFormat.format("redirect:/{0}/ex{1}", excercise.getWorksheet(),
                excercise.getTask());
        return path;
    }

    @PostMapping(value = "/movie_title")
    public String getMovieTitle(@RequestBody MovieTitle movieTitle, Model model) {
        logger.info("Initiating Movie Title Search");
        DateTime dt = new DateTime();
        String titleName = movieTitle.getTitleName();

        List<String> similar_result;
        List<String> similar_alias;
        Long startTime = System.currentTimeMillis();

        List<MovieTitle> movie_title = excerciseService.getMovieByTitle(titleName);
        Long elapsedTime = System.currentTimeMillis() - startTime;
        excerciseService.LogMovieByTitle(titleName, dt.toString(), elapsedTime);

        if (movie_title.isEmpty()) {
            List<MovieAlias> movieAlias = excerciseService.getMovieByAliasTitle(titleName);
            if (movieAlias.isEmpty()) {
                elapsedTime = System.currentTimeMillis() - startTime;
                excerciseService.LogMovieByTitle(titleName, dt.toString(), elapsedTime);

                //Get similar queries from both alias and movie title
                similar_alias = excerciseService.getSimilarResult(titleName, "movie_alias");
                for (String key : similar_alias) {
                    List<MovieAlias> simMovieAlias = excerciseService.getMovieByAliasTitle(key);
                    String simAliasId = simMovieAlias.get(0).getTitleId();
                    excerciseService.getMovieById(simAliasId);
                }
                similar_result = excerciseService.getSimilarResult(titleName,"movie_title");
                for (String key: similar_result){
                    excerciseService.getMovieByTitle(key);
                }
                similar_result.addAll(similar_alias);

                model.addAttribute("movies", movie_title);
                model.addAttribute("similar_movie", similar_result);
                model.addAttribute("user_query", titleName);
                return "movies_by_title";
            }

            String id = movieAlias.get(0).getTitleId();
            movie_title = excerciseService.getMovieById(id);
            elapsedTime = System.currentTimeMillis() - startTime;
            excerciseService.LogMovieById(id, titleName, dt.toString(), elapsedTime);
        }
        //Get similar queries from both alias and movie title
        similar_alias = excerciseService.getSimilarResult(titleName, "movie_alias");
        for (String key : similar_alias) {
            List<MovieAlias> simMovieAlias = excerciseService.getMovieByAliasTitle(key);
            String simAliasId = simMovieAlias.get(0).getTitleId();
            excerciseService.getMovieById(simAliasId);
        }
        similar_result = excerciseService.getSimilarResult(titleName,"movie_title");
        for (String key: similar_result){
            excerciseService.getMovieByTitle(key);
        }
        similar_result.addAll(similar_alias);

        model.addAttribute("movies", movie_title);
        model.addAttribute("similar_movie", similar_result);
        model.addAttribute("user_query", titleName);
        return "movies_by_title";
    }

    @PostMapping(value = "/genre")
    public String getMovieByGenre(@RequestBody MoviesByGenre moviesByGenre, Model model) {
        logger.info("Initiating Genres Search");
        DateTime dt = new DateTime();
        String genreName = moviesByGenre.getGenre();
        Long startTime = System.currentTimeMillis();

        List<MoviesByGenre> movieGenres = excerciseService.getMovieByGenre(genreName);
        Long elapsedTime = System.currentTimeMillis() - startTime;
        excerciseService.movieByGenreLogs(genreName, dt.toString(), elapsedTime);
        //Get similar queries
        List<String> similar_result = excerciseService.getSimilarResult(genreName, "movie_genre");
        for (String key : similar_result) {
            excerciseService.getMovieByGenre(key);
        }
        model.addAttribute("genre", movieGenres);
        model.addAttribute("similar_genre", similar_result);
        model.addAttribute("user_query", genreName);
        return "movies_by_genre";
    }

    @PostMapping(value = "/credits")
    public String getMovieByCreditname(@RequestBody CreditsMovies creditsMovies, Model model) {
        logger.info("Initiating credits Search");
        DateTime dt = new DateTime();
        String castName = creditsMovies.getName();
        Long startTime = System.currentTimeMillis();

        List<CreditsMovies> creditsMoviesList = excerciseService.getMoviesByCredit(castName);
        Long elapsedTime = System.currentTimeMillis() - startTime;
        excerciseService.moviesByCreditLogs(castName, dt.toString(), elapsedTime);
        //Get similar queries
        List<String> similar_result = excerciseService.getSimilarResult(castName, "movie_cast_technician");
        for (String key : similar_result) {
            excerciseService.getMoviesByCredit(key);
        }
        model.addAttribute("credits", creditsMoviesList);
        model.addAttribute("similar_credit", similar_result);
        model.addAttribute("user_query", castName);
        return "movies_by_credits";
    }

    @PostMapping(value = "/plotSummary")
    public String getMovieByPlot(@RequestBody MoviePlot moviePlot, Model model) {
        logger.info("Initiating Plot Summary Search");
        DateTime dt = new DateTime();
        Long startTime = System.currentTimeMillis();
        String ts_query = moviePlot.getPlotSummary().trim();
        ts_query = ts_query.replaceAll("\\s+", "&");

        List<MoviePlot> moviePlotList = excerciseService.getMovieByPlotSummary(ts_query);
        Long elapsedTime = System.currentTimeMillis() - startTime;
        //Log to table
        excerciseService.movieByPlotSummaryLogs(ts_query, dt.toString(), elapsedTime);
        //Get similar queries
        List<String> similar_result = excerciseService.getSimilarResult(ts_query, "movie_plot");
        for (String key : similar_result) {
            excerciseService.getMovieByPlotSummary(key.replaceAll("\\s+", "&"));
        }
        model.addAttribute("plot", moviePlotList);
        model.addAttribute("similar_plot", similar_result);
        model.addAttribute("user_query", ts_query.replaceAll("&", " "));
        return "movies_by_plot_summary";
    }

    @PostMapping(value = "/showImdbdata")
    public String showMetadataResult(@RequestBody Imdb imdb, Model model) {
        Map<String, List> imdb_metadata = excerciseService.getMetadata(imdb.getImdbtable());
        model.addAttribute("IMDB_cols", imdb_metadata.get("columns"));
        model.addAttribute("IMDB_pk", imdb_metadata.get("primary_keys"));
        model.addAttribute("IMDB_fk", imdb_metadata.get("foreign_keys"));
        model.addAttribute("IMDB_table", imdb.getImdbtable());
        return "S3Ex3Result";
    }

    @GetMapping(value = "/ex4")
    public String getEx3() {
        return "S3Ex2";
    }

    @GetMapping(value = "/ex3")
    public String showS3form() {
        return "S3Ex3";
    }
}
