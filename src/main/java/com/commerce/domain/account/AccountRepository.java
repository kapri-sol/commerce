package com.commerce.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByUsernameOrEmailOrPhoneNumber(String username, String email, String phoneNumber);
}
