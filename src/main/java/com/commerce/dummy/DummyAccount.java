package com.commerce.dummy;

import com.commerce.domain.account.Account;
import net.datafaker.Faker;

public class DummyAccount {
    Faker faker = new Faker();

    public Account generateAccount() {
        return Account.builder()
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .name(faker.name().fullName())
                .password(faker.internet().password())
                .build();
    }
}
