package com.commerce.domain.account;

import com.commerce.domain.account.dto.CreateAccountDto;
import com.commerce.domain.account.dto.UpdateAccountDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceSnippetParameters.*;
import static org.hamcrest.Matchers.*;
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

    @DisplayName("GET - Ok")
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

        // when
        mvc.perform(get("/accounts/" + initAccount.getId()))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("email", is(initAccount.getEmail())))
                .andExpect(jsonPath("phoneNumber", is(initAccount.getPhoneNumber())))
                .andExpect(jsonPath("username", is(initAccount.getUsername())))
                .andDo(
                        document(
                                "account/get/ok",
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

    @DisplayName("GET - NotFound")
    @Test
    void findAccountByNonExistId() throws Exception {
        // given
        // when
        mvc.perform(get("/accounts/" + 1000))
                // then
                .andExpect(status().isNotFound())
                .andDo(document("account/get/not-found")
                );
    }

    @DisplayName("POST - Created")
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
                .andExpect(jsonPath("accountId", notNullValue(Long.class)))
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

    @DisplayName("POST - BadRequest ( empty email )")
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
                .andExpect(jsonPath("field",  is("email")))
                .andExpect(jsonPath("message", notNullValue(String.class)))
                .andExpect(jsonPath("code", notNullValue()))
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

    @DisplayName("POST - BadRequest ( empty phoneNumber )")
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
                .andExpect(jsonPath("field",  is("phoneNumber")))
                .andExpect(jsonPath("message", notNullValue(String.class)))
                .andExpect(jsonPath("code", notNullValue()))
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

    @DisplayName("POST - BadRequest ( empty username )")
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
                .andExpect(jsonPath("field",  is("username")))
                .andExpect(jsonPath("message", notNullValue(String.class)))
                .andExpect(jsonPath("code", notNullValue()))
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

    @DisplayName("POST - BadRequest ( empty password )")
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


    @DisplayName("POST - Conflict ( duplicated username )")
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
                .andExpect(jsonPath("field",  is("username")))
                .andExpect(jsonPath("message", notNullValue(String.class)))
                .andExpect(jsonPath("code", notNullValue()))
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

    @DisplayName("POST - Conflict ( duplicated email )")
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
                .andExpect(jsonPath("field",  is("email")))
                .andExpect(jsonPath("message", notNullValue(String.class)))
                .andExpect(jsonPath("code", notNullValue()))
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

    @DisplayName("POST - Conflict ( duplicated phoneNumber )")
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
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createAccountDto))
                )
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("field",  is("phoneNumber")))
                .andExpect(jsonPath("message", notNullValue(String.class)))
                .andExpect(jsonPath("code", notNullValue()))
                .andDo(
                        document("account/post/conflict/duplicated/phone-number",
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

    @DisplayName("PATCH - NoContent")
    @Test
    void updateAccount() throws Exception {
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        accountRepository.save(initialAccount);

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

    @DisplayName("PATCH - Conflict ( duplicated phoneNumber )")
    @Test
    void updateAccountByDuplicatedPhoneNumber() throws Exception {
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        Account updateAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        initialAccount = accountRepository.save(initialAccount);
        updateAccount = accountRepository.save(updateAccount);

        UpdateAccountDto updateAccountDto = UpdateAccountDto.builder()
                .phoneNumber(initialAccount.getPhoneNumber())
                .password(faker.internet().password())
                .build();

        mvc.perform(
                        patch("/accounts/" + updateAccount.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateAccountDto))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("code", notNullValue()))
                .andExpect(jsonPath("field", is("phoneNumber")))
                .andExpect(jsonPath("message", notNullValue()))
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

    @DisplayName("PATCH - NotFound")
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

    @DisplayName("DELETE - NoContent")
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
                delete("/accounts/" + initialAccount.getId())
                )
                .andExpect(status().isNoContent())
                .andDo(
                        document("account/delete/no-content")
                );
    }

    @DisplayName("DELETE - NotFound")
    @Test()
    void deleteAccountByNotExistId() throws Exception {
        long accountId = 10000L;

        mvc.perform(
                        delete("/accounts/delete/not-found" + accountId)
                )
                .andExpect(status().isNotFound());
    }
}