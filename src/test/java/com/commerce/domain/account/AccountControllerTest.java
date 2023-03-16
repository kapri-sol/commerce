package com.commerce.domain.account;

import com.commerce.domain.account.dto.CreateAccountDto;
import com.commerce.domain.account.dto.CreateAccountResponse;
import com.commerce.domain.account.dto.FindAccountResponse;
import com.commerce.dummy.DummyAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest extends DummyAccount {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AccountRepository accountRepository;

    Faker faker = new Faker();

    @DisplayName("GET /accounts OK")
    @Test
    void findAccount() throws Exception {
        // given
        Account initAccount = accountRepository.save(generateAccount());
        FindAccountResponse findAccountResponse = FindAccountResponse.builder()
                .email(initAccount.getEmail())
                .phoneNumber(initAccount.getPhoneNumber())
                .name(initAccount.getName())
                .build();

        // when
        mvc.perform(get("/accounts/" + 1))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(findAccountResponse)));
    }

    @DisplayName("GET /accounts NOT_FOUND")
    @Test
    void findAccountByNonExistId() throws Exception {
        // given
        // when
        mvc.perform(get("/accounts/" + 1000))
                // then
                .andExpect(status().isNotFound());

    }

    @DisplayName("POST /accounts CREATED")
    @Test
    void createAccount() throws Exception {
        // given
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .name(faker.name().fullName())
                .password(faker.internet().password())
                .build();
        CreateAccountResponse createAccountResponse = CreateAccountResponse.builder()
                .accountId(1L)
                .build();

        // when
        mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountDto)))
                // then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(createAccountResponse)));
    }

    @DisplayName("POST /accounts BAD_REQUEST : 이메일을 입력안했을 때")
    @Test
    void createAccountByEmptyEmail() throws Exception {
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .email("")
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .name(faker.name().fullName())
                .password(faker.internet().password())
                .build();

        mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountDto))
        )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("POST /accounts BAD_REQUEST : 휴대폰번호를 입력안했을 때")
    @Test
    void createAccountByEmptyPhoneNumber() throws Exception {
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .email(faker.internet().emailAddress())
                .phoneNumber("")
                .name(faker.name().fullName())
                .password(faker.internet().password())
                .build();

        mvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAccountDto))
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("POST /accounts BAD_REQUEST : 이름을 입력안했을 때")
    @Test
    void createAccountByEmptyName() throws Exception {
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .name("")
                .password(faker.internet().password())
                .build();

        mvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAccountDto))
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("POST /accounts BAD_REQUEST : 비밀번호를 입력안했을 때")
    @Test
    void createAccountByEmptyPassword() throws Exception {
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .name(faker.name().fullName())
                .password("")
                .build();

        mvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAccountDto))
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("POST /accounts BAD_REQUEST : 이메일을 입력안했을 때")
    @Test
    void createAccountByNullEmail() throws Exception {
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .name(faker.name().fullName())
                .password(faker.internet().password())
                .build();

        mvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAccountDto))
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("PATCH accounts")
    @Test
    void updateAccount() {
    }

    @Test
    void deleteAccount() {
    }
}