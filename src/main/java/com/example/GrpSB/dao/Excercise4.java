package com.example.GrpSB.dao;

import java.util.List;
import java.util.Map;

public interface Excercise4 {
    List<Map<String, String>> getTaskA(String nickname, String from_date, String to_date, String query);
}
