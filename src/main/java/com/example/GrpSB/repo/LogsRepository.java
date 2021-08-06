package com.example.GrpSB.repo;

import com.example.GrpSB.model.ApplicationLogs;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LogsRepository extends Repository<ApplicationLogs, Integer> {

    void delete(ApplicationLogs deleted);

    List<ApplicationLogs> findAll();

    ApplicationLogs save(ApplicationLogs persisted);


}
