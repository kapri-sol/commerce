package com.commerce.domain.seller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateSellerDto {
    @Size(min = 1, max = 255)
    @NotEmpty
    private String name;
    @Size(min = 1, max = 255)
    @NotEmpty
    private String address;
}
