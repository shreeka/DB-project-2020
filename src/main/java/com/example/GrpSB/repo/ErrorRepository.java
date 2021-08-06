package com.example.GrpSB.repo;

import com.example.GrpSB.model.Error;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ErrorRepository extends JpaRepository<Error, String> {

    Optional<Error> findById(String id);
}
