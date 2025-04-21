package com.farmdora.farmdoraauth.auth.register.repository;

import com.farmdora.farmdoraauth.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"auth"})
    Optional<User> findById(String id);

    @Query("select u.userId from User u where u.id =:id ")
    int findUserIdById(@Param("id") String id);
}
