package com.example.GrpSB.dao;

import java.util.List;
import java.util.Map;

public interface S2Exercise1 {
    List<Map<String, String>> getAccountView(String stockname);
    List<Map<String, String>> getOrdersView(String stockname);
}
