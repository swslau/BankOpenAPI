package com.open.bank.api.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.open.bank.api.account.entity.CustomerLoginAccount;

public interface CustomerLoginAccountRepository extends JpaRepository<CustomerLoginAccount, String> {
	
	@Query("SELECT cla FROM CustomerLoginAccount cla WHERE cla.username = :username")
	CustomerLoginAccount findByUsername(String username);
	
}
