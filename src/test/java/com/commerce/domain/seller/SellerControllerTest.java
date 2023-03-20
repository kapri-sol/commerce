package com.commerce.domain.seller;

import com.commerce.domain.seller.dto.CreateSellerDto;
import com.commerce.domain.seller.dto.UpdateSellerDto;
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

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
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
        mvc.perform(get("/sellers/{sellerId}", seller.getId()))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(seller.getName())))
                .andExpect(jsonPath("address", is(seller.getAddress())))
                .andDo(
                        document("seller/get/ok",
                                resourceDetails()
                                        .tag("seller")
                                        .description("판매자를 검색한다.")
                                        .responseSchema(new Schema("FindSellerResponse")),
                                responseFields(
                                        fieldWithPath("name").description("이름"),
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
        mvc.perform(get("/sellers/{sellerId}", sellerId))
                // then
                .andExpect(status().isNotFound())
                .andDo(document("seller/get/not-found", resourceDetails().tag("seller")));
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
                                resourceDetails()
                                        .tag("seller")
                                        .description("판매자를 생성한다.")
                                        .requestSchema(new Schema("CreateSellerDto"))
                                        .responseSchema(new Schema("CreateSellerResponse")),
                                requestFields(
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("address").description("주소")
                                ),
                                responseFields(
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
                .andExpect(jsonPath("code" , notNullValue(String.class)))
                .andExpect(jsonPath("field" , is("name")))
                .andExpect(jsonPath("message" , notNullValue(String.class)))
                .andDo(
                        document("seller/post/bad-request/empty/name",
                                resourceDetails()
                                        .tag("seller")
                                        .requestSchema(new Schema("CreateSellerDto") )
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
                .andExpect(jsonPath("code" , notNullValue(String.class)))
                .andExpect(jsonPath("field" , is("address")))
                .andExpect(jsonPath("message" , notNullValue(String.class)))
                .andDo(
                        document("seller/post/bad-request/empty/address",
                                resourceDetails()
                                        .tag("seller")
                                        .requestSchema(new Schema("CreateSellerDto") )
                                        .responseSchema(new Schema("Error")),
                                requestFields(
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("address").description("주소")
                                ),
                                responseFields(
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
                        patch("/sellers/{sellerId}", seller.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateSellerDto))
                )
                // then
                .andExpect(status().isNoContent())
                .andDo(
                        document("seller/patch/no-content",
                                resourceDetails()
                                        .tag("seller")
                                        .description("판매자 정보를 수정한다.")
                                        .requestSchema(new Schema("UpdateSellerDto")),
                                requestFields(
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
                        patch("/sellers/{sellerId}", sellerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateSellerDto))
                )
                // then
                .andExpect(status().isNotFound())
                .andDo(
                        document("seller/patch/not-found", resourceDetails().tag("seller"))
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
                        patch("/sellers/{sellerId}", seller.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateSellerDto))
                )
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("field" , is("name")))
                .andExpect(jsonPath("code" , notNullValue(String.class)))
                .andExpect(jsonPath("message" , notNullValue(String.class)))
                .andDo(
                        document("seller/patch/bad-request/empty/name",
                                resourceDetails()
                                        .tag("seller")
                                        .requestSchema(new Schema("UpdateSellerDto"))
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
    void updateSellerByEmptyAddress() throws Exception {
        // given
        Seller seller = Seller.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        sellerRepository.save(seller);

        UpdateSellerDto updateSellerDto = UpdateSellerDto.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .address("")
                .build();

        // when
        mvc.perform(
                        patch("/sellers/{sellerId}", seller.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateSellerDto))
                )
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("field" , is("address")))
                .andExpect(jsonPath("code" , notNullValue(String.class)))
                .andExpect(jsonPath("message" , notNullValue(String.class)))
                .andDo(
                        document("seller/patch/bad-request/empty/name",
                                resourceDetails()
                                        .tag("seller")
                                        .requestSchema(new Schema("UpdateSellerDto"))
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
    void deleteSeller() throws Exception {
        // given
        Seller seller = Seller.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();
        sellerRepository.save(seller);

        // when
        mvc.perform(
                        delete("/sellers/{sellerId}", seller.getId())
                )
                // then
                .andExpect(status().isNoContent())
                .andDo(
                        document(
                                "seller/delete/no-content",
                                resourceDetails()
                                        .tag("seller")
                                        .description("판매자를 삭제한다.")
                        )
                );

    }

    @DisplayName("DELETE - NotFound")
    @Test
    void deleteSellerByNotExistId() throws Exception {
        // given
        long sellerId = 1000L;

        // when
        mvc.perform(
                        delete("/sellers/{sellerId}", sellerId)
                )
                // then
                .andExpect(status().isNotFound())
                .andDo(
                        document("seller/delete/not-found", resourceDetails().tag("seller"))
                );
    }
}