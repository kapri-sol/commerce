package com.commerce.domain.account.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateAccountResponse {
    private Long accountId;
}
