package com.example.GrpSB.dao;

import com.example.GrpSB.Connection.DbConnection;
import com.example.GrpSB.Queries;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class S4Excercise4Impl implements S4Excercise4 {

    Queries queries;
    DbConnection connectionInstance = DbConnection.getInstance();

    @Override
    public List<Map<String, String>> getMostPopularPickupLocations() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("S4_4A"));
            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("no_rides",resultSet.getString("no_rides"));
                results.put("borough",resultSet.getString("borough"));
                results.put("zone",resultSet.getString("zone"));
                result.add(results);
            }

        } catch (SQLException e) {
                e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getMostPopularDropOffLocations() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("S4_4B"));
            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("no_rides",resultSet.getString("no_rides"));
                results.put("borough",resultSet.getString("borough"));
                results.put("zone",resultSet.getString("zone"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getMostPopularPaymentMethods() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("S4_4C"));
            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("popular_payment",resultSet.getString("popular_payment"));
                results.put("payment_method",resultSet.getString("payment_method"));
                results.put("rank",resultSet.getString("rank"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getEstimatedPriceForaLocation() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("S4_4D"));
            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("average_trip_amount",resultSet.getString("average_trip_ammount"));
                results.put("borough",resultSet.getString("borough"));
                results.put("zone",resultSet.getString("zone"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getMostCongestedLocations() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("S4_4E"));
            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("rank",resultSet.getString("rank"));
                results.put("borough",resultSet.getString("borough"));
                results.put("zone",resultSet.getString("zone"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getMostPopularDayoftheweekforRides() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("S4_4F"));
            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("day_of_the_week",resultSet.getString("day_of_the_week"));
                results.put("no_trips",resultSet.getString("no_trips"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getAvergaeTipAMountByLocation() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("S4_4G"));
            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("tip_amount",resultSet.getString("tip_amount"));
                results.put("borough",resultSet.getString("borough"));
                results.put("zone",resultSet.getString("zone"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getAvergaeTollAMountByLocation() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("S4_4I"));
            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("average_toll_amount",resultSet.getString("average_toll_ammount"));
                results.put("borough",resultSet.getString("borough"));
                results.put("zone",resultSet.getString("zone"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getMostPopularHvfhService() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("S4_4H"));
            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("no_rides",resultSet.getString("no_rides"));
                results.put("name",resultSet.getString("name"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getMostPopularRideSharingService() {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("S4_4J"));
            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("no_rides",resultSet.getString("no_rides"));
                results.put("name",resultSet.getString("name"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
