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

@Repository
public class Excercise3Impl implements Excercise3{

    Queries queries;
    DbConnection connectionInstance = DbConnection.getInstance();


    @Override
    public List<Map<String, String>> getTaskA(Long euserid) {
        List<Map<String,String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            Statement statement = connection.createStatement();
            statement.executeUpdate(queries.getQuery("3A"));
            statement.executeUpdate(queries.getQuery("3B"));
            statement.executeUpdate(queries.getQuery("3C"));
            ResultSet resultSet = statement.executeQuery("Select * from addressbook("+euserid+") " +
                                                             "order by username asc");
            while (resultSet.next()){
                Map<String,String> results = new HashMap<>();
                results.put("username",resultSet.getString("username"));
                results.put("emailid",resultSet.getString("emailid"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
