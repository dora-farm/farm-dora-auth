package com.farmdora.farmdoraauth.auth.register.repository;

import com.farmdora.farmdoraauth.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Short> {
}
