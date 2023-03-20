package com.commerce.domain.account;

import com.commerce.domain.account.dto.CreateAccountDto;
import com.commerce.domain.account.dto.UpdateAccountDto;
import com.commerce.exception.UniqueConstraintViolationException;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(NoResultException::new);
    }

    Long createAccount(CreateAccountDto createAccountDto) {
        accountRepository.findAccountByUsernameOrEmailOrPhoneNumber(
                createAccountDto.getUsername(),
                createAccountDto.getEmail(),
                createAccountDto.getPhoneNumber()
        ).ifPresent(duplicatedAccount -> {
            String field = Objects.equals(duplicatedAccount.getUsername(), createAccountDto.getUsername()) ? "username" :
                    Objects.equals(duplicatedAccount.getEmail(), createAccountDto.getEmail()) ?  "email" : "phoneNumber";
            throw new UniqueConstraintViolationException(field);
        });

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
         accountRepository.findAccountByPhoneNumberAndIdIsNot(updateAccountDto.getPhoneNumber(), accountId)
                .ifPresent((duplicatedPhoneNumberAccount) ->  {
                    throw new UniqueConstraintViolationException("phoneNumber");
                });
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
