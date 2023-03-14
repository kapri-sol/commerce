package com.commerce.domain.account;

import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
