package com.example.GrpSB.dao;

import com.example.GrpSB.Connection.DbConnection;
import com.example.GrpSB.Queries;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class S2Exercise1Impl implements S2Exercise1 {
    Queries queries;
    DbConnection connectionInstance = DbConnection.getInstance();

    @Override
    public List<Map<String, String>> getAccountView(String stockname) {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("S2_1A"));
            statement1.setString(1, stockname);
            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("trader",resultSet.getString("trader"));
                results.put("stock",resultSet.getString("stock"));
                results.put("amount",resultSet.getString("amount"));
                results.put("call_amount",resultSet.getString("call_amount"));
                results.put("call_limit",resultSet.getString("call_limit"));
                results.put("put_amount",resultSet.getString("put_amount"));
                results.put("put_limit",resultSet.getString("put_limit"));
                results.put("market_price",resultSet.getString("market_price"));
                results.put("market_value",resultSet.getString("market_value"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getOrdersView(String stockname) {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("S2_1B"));
            statement1.setString(1, stockname);

            ResultSet resultSet = statement1.executeQuery();

            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("stock",resultSet.getString("stock"));
                results.put("call_volume",resultSet.getString("call_volume"));
                results.put("call_backlog",resultSet.getString("call_backlog"));
                results.put("price",resultSet.getString("price"));
                results.put("put_backlog",resultSet.getString("put_backlog"));
                results.put("put_volume",resultSet.getString("put_volume"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
