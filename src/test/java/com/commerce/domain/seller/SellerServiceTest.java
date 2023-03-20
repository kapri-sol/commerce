package com.commerce.domain.seller;

import com.commerce.domain.seller.dto.CreateSellerDto;
import com.commerce.domain.seller.dto.UpdateSellerDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class SellerServiceTest {
    @Autowired EntityManager entityManager;
    @Autowired SellerService sellerService;
    @Autowired SellerRepository sellerRepository;

    private final Faker faker = new Faker();

    @DisplayName("판매자를 검색한다.")
    @Test
    void findSellerById() {
        // given
        Seller seller = Seller.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        sellerRepository.save(seller);

        // when
        Seller findSeller = sellerService.findSellerById(seller.getId());

        // then
        assertThat(findSeller).isEqualTo(seller);
    }

    @DisplayName("존재하지 않는 판매자를 검색한다.")
    @Test
    void findSellerByNotExistId() {
        // given
        Long sellerId = 10000L;

        // when
        assertThatThrownBy(() ->sellerService.findSellerById(sellerId))
                // then
                .isInstanceOf(NoResultException.class);
    }

    @DisplayName("판매자를 생성한다.")
    @Test
    void createSeller() {
        // given
        CreateSellerDto createSellerDto = CreateSellerDto.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();
        // when
        Long sellerId = sellerService.createSeller(createSellerDto);

        Optional<Seller> optionalSeller = sellerRepository.findById(sellerId);
        Seller seller = optionalSeller.orElse(null);

        // then
        assertThat(optionalSeller).isPresent();
        assertThat(seller.getId()).isEqualTo(sellerId);
        assertThat(seller.getName()).isEqualTo(createSellerDto.getName());
        assertThat(seller.getAddress()).isEqualTo(createSellerDto.getAddress());
    }

    @DisplayName("판매자 정보를 수정한다.")
    @Test
    void updateSeller() {
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
        sellerService.updateSeller(seller.getId(), updateSellerDto);

        Seller updatedSeller = sellerRepository.findById(seller.getId()).get();

        // then
        assertThat(updatedSeller).isNotNull();
        assertThat(updatedSeller.getName()).isEqualTo(updateSellerDto.getName());
        assertThat(updatedSeller.getAddress()).isEqualTo(updateSellerDto.getAddress());
    }

    @DisplayName("존재하지 않는 판매자 정보를 수정한다.")
    @Test
    void updateSellerByNotExistId() {
        // given
        Long sellerId = 10000L;
        UpdateSellerDto updateSellerDto = UpdateSellerDto.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        // when
        assertThatThrownBy(() -> {
            sellerService.updateSeller(sellerId, updateSellerDto);
            // then
        }).isInstanceOf(NoResultException.class);
    }

    @DisplayName("판매자를 삭제한다.")
    @Test
    void deleteSellerById() {
        // given
        Seller seller = Seller.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        sellerRepository.save(seller);

        // when
        sellerService.deleteSellerById(seller.getId());
        sellerRepository.flush();
        entityManager.clear();

        Optional<Seller> optionalSeller = sellerRepository.findById(seller.getId());

        // then
        assertThat(optionalSeller).isEmpty();
    }

    @DisplayName("존재하지 않는 판매자를 삭제한다.")
    @Test
    void deleteSellerByNotExistId() {
        // given
        Long sellerId = 1000L;

        // when
        assertThatThrownBy(() -> {
            sellerService.deleteSellerById(sellerId);
            // then
        }).isInstanceOf(NoResultException.class);
    }
}