package com.commerce.domain.account.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAccountDto {
    private String email;
    private String phoneNumber;
    private String name;
    private String password;

    @Builder
    public CreateAccountDto(String email, String phoneNumber, String name, String password) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.password = password;
    }
}
