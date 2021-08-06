package com.example.GrpSB.model;

import org.hibernate.annotations.GeneratorType;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.*;

@Entity
@Table(name = "error")
public class Error {

    @Id
    private String id;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "diagnostic_info")
    private String diagnosticInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDiagnosticInfo() {
        return diagnosticInfo;
    }

    public void setDiagnosticInfo(String diagnosticInfo) {
        this.diagnosticInfo = diagnosticInfo;
    }
}
