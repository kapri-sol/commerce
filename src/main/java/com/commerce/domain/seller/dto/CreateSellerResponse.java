package com.commerce.domain.seller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateSellerResponse {
    private Long sellerId;
}
