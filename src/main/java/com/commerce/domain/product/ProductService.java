package com.commerce.domain.product;

import com.commerce.domain.product.dto.CreateProductDto;
import com.commerce.domain.seller.Seller;
import com.commerce.domain.seller.SellerRepository;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    Long createProduct(Long sellerId, CreateProductDto createProductDto) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(NoResultException::new);

        Product product = Product.builder()
                .seller(seller)
                .title(createProductDto.getTitle())
                .description(createProductDto.getDescription())
                .image(createProductDto.getImage())
                .price(createProductDto.getPrice())
                .stockQuantity(createProductDto.getStockQuantity())
                .build();

        productRepository.save(product);


        return product.getId();
    }

    Product findProductByProductId(Long productId) {
        return productRepository.findById(productId).orElseThrow(NoResultException::new);
    }
}
