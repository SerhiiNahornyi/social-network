package com.kpi.project.repository;

import com.kpi.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :loginParam or u.username = :loginParam")
    Optional<User> loadByEmailOrUsername(@Param("loginParam") String loginParam);

    User findByEmailOrUsername(String email, String username);

    User findByEmail(String email);

    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.id = :idParam")
    User findByIdIdentifier(@Param("idParam") Long id);
}
