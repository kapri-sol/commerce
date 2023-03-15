package com.commerce.domain.account;

import com.commerce.dummy.DummyAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest extends DummyAccount {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @Autowired AccountRepository accountRepository;

    @DisplayName("GET accounts")
    @Test
    void findAccount() throws Exception {
        // given
        Account initAccount = accountRepository.save(generateAccount());

        // when
        // then
        mvc.perform(MockMvcRequestBuilders.get("/accounts/" + 1)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createAccount() {
    }

    @Test
    void updateAccount() {
    }

    @Test
    void deleteAccount() {
    }
}