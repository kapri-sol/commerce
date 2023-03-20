package com.commerce.domain.seller;

import com.commerce.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Where(clause = "deleted is false")
@Getter
@NoArgsConstructor
@Entity
public class Seller extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String address;

    private Boolean deleted = false;

    @Builder
    public Seller(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void delete() {
        this.deleted = true;
    }
}
