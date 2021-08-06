package com.example.GrpSB.dao;

import com.example.GrpSB.Connection.DbConnection;
import com.example.GrpSB.Queries;
import com.example.GrpSB.model.MoviePlot;
import com.example.GrpSB.repo.PlotSummaryRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.management.Query;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SearchExerciseImpl implements SearchExercise{
    Queries queries;
    DbConnection connectionInstance = DbConnection.getInstance();

    @Override
    public List<String> getSimilarResult(String searchKey, String searchType, String userName) {
        List<String> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            PreparedStatement statement1 = connection.prepareStatement(queries.getQuery("sim_result"));
            statement1.setString(1, searchKey);
            statement1.setString(2, userName);
            statement1.setString(3, searchType);
            statement1.setString(4, searchKey);
            ResultSet resultSet = statement1.executeQuery();

            while (resultSet.next()){
                result.add(resultSet.getString("search_keyword"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
