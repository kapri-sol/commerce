package com.commerce.domain.customer;

import com.commerce.domain.customer.dto.CreateCustomerDto;
import com.commerce.domain.customer.dto.UpdateCustomerDto;
import com.commerce.domain.seller.Seller;
import com.commerce.domain.seller.dto.CreateSellerDto;
import com.commerce.domain.seller.dto.UpdateSellerDto;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(NoResultException::new);
    }

    public Long createCustomer(CreateCustomerDto createCustomerDto) {
        Customer customer = Customer.builder()
                .name(createCustomerDto.getName())
                .address(createCustomerDto.getAddress())
                .build();

        return customerRepository.save(customer).getId();
    }

    public void updateCustomer(Long id, UpdateCustomerDto updateCustomerDto) {
        Customer customer = customerRepository.findById(id).orElseThrow(NoResultException::new);

        if (updateCustomerDto.getName() != null) {
            customer.setName(updateCustomerDto.getName());
        }

        if(updateCustomerDto.getAddress() != null) {
            customer.setAddress(updateCustomerDto.getAddress());
        }

        customerRepository.save(customer);
    }

    public void deleteCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(NoResultException::new);
        customer.delete();
    }
}
