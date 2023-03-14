package com.commerce.domain.account.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateAccountDto {
    private String email;
    private String phoneNumber;
    private String name;
    private String password;
}
