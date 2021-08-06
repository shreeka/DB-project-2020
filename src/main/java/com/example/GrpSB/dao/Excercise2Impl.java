package com.example.GrpSB.dao;

import com.example.GrpSB.Connection.DbConnection;
import com.example.GrpSB.Queries;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("postgres")
public class Excercise2Impl implements Excercise2 {

    Queries queries;
    DbConnection connectionInstance = DbConnection.getInstance();

    @Override
    public List<Map<String, String>> getTaskA() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queries.getQuery("2A"));
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("nickname",resultSet.getString("nickname"));
                results.put("address",resultSet.getString("addr"));
                result.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getTaskB() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queries.getQuery("2B"));
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("user_type",resultSet.getString("user_type"));
                results.put("count",resultSet.getString("count"));
                result.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getTaskC() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queries.getQuery("2C"));
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("firstletter",resultSet.getString("first_letter"));
                results.put("frequency",resultSet.getString("frequency"));
                result.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getTaskD() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queries.getQuery("2D"));
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("nickname",resultSet.getString("nickname"));
                result.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getTaskE() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queries.getQuery("2E"));
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("nickname",resultSet.getString("nickname"));
                results.put("month_birth",resultSet.getString("month_birth"));
                results.put("day_birth",resultSet.getString("day_birth"));
                result.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getTaskF() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queries.getQuery("2F"));
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("address",resultSet.getString("adid"));
                results.put("username",resultSet.getString("username"));
                results.put("avg_response_time",resultSet.getString("avg_response_time"));
                results.put("dense_rank",resultSet.getString("dense_rank"));
                result.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getTaskG() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queries.getQuery("2G"));
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("address",resultSet.getString("adid"));
                results.put("username",resultSet.getString("username"));
                results.put("avg_response_time",resultSet.getString("avg_response_time"));
                results.put("rank",resultSet.getString("rank"));
                result.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getTaskH() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queries.getQuery("2H"));
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("thread_id",resultSet.getString("thread_id"));
                results.put("email_id",resultSet.getString("email_id"));
                results.put("username",resultSet.getString("username"));
                results.put("in_reply_to",resultSet.getString("in_reply_to"));
                results.put("depth",resultSet.getString("depth"));
                results.put("date",resultSet.getString("date"));
                result.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, List> getAllTasks() {
        Map<String,List> result = new HashMap<>();
        result.put("A",getTaskA());
        result.put("B",getTaskB());
        result.put("C",getTaskC());
        result.put("D",getTaskD());
        result.put("E",getTaskE());
        result.put("F",getTaskF());
        result.put("G",getTaskG());
        result.put("H",getTaskH());
        return result;
    }

}
