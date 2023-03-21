package com.commerce.domain.product;

import com.commerce.domain.product.dto.CreateProductDto;
import com.commerce.domain.seller.Seller;
import com.commerce.domain.seller.SellerRepository;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {
    @Autowired private ProductRepository productRepository;
    @Autowired private ProductService productService;
    @Autowired private SellerRepository sellerRepository;

    private final Faker faker = new Faker();

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // given
        Seller seller = Seller.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        sellerRepository.save(seller);

        CreateProductDto createProductDto = CreateProductDto.builder()
                .title(faker.commerce().productName())
                .description(faker.lorem().sentence())
                .image(faker.internet().image())
                .price(faker.random().nextInt(0, 1000))
                .stockQuantity(faker.random().nextInt(0, 1000))
                .build();

        // when
        Long productId = productService.createProduct(seller.getId(), createProductDto);

        Optional<Product> optionalProduct = productRepository.findById(productId);

        // then
        assertThat(optionalProduct).isPresent();
        assertThat(optionalProduct).hasValueSatisfying(product -> {
            assertThat(product.getId()).isEqualTo(productId);
            assertThat(product.getTitle()).isEqualTo(createProductDto.getTitle());
            assertThat(product.getDescription()).isEqualTo(createProductDto.getDescription());
            assertThat(product.getImage()).isEqualTo(createProductDto.getImage());
            assertThat(product.getPrice()).isEqualTo(createProductDto.getPrice());
            assertThat(product.getStockQuantity()).isEqualTo(createProductDto.getStockQuantity());
        });
    }

    @DisplayName("상품 id로 상품을 검색한다.")
    @Test
    void findProductById() {
        // given
        Product product = Product.builder()
                .title(faker.commerce().productName())
                .description(faker.lorem().sentence())
                .image(faker.internet().image())
                .price(faker.random().nextInt(0, 1000))
                .stockQuantity(faker.random().nextInt(0, 1000))
                .build();

        productRepository.save(product);

        // when
        Product findProduct = productService.findProductByProductId(product.getId());

        // then
        assertThat(findProduct.getId()).isEqualTo(product.getId());
        assertThat(findProduct.getTitle()).isEqualTo(product.getTitle());
        assertThat(findProduct.getDescription()).isEqualTo(product.getDescription());
        assertThat(findProduct.getImage()).isEqualTo(product.getImage());
        assertThat(findProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(findProduct.getStockQuantity()).isEqualTo(product.getStockQuantity());
    }

    @Test
    void queryDslTest() {

    }
}