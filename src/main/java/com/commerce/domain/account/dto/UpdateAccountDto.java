package com.commerce.domain.account.dto;

import lombok.Data;

@Data
public class UpdateAccountDto {
    String phoneNumber;
    String name;
    String password;
}
