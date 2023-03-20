package com.commerce.domain.customer;

import com.commerce.domain.customer.dto.CreateCustomerDto;
import com.commerce.domain.customer.dto.UpdateCustomerDto;
import com.epages.restdocs.apispec.Schema;
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

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CustomerControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired CustomerRepository customerRepository;
    Faker faker = new Faker();

    @DisplayName("GET - Ok")
    @Test
    void findCustomerById() throws Exception {
        // given
        Customer customer = Customer.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        customerRepository.save(customer);

        // when
        mvc.perform(get("/customers/{customerId}", customer.getId()))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(customer.getName())))
                .andExpect(jsonPath("address", is(customer.getAddress())))
                .andDo(
                        document("customer/get/ok",
                                resourceDetails()
                                        .tag("customer")
                                        .description("고객을 검색한다..")
                                        .responseSchema(new Schema("FindCustomerResponse"))
                                ,
                                responseFields(
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("address").description("주소")
                                )
                        )
                );
    }

    @DisplayName("GET - NotFound")
    @Test
    void findCustomerByNotExistId() throws Exception {
        // given
        long customerId = 1000L;

        // when
        mvc.perform(get("/customers/{customerId}", customerId))
                // then
                .andExpect(status().isNotFound())
                .andDo(document("customer/get/not-found", resourceDetails().tag("customer")));
    }

    @DisplayName("POST - Created")
    @Test
    void createCustomer() throws Exception {
        // given
        CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        // when
        mvc.perform(
                        post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createCustomerDto))
                )
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("customerId" , notNullValue(Long.class)))
                .andDo(
                        document("customer/post/created",
                                resourceDetails()
                                        .tag("customer")
                                        .description("고객을 생성한다.")
                                        .requestSchema(new Schema("CreateCustomerDto"))
                                        .responseSchema(new Schema("CreateCustomerResponse")),
                                        requestFields(
                                                fieldWithPath("name").description("이름"),
                                                fieldWithPath("address").description("주소")
                                        ),
                                        responseFields(
                                                fieldWithPath("customerId").description("고객 고유값")
                                        )
                        )
                );
    }

    @DisplayName("POST - BadRequest ( empty name )")
    @Test
    void createCustomerByEmptyName() throws Exception {
        // given
        CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
                .name("")
                .address(faker.address().fullAddress())
                .build();
        // when
        mvc.perform(
                        post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createCustomerDto))
                )
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code" , notNullValue(String.class)))
                .andExpect(jsonPath("field" , is("name")))
                .andExpect(jsonPath("message" , notNullValue(String.class)))
                .andDo(
                        document("customer/post/bad-request/empty/name",
                                resourceDetails()
                                        .tag("customer")
                                        .requestSchema(new Schema("CreateCustomerDto"))
                                        .responseSchema(new Schema("Error")),
                                requestFields(
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("address").description("주소")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("에러 코드"),
                                        fieldWithPath("field").description("필드"),
                                        fieldWithPath("message").description("메시지")
                                )
                        )
                );
    }

    @DisplayName("POST - BadRequest ( empty address )")
    @Test
    void createCustomerByEmptyAddress() throws Exception {
        // given
        CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
                .name(faker.name().fullName())
                .address("")
                .build();

        // when
        mvc.perform(
                        post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createCustomerDto))
                )
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code" , notNullValue(String.class)))
                .andExpect(jsonPath("field" , is("address")))
                .andExpect(jsonPath("message" , notNullValue(String.class)))
                .andDo(
                        document("customer/post/bad-request/empty/name",
                                resourceDetails()
                                        .tag("customer")
                                        .requestSchema(new Schema("CreateCustomerDto"))
                                        .responseSchema(new Schema("Error")),
                                requestFields(
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("address").description("주소")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("에러 코드"),
                                        fieldWithPath("field").description("필드"),
                                        fieldWithPath("message").description("메시지")
                                )
                        )
                );
    }

    @DisplayName("PATCH - NoContent")
    @Test
    void updateCustomer() throws Exception {
        // given
        Customer customer = Customer.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        customerRepository.save(customer);

        UpdateCustomerDto updateCustomerDto = UpdateCustomerDto.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        // when
        mvc.perform(
                patch("/customers/{customerId}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerDto))
                )
                // then
                .andExpect(status().isNoContent())
                .andDo(
                        document("customer/patch/no-content",
                                resourceDetails()
                                        .tag("customer")
                                        .description("고객 정보를 수정한다.")
                                        .requestSchema(new Schema("UpdateCustomerDto")),
                                requestFields(
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("address").description("주소")
                                )
                        )
                );
    }

    @DisplayName("PATCH - NotFound")
    @Test
    void updateCustomerByNotExistId() throws Exception {
        // given
        long customerId = 10000L;

        UpdateCustomerDto updateCustomerDto = UpdateCustomerDto.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        // when
        mvc.perform(
                patch("/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerDto))
                )
                // then
                .andExpect(status().isNotFound())
                .andDo(
                        document("customer/patch/not-found", resourceDetails().tag("customer"))
                );
    }

    @DisplayName("PATCH - BadRequest ( empty name )")
    @Test
    void updateCustomerByEmptyName() throws Exception {
        // given
        Customer customer = Customer.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        customerRepository.save(customer);

        UpdateCustomerDto updateCustomerDto = UpdateCustomerDto.builder()
                .name("")
                .address(faker.address().fullAddress())
                .build();

        // when
        mvc.perform(
                patch("/customers/{customerId}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerDto))
                )
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("field" , is("name")))
                .andExpect(jsonPath("code" , notNullValue(String.class)))
                .andExpect(jsonPath("message" , notNullValue(String.class)))
                .andDo(
                        document("customer/patch/bad-request/empty/name",
                                resourceDetails()
                                        .tag("customer")
                                        .requestSchema(new Schema("UpdateCustomerDto"))
                                        .responseSchema(new Schema("Error")),
                                        requestFields(
                                                fieldWithPath("name").description("이름"),
                                                fieldWithPath("address").description("주소")
                                        ),
                                        responseFields(
                                                fieldWithPath("code").description("에러 코드"),
                                                fieldWithPath("field").description("필드"),
                                                fieldWithPath("message").description("메시지")
                                        )
                        )
                );
    }

    @DisplayName("PATCH - BadRequest ( empty address )")
    @Test
    void updateCustomerByEmptyAddress() throws Exception {
        // given
        Customer customer = Customer.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();
        customerRepository.save(customer);

        UpdateCustomerDto updateCustomerDto = UpdateCustomerDto.builder()
                .name(faker.name().fullName())
                .address("")
                .build();

        // when
        mvc.perform(
                        patch("/customers/{customerId}", customer.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateCustomerDto))
                )
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("field" , is("address")))
                .andExpect(jsonPath("code" , notNullValue(String.class)))
                .andExpect(jsonPath("message" , notNullValue(String.class)))
                .andDo(
                        document("customer/patch/bad-request/empty/address",
                                resourceDetails()
                                        .tag("customer")
                                        .requestSchema(new Schema("UpdateCustomerDto"))
                                        .responseSchema(new Schema("Error")),
                                requestFields(
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("address").description("주소")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("에러 코드"),
                                        fieldWithPath("field").description("필드"),
                                        fieldWithPath("message").description("메시지")
                                )
                        )
                );
    }

    @DisplayName("DELETE - NoContent")
    @Test
    void deleteCustomerById() throws Exception {
        // given
        Customer customer = Customer.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();
        customerRepository.save(customer);

        // when
        mvc.perform(
                        delete("/customers/{customerId}", customer.getId())
                )
                // then
                .andExpect(status().isNoContent())
                .andDo(
                        document(
                                "customer/delete/no-content",
                                resourceDetails()
                                        .tag("customer")
                                        .description("고객을 삭제한다.")
                        )
                );
    }

    @DisplayName("DELETE - NotFound")
    @Test
    void deleteCustomerByNotExistId() throws Exception {
        // given
        long  customerId = 1000L;

        // when
        mvc.perform(
                        delete("/customers/{customerId}", customerId)
                )
                // then
                .andExpect(status().isNotFound())
                .andDo(
                        document(
                                "customer/delete/not-found",
                                resourceDetails()
                                        .tag("customer")
                        )
                );
    }
}