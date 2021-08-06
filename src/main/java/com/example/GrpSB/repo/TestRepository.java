package com.example.GrpSB.repo;

import com.example.GrpSB.model.TestModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends CrudRepository<TestModel, String> {
}
