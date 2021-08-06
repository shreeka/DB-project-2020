package com.example.GrpSB.dao;

import java.util.List;
import java.util.Map;

public interface S4Excercise4 {
    List<Map<String, String>> getMostPopularPickupLocations();
    List<Map<String, String>> getMostPopularDropOffLocations();
    List<Map<String, String>> getMostPopularPaymentMethods();
    List<Map<String, String>> getEstimatedPriceForaLocation();
    List<Map<String, String>> getMostCongestedLocations();
    List<Map<String, String>> getMostPopularDayoftheweekforRides();
    List<Map<String, String>> getAvergaeTipAMountByLocation();
    List<Map<String, String>> getAvergaeTollAMountByLocation();
    List<Map<String, String>> getMostPopularHvfhService();
    List<Map<String, String>> getMostPopularRideSharingService();
}
