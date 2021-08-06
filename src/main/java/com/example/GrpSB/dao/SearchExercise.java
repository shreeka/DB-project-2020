package com.example.GrpSB.dao;

import com.example.GrpSB.model.ApplicationLogs;
import com.example.GrpSB.model.MoviePlot;
import org.joda.time.DateTime;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public interface SearchExercise {
    List <String> getSimilarResult(String search_keyword, String search_type, String username);
}
