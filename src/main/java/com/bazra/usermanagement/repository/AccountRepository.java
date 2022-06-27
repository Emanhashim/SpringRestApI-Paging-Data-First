package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.Account;



@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
   Optional<Account> findByAccountNumberEquals(String accountnumber);
   Optional<Account> findByusername(String username);
}
