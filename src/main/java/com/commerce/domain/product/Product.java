package com.commerce.domain.product;

import com.commerce.common.entity.BaseEntity;
import com.commerce.domain.seller.Seller;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn
    private Seller seller;
    private String title;
    private String description;
    private String image;
    private Integer price;
    private Integer stockQuantity;

    private Boolean deleted = false;

    @Builder
    public Product(Seller seller, String title, String description, String image, Integer price, Integer stockQuantity) {

        if (stockQuantity <= 0) {
            throw new IllegalArgumentException("stockQuantity must be bigger than 0");
        }

        this.seller = seller;
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void delete() {
        this.deleted = true;
    }

    public void decreaseQuantity(Integer quantity) {

        if (this.stockQuantity - quantity < 0) {
           throw new IllegalStateException("stockQuantity is Insufficient");
        }

        this.stockQuantity -= quantity;
    }
}
