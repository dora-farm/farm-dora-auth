package com.farmdora.farmdoraauth.auth.oauth.repository;

import com.farmdora.farmdoraauth.entity.SnsType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsTypeRepository extends JpaRepository<SnsType, Short> {
    SnsType findByName(String name);
}
