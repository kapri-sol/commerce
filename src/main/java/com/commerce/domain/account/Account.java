package com.commerce.domain.account;

import com.commerce.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

@Where(clause = "deleted is False")
@Getter
@Entity
@NoArgsConstructor
public class Account extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    private String name;

    private String password;

    private Boolean deleted = false;

    @Builder
    Account(String email, String name, String phoneNumber, String password) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public void update(String name, String phoneNumber, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    void delete() {
        this.deleted = true;
    }
}
