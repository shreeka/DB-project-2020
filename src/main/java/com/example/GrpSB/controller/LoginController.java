package com.example.GrpSB.controller;

import com.example.GrpSB.model.ApplicationLogs;
import com.example.GrpSB.model.Error;
import com.example.GrpSB.repo.ErrorRepository;
import com.example.GrpSB.repo.LogsRepository;
import com.example.GrpSB.service.ExcerciseService;
import org.joda.time.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private ExcerciseService excerciseService;
    private final ErrorRepository errorRepository;
    private final LogsRepository logsRepository;

    public LoginController(ExcerciseService excerciseService, ErrorRepository errorRepository, LogsRepository logsRepository) {
        this.excerciseService = excerciseService;
        this.errorRepository = errorRepository;
        this.logsRepository = logsRepository;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        String errorMessge = null;
        if(error != null) {
            DateTime dt = new DateTime();
            ApplicationLogs applicationLogs = new ApplicationLogs();

            Error errorDetails = errorRepository.findById("ERR101").orElse(null);
            applicationLogs.setErrorId("ERR101");

            applicationLogs.setErrorDescription(errorDetails.getDiagnosticInfo());
            applicationLogs.setTime(dt.toString());
            logsRepository.save(applicationLogs);
            errorMessge = "Username or Password is incorrect !!";
        }

        model.addAttribute("errorMessge", errorMessge);
        return "login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }

    @RequestMapping(value="/create-defaault-users", method = RequestMethod.GET)
    public ResponseEntity<String> createUsers () {
        return excerciseService.createUsers();
    }

}
