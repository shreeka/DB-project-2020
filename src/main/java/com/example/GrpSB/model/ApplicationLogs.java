package com.example.GrpSB.model;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "application_logs")
public class ApplicationLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "search_type")
    private String searchType;

    private String time;

    @Column(name = "query_run_time")
    private Long queryRunTime;

    private String query;

    private String username;

    @Column(name = "error_id")
    private String errorId;

    @Column(name = "error_description")
    private String errorDescription;

    @Column(name = "search_keyword")
    private String searchKeyword;


    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String error_description) {
        this.errorDescription = error_description;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getQueryRunTime() {
        return queryRunTime;
    }

    public void setQueryRunTime(Long query_run_time) {
        this.queryRunTime = query_run_time;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getUsername() {
        return username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
