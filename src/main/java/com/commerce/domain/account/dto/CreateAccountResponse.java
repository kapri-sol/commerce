package com.commerce.domain.account.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateAccountResponse {
    private Long accountId;
}
