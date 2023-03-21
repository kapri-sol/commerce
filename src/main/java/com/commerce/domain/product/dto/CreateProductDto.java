package com.commerce.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateProductDto {
    private Long sellerId;
    private String title;
    private String description;
    private String image;
    private Integer price;
    private Integer stockQuantity;

    @Builder
    public CreateProductDto(Long sellerId, String title, String description, String image, Integer price, Integer stockQuantity) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
