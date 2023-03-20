package com.commerce.domain.customer.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateCustomerResponse {
    Long customerId;
}
