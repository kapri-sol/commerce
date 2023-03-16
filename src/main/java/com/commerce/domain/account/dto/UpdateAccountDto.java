package com.commerce.domain.account.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UpdateAccountDto {
    String phoneNumber;
    String password;

    @Builder
    public UpdateAccountDto(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
