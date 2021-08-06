package com.example.GrpSB.controller;

import com.example.GrpSB.model.Excercise;
import com.example.GrpSB.model.Stock;
import com.example.GrpSB.service.ExcerciseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/worksheet2")
public class Excercise2 {

    private ExcerciseService excerciseService;

    public Excercise2(ExcerciseService excerciseService) {
        this.excerciseService = excerciseService;
    }

    @PostMapping(value="/excercise")
    public String getPostExcecise(@RequestBody Excercise excercise) {
        String path = MessageFormat.format("redirect:/{0}/ex{1}",excercise.getWorksheet(),
                excercise.getTask());
        return path;
    }

    @GetMapping(value = "/ex1")
    public String showS2form() {
        return "S2Ex1";
    }

    @PostMapping(value="/showViews")
    public String showViewsResult(@RequestBody Stock stock, Model model) {
        List<Map<String,String>> accountv = excerciseService.getAccountView(stock.getStockname());
        List<Map<String,String>> ordersv = excerciseService.getOrdersView(stock.getStockname());
        model.addAttribute("Account_v", accountv);
        model.addAttribute("Orders_v", ordersv);
        return "S2Ex1Result";
    }


}
