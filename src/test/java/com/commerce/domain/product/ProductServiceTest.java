package com.commerce.domain.product;

import com.commerce.domain.product.dto.CreateProductDto;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {
    @Autowired private ProductRepository productRepository;
    @Autowired private ProductService productService;
    private Faker faker = new Faker();

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        CreateProductDto createProductDto = CreateProductDto.builder()
                .build();
    }
}