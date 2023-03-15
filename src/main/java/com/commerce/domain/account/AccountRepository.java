package com.commerce.domain.account;

import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmailOrNameOrPhoneNumber(String email, String name, String phoneNumber);
}
