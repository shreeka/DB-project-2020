package com.example.GrpSB.repo;

import com.example.GrpSB.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Modifying
    @Query("update User u set u.lastLogin = ?2 where u.id = ?1")
    int updateLastLoggedIn(long id, String login_time);
}
