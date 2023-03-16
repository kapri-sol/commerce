package com.commerce.domain.account.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FindAccountResponse {
    private String username;
    private String email;
    private String phoneNumber;
}
