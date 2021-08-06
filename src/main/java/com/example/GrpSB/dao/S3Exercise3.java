package com.example.GrpSB.dao;

import java.util.List;
import java.util.Map;

public interface S3Exercise3 {
    Map<String, List> getMetadata(String imdbtable);
    List<Map<String, String>> getColMetadata(String imdbtable);
    List<Map<String, String>> getPrimaryKeys(String imdbtable);
    List<Map<String, String>> getForeignKeys(String imdbtable);
}
