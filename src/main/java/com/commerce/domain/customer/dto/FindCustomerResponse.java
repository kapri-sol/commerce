package com.commerce.domain.customer.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FindCustomerResponse {
    private String name;
    private String address;
}
