package com.example.swed.repository;

import com.example.swed.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<List<Account>> findAccountsById(Integer accountId);
    Optional<Account> findByCustomerIdAndCurrency(Integer customerId, String currency);



}
