package com.commerce.domain.customer.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateCustomerDto {
    @Size(min = 1, max = 255)
    @NotEmpty
    private String name;
    @Size(min = 1, max = 255)
    @NotEmpty
    private String address;

    @Builder
    public UpdateCustomerDto(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
