package com.commerce.domain.seller;

import com.commerce.domain.seller.dto.CreateSellerDto;
import com.commerce.domain.seller.dto.UpdateSellerDto;
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
class SellerControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired SellerRepository sellerRepository;
    Faker faker = new Faker();

    @DisplayName("GET - Ok")
    @Test
    void findSeller() throws Exception {
        // given
        Seller seller = Seller.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();
        sellerRepository.save(seller);
        // when
        mvc.perform(get("/sellers/" + seller.getId()))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(seller.getName())))
                .andExpect(jsonPath("address", is(seller.getAddress())))
                .andDo(
                        document("seller/get/ok",
                                builder().requestFields(
                                        fieldWithPath("username").description("이름"),
                                        fieldWithPath("address").description("주소")
                                )
                        )
                );
    }

    @DisplayName("GET - NotFound")
    @Test
    void findSellerByNotExistId() throws Exception {
        // given
        long sellerId = 1000L;

        // when
        mvc.perform(get("/sellers/" + sellerId))
                // then
                .andExpect(status().isNotFound())
                .andDo(document("seller/get/not-found"));
    }

    @DisplayName("POST - Created")
    @Test
    void createSeller() throws Exception {
        // given
        CreateSellerDto createSellerDto = CreateSellerDto.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();
        // when
        mvc.perform(
                post("/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSellerDto))
                )
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("sellerId" , notNullValue(Long.class)))
                .andDo(
                        document("seller/post/created",
                                builder()
                                        .requestFields(
                                            fieldWithPath("name").description("이름"),
                                            fieldWithPath("address").description("주소")
                                        )
                                        .responseFields(
                                                fieldWithPath("sellerId").description("판매자 고유값")
                                        )
                        )
                );
    }

    @DisplayName("POST - BadRequest ( empty name )")
    @Test
    void createSellerByEmptyName() throws Exception {
        // given
        CreateSellerDto createSellerDto = CreateSellerDto.builder()
                .name("")
                .address(faker.address().fullAddress())
                .build();
        // when
        mvc.perform(
                        post("/sellers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createSellerDto))
                )
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("field" , is("name")))
                .andDo(
                        document("seller/post/bad-request/empty/name",
                                builder()
                                        .requestFields(
                                                fieldWithPath("name").description("이름"),
                                                fieldWithPath("address").description("주소")
                                        )
                                        .responseFields(
                                                fieldWithPath("field").description("필드")
                                        )
                        )
                );
    }

    @DisplayName("POST - BadRequest ( empty address  )")
    @Test
    void createSellerByEmptyAddress() throws Exception {
        // given
        CreateSellerDto createSellerDto = CreateSellerDto.builder()
                .name(faker.name().fullName())
                .address("")
                .build();
        // when
        mvc.perform(
                        post("/sellers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createSellerDto))
                )
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("field" , is("address")))
                .andDo(
                        document("seller/post/bad-request/empty/address",
                                builder()
                                        .requestFields(
                                                fieldWithPath("name").description("이름"),
                                                fieldWithPath("address").description("주소")
                                        )
                                        .responseFields(
                                                fieldWithPath("code").description("에러 코드"),
                                                fieldWithPath("message").description("에러 메세지"),
                                                fieldWithPath("field").description("에러 필드")
                                        )
                        )
                );
    }

    @DisplayName("PATCH - NoContent")
    @Test
    void updateSeller() throws Exception {
        // given
        Seller seller = Seller.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();
        sellerRepository.save(seller);

        UpdateSellerDto updateSellerDto = UpdateSellerDto.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        // when
        mvc.perform(
                        patch("/sellers/" + seller.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateSellerDto))
                )
                // then
                .andExpect(status().isNoContent())
                .andDo(
                        document("seller/patch/no-content",
                                builder()
                                        .requestFields(
                                                fieldWithPath("name").description("이름"),
                                                fieldWithPath("address").description("주소")
                                        )
                        )
                );
    }

    @DisplayName("PATCH - NotFound")
    @Test
    void updateSellerByNotExistId() throws Exception {
        // given
        Long sellerId = 10000L;

        UpdateSellerDto updateSellerDto = UpdateSellerDto.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        // when
        mvc.perform(
                        patch("/sellers/" + sellerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateSellerDto))
                )
                // then
                .andExpect(status().isNotFound())
                .andDo(
                        document("seller/patch/not-found")
                );
    }

    @DisplayName("PATCH - BadRequest ( empty name )")
    @Test
    void updateSellerByEmptyName() throws Exception {
        // given
        Seller seller = Seller.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();
        sellerRepository.save(seller);

        UpdateSellerDto updateSellerDto = UpdateSellerDto.builder()
                .name("")
                .address(faker.address().fullAddress())
                .build();

        // when
        mvc.perform(
                        patch("/sellers/" + seller.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateSellerDto))
                )
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("field" , is("name")))
                .andDo(
                        document("seller/patch/bad-request/empty/name",
                                builder()
                                        .requestFields(
                                                fieldWithPath("name").description("이름"),
                                                fieldWithPath("address").description("주소")
                                        )
                                        .responseFields(
                                                fieldWithPath("field").description("필드")
                                        )
                        )
                );
    }

    @DisplayName("DELETE - NoContent")
    @Test
    void deleteSeller() throws Exception {
        // given
        Seller seller = Seller.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();
        sellerRepository.save(seller);

        // when
        mvc.perform(
                        delete("/sellers/" + seller.getId())
                )
                // then
                .andExpect(status().isNoContent())
                .andDo(
                        document("seller/delete/no-content")
                );

    }

    @DisplayName("DELETE - NotFound")
    @Test
    void deleteSellerByNotExistId() throws Exception {
        // given
        long sellerId = 1000L;

        // when
        mvc.perform(
                        delete("/sellers/" + sellerId)
                )
                // then
                .andExpect(status().isNotFound())
                .andDo(
                        document("seller/delete/not-found")
                );
    }
}