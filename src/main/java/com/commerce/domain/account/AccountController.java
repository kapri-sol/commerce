package com.commerce.domain.account;

import com.commerce.domain.account.dto.CreateAccountDto;
import com.commerce.domain.account.dto.CreateAccountResponse;
import com.commerce.domain.account.dto.FindAccountResponse;
import com.commerce.domain.account.dto.UpdateAccountDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("accounts")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("{id}")
    FindAccountResponse findAccount(@PathVariable("id") Long accountId) {
        Account account = accountService.findAccountById(accountId);
        return FindAccountResponse.builder()
                .email(account.getEmail())
                .name(account.getName())
                .phoneNumber(account.getPhoneNumber())
                .build();
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    CreateAccountResponse createAccount(@RequestBody @Valid CreateAccountDto createAccountDto) {
        Long accountId = accountService.createAccount(createAccountDto);
        return CreateAccountResponse.builder()
                .accountId(accountId)
                .build();
    }

    @PatchMapping("{id}")
    void  updateAccount(@PathVariable("id") Long accountId, @RequestBody UpdateAccountDto updateAccountDto) {
        accountService.updateAccount(accountId, updateAccountDto);
    }

    @DeleteMapping("{id}")
    void deleteAccount(@PathVariable("id") Long accountId) {
        accountService.deleteAccountById(accountId);
    }
}
