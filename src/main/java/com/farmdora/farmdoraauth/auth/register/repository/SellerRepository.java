package com.farmdora.farmdoraauth.auth.register.repository;

import com.farmdora.farmdoraauth.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Integer> {

    List<Seller> findByIsApproveOrderByIdDesc(boolean isApprove);
}
