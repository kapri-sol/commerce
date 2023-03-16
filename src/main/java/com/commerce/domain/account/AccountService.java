package com.commerce.domain.account;

import com.commerce.domain.account.dto.CreateAccountDto;
import com.commerce.domain.account.dto.UpdateAccountDto;
import com.commerce.exception.UniqueConstraintViolationException;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(NoResultException::new);
    }

    Long createAccount(CreateAccountDto createAccountDto) {
        Optional<Account> duplicatedAccount = accountRepository.findAccountByUsernameOrEmailOrPhoneNumber(
                createAccountDto.getUsername(),
                createAccountDto.getEmail(),
                createAccountDto.getPhoneNumber()
        );

        if (duplicatedAccount.isPresent()) {
            String field = Objects.equals(duplicatedAccount.get().getUsername(), createAccountDto.getUsername()) ? "username" :
                    Objects.equals(duplicatedAccount.get().getEmail(), createAccountDto.getEmail()) ?  "email" : "phoneNumber";

            throw new UniqueConstraintViolationException(field);
        }

        Account createAccount = Account.builder()
                .email(createAccountDto.getEmail())
                .username(createAccountDto.getUsername())
                .password(createAccountDto.getPassword())
                .build();
        Account account = accountRepository.save(createAccount);
        return account.getId();
    }

    void updateAccount(Long accountId, UpdateAccountDto updateAccountDto) {
        Account account = accountRepository.findById(accountId).orElseThrow(NoResultException::new);
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
