package com.example.GrpSB.model;

public class Attachment {
    private String nickname;
    private String from_date;
    private String to_date;
    private String query;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Attachment(String nickname, String from_date, String to_date, String query) {
        this.nickname = nickname;
        this.from_date = from_date;
        this.to_date = to_date;
        this.query = query;
    }
}
