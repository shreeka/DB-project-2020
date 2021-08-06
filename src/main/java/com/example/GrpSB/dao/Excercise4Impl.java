package com.example.GrpSB.dao;

import com.example.GrpSB.Connection.DbConnection;
import com.example.GrpSB.Queries;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class Excercise4Impl implements Excercise4{

    Queries queries;
    DbConnection connectionInstance = DbConnection.getInstance();

    @Override
    public List<Map<String, String>> getTaskA(String nickname, String from_date, String to_date, String query) {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            Statement statement = connection.createStatement();
            statement.executeUpdate(queries.getQuery("4A"));
            statement.executeUpdate(queries.getQuery("4B"));

            PreparedStatement statement1 = connection.prepareStatement("select * from getAttachments (cast( ? as varchar )," +
                    "cast (? as date),cast(? as date ),cast(? as varchar ))");
            statement1.setString(1,nickname);
            statement1.setString(2, from_date);
            statement1.setString(3, to_date);
            statement1.setString(4,query);

            ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("filename",resultSet.getString("filename"));
                results.put("subject",resultSet.getString("subject"));
                results.put("_from",resultSet.getString("_from"));
                results.put("address",resultSet.getString("address"));
                results.put("date",resultSet.getString("date"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
