package com.example.GrpSB.service;


import com.example.GrpSB.dao.*;
import com.example.GrpSB.dao.Excercise2;
import com.example.GrpSB.dao.Excercise3;
import com.example.GrpSB.dao.Excercise4;
import com.example.GrpSB.dao.S2Exercise1;
import com.example.GrpSB.model.*;
import com.example.GrpSB.repo.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ExcerciseService {

    @Value("${4D}")
    private String plotSummaryQuery;

    @Value("${4A}")
    private String movieNameQuery;

    @Value("${4A_1}")
    private String movieAliasQuery;

    @Value("${4B}")
    private String movieCreditsQuery;

    @Value("${4C}")
    private String movieGenreQuery;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Excercise2 excercise2;
    private final Excercise3 excercise3;
    private final Excercise4 excercise4;
    private final S2Exercise1 s2Exercise1;
    private final S3Exercise3 s3Exercise3;
    private final S4Excercise4 s4Exercise4;
    private final MovieRepository movieRepository;
    private final AliasRepository aliasRepository;
    private final MoviesByCreditRepository moviesByCreditRepository;
    private final MoviesByGenreRepository moviesByGenreRepository;
    private final PlotSummaryRepository plotSummaryRepository;
    private final LogsRepository logsRepository;
    private final TestRepository testRepository;
    private final UserRepository userRepository;
    private final SearchExercise searchExercise;

    @Autowired
    public ExcerciseService(@Qualifier("postgres") Excercise2 excercise2, Excercise3 excercise3,
                            Excercise4 excercise4, S2Exercise1 s2Exercise1, S3Exercise3 s3Exercise3, S4Excercise4 s4Exercise4, MovieRepository movieRepository,
                            AliasRepository aliasRepository, MoviesByCreditRepository moviesByCreditRepository,
                            MoviesByGenreRepository moviesByGenreRepository, PlotSummaryRepository plotSummaryRepository,
                            LogsRepository logsRepository, TestRepository testRepository, UserRepository userRepository, SearchExercise searchExercise) {
        this.excercise2 = excercise2;
        this.excercise3 = excercise3;
        this.excercise4 = excercise4;
        this.s2Exercise1 = s2Exercise1;
        this.s3Exercise3 = s3Exercise3;
        this.s4Exercise4 = s4Exercise4;
        this.movieRepository = movieRepository;
        this.aliasRepository = aliasRepository;
        this.moviesByCreditRepository = moviesByCreditRepository;
        this.moviesByGenreRepository = moviesByGenreRepository;
        this.plotSummaryRepository = plotSummaryRepository;
        this.logsRepository = logsRepository;
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.searchExercise = searchExercise;
    }

    public Map<String, List> getTask2() {
        return excercise2.getAllTasks();
    }

    public List<Map<String, String>> getAddressbook(Long euserid) {
        return excercise3.getTaskA(euserid);
    }

    public List<Map<String, String>> getTask4(String nickname, String from_date, String to_date, String query) {
        return excercise4.getTaskA(nickname, from_date, to_date, query);
    }

    public List<Map<String, String>> getAccountView(String stockname) {
        return s2Exercise1.getAccountView(stockname);
    }

    public List<Map<String, String>> getOrdersView(String stockname) {
        return s2Exercise1.getOrdersView(stockname);
    }

    public Map<String, List> getMetadata(String imdbtable){
        return s3Exercise3.getMetadata(imdbtable);
    }

    @Cacheable(value = "MovieTitle", key = "#title_name")
    public List<MovieTitle> getMovieByTitle(String title_name) {
        List<MovieTitle> movieTitleList = movieRepository.findByTitleName(title_name);
        return movieTitleList;
    }

    public void LogMovieByTitle(String title_name, String time, Long elapsedTime) {
        ApplicationLogs applicationLogs = new ApplicationLogs();
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String query = MessageFormat.format(movieNameQuery, title_name);

        applicationLogs.setQuery(query);
        applicationLogs.setQueryRunTime(elapsedTime);
        applicationLogs.setSearchType("movie_title");
        applicationLogs.setTime(time);
        applicationLogs.setUsername(user.getUsername());
        applicationLogs.setSearchKeyword(title_name);

        logsRepository.save(applicationLogs);
    }

    @Cacheable(value = "MovieTitle", key = "#id")
    public List<MovieTitle> getMovieById(String id) {
        List<MovieTitle> movieTitleList = movieRepository.findByTitleId(id);
        return movieTitleList;
    }

    public void LogMovieById(String id, String title_search, String time, Long elapsedTime) {
        ApplicationLogs applicationLogs = new ApplicationLogs();
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String query = MessageFormat.format(movieAliasQuery, id);

        applicationLogs.setQuery(query);
        applicationLogs.setQueryRunTime(elapsedTime);
        applicationLogs.setSearchType("movie_alias");
        applicationLogs.setTime(time);
        applicationLogs.setUsername(user.getUsername());
        applicationLogs.setSearchKeyword(title_search);

        logsRepository.save(applicationLogs);
    }

    public List<MovieAlias> getMovieByAliasTitle(String title_name) {
        return aliasRepository.findByAliasTitle(title_name);
    }

    @Cacheable(value = "MoviesByGenre", key = "#genre")
    public List<MoviesByGenre> getMovieByGenre(String genre){
        List<MoviesByGenre> moviesByGenreList = moviesByGenreRepository.findByMovieGenre(genre);
        return moviesByGenreList;
    }

    public void movieByGenreLogs(String genre,String time,Long elapsedTime){
        ApplicationLogs applicationLogs = new ApplicationLogs();
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String query = MessageFormat.format(movieGenreQuery, genre);
        applicationLogs.setQuery(query);
        applicationLogs.setQueryRunTime(elapsedTime);
        applicationLogs.setSearchType("movie_genre");
        applicationLogs.setTime(time);
        applicationLogs.setUsername(user.getUsername());
        applicationLogs.setSearchKeyword(genre);

        logsRepository.save(applicationLogs);
    }

    @Cacheable(value = "CreditsMovies", key = "#credit")
    public List<CreditsMovies> getMoviesByCredit(String credit){
        List<CreditsMovies> creditsMoviesList = moviesByCreditRepository.findByCreditName(credit);
        return creditsMoviesList;
    }

    public void moviesByCreditLogs(String credit,String time,Long elapsedTime){
        ApplicationLogs applicationLogs = new ApplicationLogs();
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String query = MessageFormat.format(movieCreditsQuery,credit);

        applicationLogs.setQuery(query);
        applicationLogs.setQueryRunTime(elapsedTime);
        applicationLogs.setSearchType("movie_cast_technician");
        applicationLogs.setTime(time);
        applicationLogs.setUsername(user.getUsername());
        applicationLogs.setSearchKeyword(credit);

        logsRepository.save(applicationLogs);
    }

    @Cacheable(value = "MoviePlot", key = "#plot_summary")
    public List<MoviePlot> getMovieByPlotSummary(String plot_summary) {
        List<MoviePlot> result = plotSummaryRepository.findByMoviePlotSummary(plot_summary);
        return result;
    }

    public void movieByPlotSummaryLogs (String plot_summary,String time,Long elapsedTime){

        ApplicationLogs applicationLogs = new ApplicationLogs();
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String query = MessageFormat.format(plotSummaryQuery, plot_summary, plot_summary);
        plot_summary = plot_summary.replaceAll("&"," ");
        applicationLogs.setQuery(query);
        applicationLogs.setQueryRunTime(elapsedTime);
        applicationLogs.setSearchType("movie_plot");
        applicationLogs.setTime(time);
        applicationLogs.setUsername(user.getUsername());
        applicationLogs.setSearchKeyword(plot_summary);

        logsRepository.save(applicationLogs);
    }

    public void postTestValues(String testvar) {
        TestModel testModel = new TestModel();
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        testModel.setId(uuid);
        testModel.setTest(testvar);
        testRepository.save(testModel);
    }

    public List<TestModel> getTestValues() {
        List<TestModel> testModel = (List<TestModel>) testRepository.findAll();
        return testModel;
    }

    public ResponseEntity<String> createUsers() {

            DateTime dt = new DateTime();

            User user1 = new User("user", passwordEncoder.encode("user123"), "USER", "user@mail.com", dt.toString());
            User user2 = new User("user1", passwordEncoder.encode("user123"), "USER", "user1@mail.com", dt.toString());

            List<User> users = Arrays.asList(user1, user2);
            userRepository.saveAll(users);

            return new ResponseEntity<>("Default user Created", HttpStatus.CREATED);

    }

    public List <String> getSimilarResult (String search_keyword, String search_type) {
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return searchExercise.getSimilarResult(search_keyword, search_type, user.getUsername());
    }

    public List<Map<String,String>> getMostPopularPickupLocations() {
        return s4Exercise4.getMostPopularPickupLocations();
    }

    public List<Map<String,String>> getMostPopularDropOffLocations() {
        return s4Exercise4.getMostPopularDropOffLocations();
    }

    public List<Map<String,String>> getMostPopularPaymentMethods() {
        return s4Exercise4.getMostPopularPaymentMethods();
    }

    public List<Map<String,String>> getEstimatedPriceForaLocation() {
        return s4Exercise4.getEstimatedPriceForaLocation();
    }

    public List<Map<String,String>> getMostCongestedLocations() {
        return s4Exercise4.getMostCongestedLocations();
    }

    public List<Map<String,String>> getMostPopularDayoftheweekforRides() {
        return s4Exercise4.getMostPopularDayoftheweekforRides();
    }

    public List<Map<String,String>> getAvergaeTipAMountByLocation() {
        return s4Exercise4.getAvergaeTipAMountByLocation();
    }

    public List<Map<String,String>> getAvergaeTollAMountByLocation() {
        return s4Exercise4.getAvergaeTollAMountByLocation();
    }

    public List<Map<String,String>> getMostPopularHvfhService() {
        return s4Exercise4.getMostPopularHvfhService();
    }

    public List<Map<String,String>> getMostPopularRideSharingService() {
        return s4Exercise4.getMostPopularRideSharingService();
    }
}
