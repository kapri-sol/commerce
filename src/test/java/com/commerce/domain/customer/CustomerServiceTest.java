package com.commerce.domain.customer;

import com.commerce.domain.customer.dto.CreateCustomerDto;
import com.commerce.domain.customer.dto.UpdateCustomerDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class CustomerServiceTest {
    @Autowired
    EntityManager entityManager;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerRepository customerRepository;

    final Faker faker = new Faker();

    @DisplayName("고객을 검색한다.")
    @Test
    void findByCustomerId() {
        // given
        Customer customer = Customer.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        customerRepository.save(customer);

        // when
        Customer findCustomer = customerService.findCustomerById(customer.getId());

        // then
        assertThat(findCustomer).isEqualTo(customer);
    }

    @DisplayName("존재하지 않는 고객을 검색한다.")
    @Test
    void findCustomerByNotExistId() {
        // given
        Long customerId = 10000L;

        // when
        assertThatThrownBy(() ->customerService.findCustomerById(customerId))
                // then
                .isInstanceOf(NoResultException.class);
    }

    @DisplayName("고객을 생성한다.")
    @Test
    void createCustomer() {
        // given
        CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        // when
        Long customerId = customerService.createCustomer(createCustomerDto);

        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        Customer customer = optionalCustomer.orElse(null);

        // then
        assertThat(optionalCustomer).isPresent();
        assertThat(customer.getId()).isEqualTo(customerId);
        assertThat(customer.getName()).isEqualTo(createCustomerDto.getName());
        assertThat(customer.getAddress()).isEqualTo(createCustomerDto.getAddress());
    }

    @DisplayName("고객 정보를 수정한다.")
    @Test
    void updateCustomer() {
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
        customerService.updateCustomer(customer.getId(), updateCustomerDto);

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();

        // then
        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getName()).isEqualTo(updateCustomerDto.getName());
        assertThat(updatedCustomer.getAddress()).isEqualTo(updateCustomerDto.getAddress());
    }

    @DisplayName("존재하지 않는 고객 정보를 수정한다.")
    @Test
    void updateCustomerByNotExistId() {
        // given
        Long customerId = 10000L;
        UpdateCustomerDto updateCustomerDto = UpdateCustomerDto.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        // when
        assertThatThrownBy(() -> {
            customerService.updateCustomer(customerId, updateCustomerDto);
            // then
        }).isInstanceOf(NoResultException.class);
    }

    @DisplayName("고객을 삭제한다.")
    @Test
    void deleteCustomerById() {
        // given
        Customer customer = Customer.builder()
                .name(faker.name().fullName())
                .address(faker.address().fullAddress())
                .build();

        customerRepository.save(customer);

        // when
        customerService.deleteCustomerById(customer.getId());
        customerRepository.flush();
        entityManager.clear();

        Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());

        // then
        assertThat(optionalCustomer).isEmpty();
    }

    @DisplayName("존재하지 않는 고개을 삭제한다.")
    @Test
    void deleteCustomerByNotExistId() {
        // given
        Long customerId = 1000L;

        // when
        assertThatThrownBy(() -> {
            customerService.deleteCustomerById(customerId);
            // then
        }).isInstanceOf(NoResultException.class);
    }
}