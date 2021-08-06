package com.example.GrpSB.controller;

import com.example.GrpSB.dao.UserPrincipal;
import com.example.GrpSB.model.ApplicationLogs;
import com.example.GrpSB.model.Error;
import com.example.GrpSB.repo.ErrorRepository;
import com.example.GrpSB.repo.LogsRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    private final ErrorRepository errorRepository;
    private final LogsRepository logsRepository;

    public ExceptionHandlerController(ErrorRepository errorRepository, LogsRepository logsRepository) {
        this.errorRepository = errorRepository;
        this.logsRepository = logsRepository;
    }


    @ExceptionHandler(value
            = {IllegalArgumentException.class, IllegalStateException.class})
    protected String handleConflict(
            RuntimeException ex, WebRequest request, Model model) {

        model.addAttribute("error_code", "500");
        model.addAttribute("diagonistic", "An Unknown SQL Error Occured");
        model.addAttribute("detailed_info", ex.getCause().getMessage());
        return "error";
    }

    @ExceptionHandler(value = SQLException.class)
    public String handleDatabaseExceptions(SQLException e, Model model) {
        model.addAttribute("error_code", "500");
        model.addAttribute("diagonistic", "An Unknown SQL Error Occured");
        model.addAttribute("detailed_info", e.getCause().getMessage());

        return "error";
    }

    @ExceptionHandler(value = SQLGrammarException.class)
    public String handleJPaMappingException(SQLGrammarException e, Model model) {
        DateTime dt = new DateTime();
        Error error = errorRepository.findById("ERR105").orElse(null);
        model.addAttribute("id", error.getId());
        model.addAttribute("error_code", error.getErrorCode());
        model.addAttribute("diagonistic", error.getDiagnosticInfo());
        model.addAttribute("detailed_info", e.getCause().getMessage());


        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationLogs applicationLogs = new ApplicationLogs();

        applicationLogs.setErrorId(error.getId());
        applicationLogs.setErrorDescription(error.getDiagnosticInfo());
        applicationLogs.setTime(dt.toString());
        applicationLogs.setUsername(user.getUsername());
        logsRepository.save(applicationLogs);

        return "error";
    }

    @ExceptionHandler(value = JDBCConnectionException.class)
    public String handleJPaMissingRelation(JDBCConnectionException e, Model model) {
        model.addAttribute("error_code", "400");
        model.addAttribute("diagonistic", "JDBC Connection not found");
        model.addAttribute("detailed_info", "Check JDBC connection");

        return "error";
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public String handleIntegrityConstraintException(ConstraintViolationException e, Model model) {
        DateTime dt = new DateTime();
        Error error = errorRepository.findById("ERR110").orElse(null);
        model.addAttribute("error_code", error.getErrorCode());
        model.addAttribute("diagonistic", error.getDiagnosticInfo());
        model.addAttribute("detailed_info", e.getCause().getMessage());

        ApplicationLogs applicationLogs = new ApplicationLogs();
        applicationLogs.setErrorId(error.getId());
        applicationLogs.setErrorDescription(error.getDiagnosticInfo());
        applicationLogs.setTime(dt.toString());
        logsRepository.save(applicationLogs);

        return "error";
    }

    @ExceptionHandler(value = DataException.class)
    public String handleDataException(DataException e, Model model) {
        DateTime dt = new DateTime();
        Error error = errorRepository.findById("ERR113").orElse(null);
        model.addAttribute("error_code", error.getErrorCode());
        model.addAttribute("diagonistic", error.getDiagnosticInfo());
        model.addAttribute("detailed_info", e.getCause().getMessage());

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationLogs applicationLogs = new ApplicationLogs();

        applicationLogs.setErrorId(error.getId());
        applicationLogs.setErrorDescription(error.getDiagnosticInfo());
        applicationLogs.setTime(dt.toString());
        applicationLogs.setUsername(user.getUsername());
        logsRepository.save(applicationLogs);

        return "error";
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public String handleUserNotFound(UsernameNotFoundException e, Model model) {
        Error error = errorRepository.findById("ERR180").orElse(null);
        model.addAttribute("error_code", error.getErrorCode());
        model.addAttribute("diagonistic", error.getDiagnosticInfo());
        model.addAttribute("detailed_info", e.getCause().getMessage());

        return "error";
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DateTime dt = new DateTime();
        ApplicationLogs applicationLogs = new ApplicationLogs();

        Error errorDetails = errorRepository.findById("ERR405").orElse(null);
        applicationLogs.setErrorId("ERR405");
        applicationLogs.setErrorDescription(errorDetails.getDiagnosticInfo());
        applicationLogs.setTime(dt.toString());
        applicationLogs.setUsername(user.getUsername());
        logsRepository.save(applicationLogs);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/worksheet4/http_error?error=ERR405");
        return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DateTime dt = new DateTime();
        ApplicationLogs applicationLogs = new ApplicationLogs();

        Error errorDetails;
        if (status.value() == 404) {
            errorDetails = errorRepository.findById("ERR404").orElse(null);
            applicationLogs.setErrorId("ERR404");
        } else {
            errorDetails = errorRepository.findById("ERR500").orElse(null);
            applicationLogs.setErrorId("ERR500");
        }
        applicationLogs.setErrorDescription(errorDetails.getDiagnosticInfo());
        applicationLogs.setTime(dt.toString());
        applicationLogs.setUsername(user.getUsername());
        logsRepository.save(applicationLogs);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "/worksheet4/http_error?error="+errorDetails.getId());
        return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }


}
