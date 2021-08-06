package com.example.GrpSB.repo;

import com.example.GrpSB.model.MovieAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AliasRepository extends JpaRepository<MovieAlias, Integer> {

    List<MovieAlias> findByAliasTitle(String title);
}
