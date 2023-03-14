package com.commerce.domain.account;

import com.commerce.domain.account.dto.CreateAccountDto;
import com.commerce.domain.account.dto.UpdateAccountDto;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(NoResultException::new);
    }

    Long createAccount(CreateAccountDto createAccountDto) {
        Account createAccount = Account.builder()
                .email(createAccountDto.getEmail())
                .name(createAccountDto.getName())
                .password(createAccountDto.getPassword())
                .build();
        Account account = accountRepository.save(createAccount);
        return account.getId();
    }

    void updateAccount(Long accountId, UpdateAccountDto updateAccountDto) {
        Account account = accountRepository.findById(accountId).orElseThrow(NoResultException::new);
        account.setName(updateAccountDto.getName());
        account.setPhoneNumber(updateAccountDto.getPhoneNumber());
        account.setPassword(updateAccountDto.getPassword());
        accountRepository.save(account);
    }

    void deleteAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(NoResultException::new);
        account.delete();
        accountRepository.save(account);
    }
}
