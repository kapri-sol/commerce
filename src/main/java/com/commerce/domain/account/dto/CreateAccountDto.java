package com.commerce.domain.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAccountDto {
    @Size(min = 1, max = 255)
    @NotEmpty
    private String username;
    @Email
    @Size(min = 1, max = 255)
    @NotEmpty
    private String email;
    @Size(min = 1, max = 255)
    @NotEmpty
    private String phoneNumber;
    @Size(min = 1, max = 255)
    @NotEmpty
    private String password;

    @Builder
    public CreateAccountDto(String username, String email, String phoneNumber, String password) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
