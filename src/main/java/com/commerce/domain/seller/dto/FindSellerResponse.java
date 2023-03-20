package com.commerce.domain.seller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindSellerResponse {
    private String name;
    private String address;
}
