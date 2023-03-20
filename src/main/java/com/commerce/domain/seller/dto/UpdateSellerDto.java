package com.commerce.domain.seller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateSellerDto {
    @Size(min = 1, max = 255)
    @NotEmpty
    private String name;
    @Size(min = 1, max = 255)
    @NotEmpty
    private String address;

    @Builder
    public UpdateSellerDto(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
