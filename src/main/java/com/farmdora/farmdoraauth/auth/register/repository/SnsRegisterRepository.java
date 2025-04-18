package com.farmdora.farmdoraauth.auth.register.repository;

import com.farmdora.farmdoraauth.entity.Sns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsRegisterRepository extends JpaRepository<Sns, Short> {
}
