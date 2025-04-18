package com.farmdora.farmdoraauth.auth.register.repository;

import com.farmdora.farmdoraauth.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"auth"})
    Optional<User> findById(String id);
}
