package com.commerce.domain.account;

import com.commerce.domain.account.dto.CreateAccountDto;
import com.commerce.domain.account.dto.FindAccountResponse;
import com.commerce.domain.account.dto.UpdateAccountDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AccountRepository accountRepository;

    Faker faker = new Faker();

    @Rollback(value = true)
    @DisplayName("GET OK")
    @Test
    void findAccount() throws Exception {
        // given
        Account initAccount = accountRepository.save(
                Account.builder()
                        .email(faker.internet().emailAddress())
                        .phoneNumber(faker.phoneNumber().phoneNumber())
                        .username(faker.name().username())
                        .password(faker.internet().password())
                        .build()
        );

        FindAccountResponse findAccountResponse = FindAccountResponse.builder()
                .username(initAccount.getUsername())
                .email(initAccount.getEmail())
                .phoneNumber(initAccount.getPhoneNumber())
                .build();

        // when
        mvc.perform(get("/accounts/" + initAccount.getId()))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(findAccountResponse)))
                .andDo(
                        document(
                                "account/get-ok",
                                builder()
                                        .tag("GET")
                                        .responseFields(
                                                fieldWithPath("username").description("유저 아이디"),
                                                fieldWithPath("email").description("이메일"),
                                                fieldWithPath("phoneNumber").description("휴대폰 번호")
                                        )
                        )
                );
    }

    @DisplayName("GET NotFound")
    @Test
    void findAccountByNonExistId() throws Exception {
        // given
        // when
        mvc.perform(get("/accounts/" + 1000))
                // then
                .andExpect(status().isNotFound())
                .andDo(document("account/get-not-found")
                );
    }

    @DisplayName("POST Created")
    @Test
    void createAccount() throws Exception {
        // given
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .username(faker.name().fullName())
                .password(faker.internet().password())
                .build();

        // when
        mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountDto)))
                // then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", Matchers.notNullValue(Long.class)))
                .andDo(
                        document("account/post/created",
                                builder()
                                        .requestFields(
                                                fieldWithPath("username").description("유저 아이디"),
                                                fieldWithPath("email").description("이메일"),
                                                fieldWithPath("phoneNumber").description("휴대폰 번호"),
                                                fieldWithPath("password").description("비밀번호")
                                        )
                                        .responseFields(
                                                fieldWithPath("accountId").description("계정 고유값")
                                        )
                                )
                );
    }

    @DisplayName("POST BadRequest[ empty email ]")
    @Test
    void createAccountByEmptyEmail() throws Exception {
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .username(faker.name().fullName())
                .email("")
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountDto))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field",  Matchers.is("email")))
                .andExpect(jsonPath("$.message", Matchers.notNullValue(String.class)))
                .andExpect(jsonPath("$.code", Matchers.notNullValue()))
                .andDo(
                        document("account/post/bad-request/empty/email",
                                builder()
                                        .requestFields(
                                                fieldWithPath("username").description("유저 아이디"),
                                                fieldWithPath("email").description("이메일"),
                                                fieldWithPath("phoneNumber").description("휴대폰 번호"),
                                                fieldWithPath("password").description("비밀번호")
                                        )
                                )
                );

    }

    @DisplayName("POST BadRequest : [ empty phoneNumber ]")
    @Test
    void createAccountByEmptyPhoneNumber() throws Exception {
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .username(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .phoneNumber("")
                .password(faker.internet().password())
                .build();

        mvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAccountDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field",  Matchers.is("phoneNumber")))
                .andExpect(jsonPath("$.message", Matchers.notNullValue(String.class)))
                .andExpect(jsonPath("$.code", Matchers.notNullValue()))
                .andDo(
                        document("account/post/bad-request/empty/phone-number",
                                builder()
                                        .requestFields(
                                                fieldWithPath("username").description("유저 아이디"),
                                                fieldWithPath("email").description("이메일"),
                                                fieldWithPath("phoneNumber").description("휴대폰 번호"),
                                                fieldWithPath("password").description("비밀번호")
                                        )
                        )
                );
    }

    @DisplayName("POST BadRequest [ empty username ]")
    @Test
    void createAccountByEmptyUsername() throws Exception {
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .username("")
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        mvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAccountDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field",  Matchers.is("username")))
                .andExpect(jsonPath("$.message", Matchers.notNullValue(String.class)))
                .andExpect(jsonPath("$.code", Matchers.notNullValue()))
                .andDo(
                        document("account/post/bad-request/empty/username",
                                builder()
                                        .requestFields(
                                                fieldWithPath("username").description("유저 아이디"),
                                                fieldWithPath("email").description("이메일"),
                                                fieldWithPath("phoneNumber").description("휴대폰 번호"),
                                                fieldWithPath("password").description("비밀번호")
                                        )
                        )
                );
    }

    @DisplayName("POST BadRequest [ empty password ]")
    @Test
    void createAccountByEmptyPassword() throws Exception {
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .username(faker.name().fullName())
                .password("")
                .build();

        mvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAccountDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(
                        document("account/post/bad-request/empty/password",
                                builder()
                                        .requestFields(
                                                fieldWithPath("username").description("유저 아이디"),
                                                fieldWithPath("email").description("이메일"),
                                                fieldWithPath("phoneNumber").description("휴대폰 번호"),
                                                fieldWithPath("password").description("비밀번호")
                                        )
                        )
                );
    }


    @DisplayName("POST Conflict [ duplicated username ]")
    @Test
    void createAccountByDuplicatedUsername() throws Exception {
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        initialAccount = accountRepository.save(initialAccount);

        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .username(initialAccount.getUsername())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        mvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAccountDto))
                )
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field",  Matchers.is("username")))
                .andExpect(jsonPath("$.message", Matchers.notNullValue(String.class)))
                .andExpect(jsonPath("$.code", Matchers.notNullValue()))
                .andDo(
                        document("account/post/conflict/duplicated/username",
                                builder()
                                        .requestFields(
                                                fieldWithPath("username").description("유저 아이디"),
                                                fieldWithPath("email").description("이메일"),
                                                fieldWithPath("phoneNumber").description("휴대폰 번호"),
                                                fieldWithPath("password").description("비밀번호")
                                        )
                        )
                );
    }

    @DisplayName("POST Conflict [ duplicated email ]")
    @Test
    void createAccountByDuplicatedEmail() throws Exception {
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        initialAccount = accountRepository.save(initialAccount);

        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .username(faker.name().username())
                .email(initialAccount.getEmail())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        mvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAccountDto))
                )
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field",  Matchers.is("email")))
                .andExpect(jsonPath("$.message", Matchers.notNullValue(String.class)))
                .andExpect(jsonPath("$.code", Matchers.notNullValue()))
                .andDo(
                        document("account/post/conflict/duplicated/email",
                                builder()
                                        .requestFields(
                                                fieldWithPath("username").description("유저 아이디"),
                                                fieldWithPath("email").description("이메일"),
                                                fieldWithPath("phoneNumber").description("휴대폰 번호"),
                                                fieldWithPath("password").description("비밀번호")
                                        )
                        )
                );
    }

    @DisplayName("POST Conflict [ duplicated phoneNumber ]")
    @Test
    void createAccountByDuplicatedPhoneNumber() throws Exception {
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        initialAccount = accountRepository.save(initialAccount);

        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(initialAccount.getPhoneNumber())
                .password(faker.internet().password())
                .build();

        mvc.perform(
                        post("/accounts/post/conflict/duplicated/phone-number")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAccountDto))
                )
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field",  Matchers.is("phoneNumber")))
                .andExpect(jsonPath("$.message", Matchers.notNullValue(String.class)))
                .andExpect(jsonPath("$.code", Matchers.notNullValue()))
                .andDo(
                        document("account",
                                builder()
                                        .requestFields(
                                                fieldWithPath("username").description("유저 아이디"),
                                                fieldWithPath("email").description("이메일"),
                                                fieldWithPath("phoneNumber").description("휴대폰 번호"),
                                                fieldWithPath("password").description("비밀번호")
                                        )
                        )
                );
    }

    @DisplayName("PATCH NoContent")
    @Test
    void updateAccount() throws Exception {
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        initialAccount = accountRepository.save(initialAccount);

        UpdateAccountDto updateAccountDto = UpdateAccountDto.builder()
                .phoneNumber(initialAccount.getPhoneNumber())
                .password(faker.internet().password())
                .build();

        mvc.perform(
                        patch("/accounts/" + initialAccount.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateAccountDto))
                )
                .andExpect(status().isNoContent())
                .andDo(
                        document("account/patch/no-content",
                                builder()
                                        .requestFields(
                                                fieldWithPath("phoneNumber").description("휴대폰 번호"),
                                                fieldWithPath("password").description("비밀번호")
                                        )
                        )
                );
    }

    @DisplayName("PATCH NotFound")
    @Test
    void updateAccountByNotExistId() throws Exception {
        long accountId = 10000L;

        UpdateAccountDto updateAccountDto = UpdateAccountDto.builder()
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        mvc.perform(
                patch("/accounts/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAccountDto))
                )
                .andExpect(status().isNotFound())
                .andDo(
                        document("account/patch/not-found",
                                builder()
                                        .requestFields(
                                                fieldWithPath("phoneNumber").description("휴대폰 번호"),
                                                fieldWithPath("password").description("비밀번호")
                                        )
                        )
                );
    }

    @DisplayName("DELETE NoContent")
    @Test()
    void deleteAccount() throws Exception {
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        initialAccount = accountRepository.save(initialAccount);

        mvc.perform(
                delete("/accounts/delete/no-content" + initialAccount.getId())
                )
                .andExpect(status().isNoContent());
    }

    @DisplayName("DELETE NotFound")
    @Test()
    void deleteAccountByNotExistId() throws Exception {
        long accountId = 10000L;

        mvc.perform(
                        delete("/accounts/delete/not-found" + accountId)
                )
                .andExpect(status().isNotFound());
    }
}