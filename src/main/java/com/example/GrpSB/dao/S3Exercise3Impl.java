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
public class S3Exercise3Impl implements S3Exercise3 {
    Queries queries;
    DbConnection connectionInstance = DbConnection.getInstance();

    @Override
    public List<Map<String, String>> getColMetadata(String imdbtable) {
        List<Map<String, String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet columns = databaseMetaData.getColumns(null, null, imdbtable, null);

            while (columns.next()) {
                Map<String, String> results = new HashMap<>();
                results.put("column_name", columns.getString("COLUMN_NAME"));
                results.put("data_type", columns.getString("TYPE_NAME"));
                results.put("column_size", columns.getString("COLUMN_SIZE"));
                results.put("decimal_digits", columns.getString("DECIMAL_DIGITS"));
                results.put("is_autoIncrement", columns.getString("IS_AUTOINCREMENT"));
                result.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getPrimaryKeys(String imdbtable) {
        List<Map<String, String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet PK = databaseMetaData.getPrimaryKeys(null,null, imdbtable);

            while (PK.next()) {
                Map<String, String> results = new HashMap<>();
                results.put("primary_key_column", PK.getString("COLUMN_NAME"));
                results.put("primary_key_name", PK.getString("PK_NAME"));
                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getForeignKeys(String imdbtable) {
        List<Map<String, String>> result = new ArrayList<>();
        try {
            Connection connection = connectionInstance.getConnection();

            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet FK = databaseMetaData.getImportedKeys(null, null, imdbtable);

            while (FK.next()) {
                Map<String, String> results = new HashMap<>();
                results.put("primary_key_table", FK.getString("PKTABLE_NAME"));
                results.put("primary_key_column", FK.getString("PKCOLUMN_NAME"));
                results.put("foreign_key_column", FK.getString("FKCOLUMN_NAME"));
                results.put("foreign_key_name", FK.getString("FK_NAME"));

                result.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, List> getMetadata(String imdbtable) {
        Map<String,List> result = new HashMap<>();
        result.put("columns",getColMetadata(imdbtable));
        result.put("primary_keys",getPrimaryKeys(imdbtable));
        result.put("foreign_keys",getForeignKeys(imdbtable));
        return result;
    }
}

