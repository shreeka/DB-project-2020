package com.example.GrpSB.controller;

import com.example.GrpSB.dao.UserPrincipal;
import com.example.GrpSB.model.Error;
import com.example.GrpSB.model.TestModel;
import com.example.GrpSB.repo.ErrorRepository;
import com.example.GrpSB.service.ExcerciseService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/worksheet4")
public class Excercise4 {
    private ExcerciseService excerciseService;
    private final ErrorRepository errorRepository;

    public Excercise4(ExcerciseService excerciseService, ErrorRepository errorRepository) {
        this.excerciseService = excerciseService;
        this.errorRepository = errorRepository;
    }

    @PostMapping(value="/test")
    public String postExercise(@RequestBody TestModel testModel, HttpServletRequest request) {
       String x = request.getServletPath();

        List<String> user_session = (List<String>) request.getSession().getAttribute("SESSION_USERS");

        Map<String, String> user = new HashMap<>();
        user.put("username", "User1");

        if (user_session == null) {
            request.getSession().setAttribute("SESSION_USERS", user);
        }

        request.getSession().setAttribute("MY_SESSION_MESSAGES", user);
        excerciseService.postTestValues(testModel.getTest());
        return "{status: 200}";
    }

    @GetMapping(value="/ex2")
    public String getExercise(Model model, UserPrincipal userPrincipal) {
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("last_login", user.getLastLogin());
        return "S3Ex2";
    }


    @GetMapping(value="/ex4")
    public String testAnalysis() {
        return "S4Ex4";
    }

    @PostMapping(value="/popularPickup")
    public String showMostPopularPickupLocations(Model model) {
        List<Map<String,String>> result = excerciseService.getMostPopularPickupLocations();
        model.addAttribute("query_result",result);
        return "result_popularPickup";
    }

    @PostMapping(value="/popularDropOff")
    public String showMostPopularDropOffLocations(Model model) {
        List<Map<String,String>> result = excerciseService.getMostPopularDropOffLocations();
        model.addAttribute("query_result",result);
        return "result_popularDropOff";
    }

    @PostMapping(value="/popularPayment")
    public String showMostPopularPaymentMethods(Model model) {
        List<Map<String,String>> result = excerciseService.getMostPopularPaymentMethods();
        model.addAttribute("query_result",result);
        return "result_popularPayment";
    }

    @PostMapping(value="/estimatedPrice")
    public String showEstimatedPriceForaLocation(Model model) {
        List<Map<String,String>> result = excerciseService.getEstimatedPriceForaLocation();
        model.addAttribute("query_result",result);
        return "result_estimatedPrice";
    }

    @PostMapping(value="/mostCongested")
    public String showMostCongestedLocations(Model model) {
        List<Map<String,String>> result = excerciseService.getMostCongestedLocations();
        model.addAttribute("query_result",result);
        return "result_mostCongested";
    }

    @PostMapping(value="/mostPopularDay")
    public String showMostPopularDayoftheweekforRides(Model model) {
        List<Map<String,String>> result = excerciseService.getMostPopularDayoftheweekforRides();
        model.addAttribute("query_result",result);
        return "result_mostPopularDay";
    }

    @PostMapping(value="/averageTipAmount")
    public String showAvergaeTipAMountByLocation(Model model) {
        List<Map<String,String>> result = excerciseService.getAvergaeTipAMountByLocation();
        model.addAttribute("query_result",result);
        return "result_averageTipAmount";
    }

    @PostMapping(value="/averageTollAmount")
    public String showAvergaeTollAMountByLocation(Model model) {
        List<Map<String,String>> result = excerciseService.getAvergaeTollAMountByLocation();
        model.addAttribute("query_result",result);
        return "result_averageTollAmount";
    }

    @PostMapping(value="/mostPopularHvfh")
    public String showMostPopularHvfhService(Model model) {
        List<Map<String,String>> result = excerciseService.getMostPopularHvfhService();
        model.addAttribute("query_result",result);
        return "result_mostPopularHvfh";
    }

    @PostMapping(value="/mostPopularRide")
    public String showMostPopularRideSharingService(Model model) {
        List<Map<String,String>> result = excerciseService.getMostPopularRideSharingService();
        model.addAttribute("query_result",result);
        return "result_mostPopularRide";
    }

    @RequestMapping(value = "/http_error", method = RequestMethod.GET)
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        Error errorDetails = errorRepository.findById(error).orElse(null);
        model.addAttribute("error_code", errorDetails.getErrorCode());
        model.addAttribute("diagonistic", errorDetails.getDiagnosticInfo());

        return "error";
    }

}
