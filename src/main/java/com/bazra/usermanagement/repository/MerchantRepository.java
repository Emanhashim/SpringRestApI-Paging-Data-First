package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bazra.usermanagement.model.MerchantInfo;

public interface MerchantRepository extends JpaRepository<MerchantInfo, Long>{
	Optional<MerchantInfo> findByUsername(String username);
}
