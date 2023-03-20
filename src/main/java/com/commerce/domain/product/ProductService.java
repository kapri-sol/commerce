package com.commerce.domain.product;

import com.commerce.domain.product.dto.CreateProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    Long createProduct(CreateProductDto createProductDto) {
        Product product = Product.builder()
                .title(createProductDto.getTitle())
                .description(createProductDto.getDescription())
                .image(createProductDto.getImage())
                .price(createProductDto.getPrice())
                .stockQuantity(createProductDto.getStockQuantity())
                .build();

        productRepository.save(product);

        return product.getId();
    }
}
