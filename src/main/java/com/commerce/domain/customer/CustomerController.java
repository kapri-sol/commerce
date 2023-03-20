package com.commerce.domain.customer;

import com.commerce.domain.customer.dto.CreateCustomerDto;
import com.commerce.domain.customer.dto.CreateCustomerResponse;
import com.commerce.domain.customer.dto.FindCustomerResponse;
import com.commerce.domain.customer.dto.UpdateCustomerDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("customers")
@RequiredArgsConstructor
@RestController
public class CustomerController {
    private final CustomerService customerService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    FindCustomerResponse findCustomerById(@PathVariable("id") Long customerId) {
        Customer customer = customerService.findCustomerById(customerId);

        return FindCustomerResponse.builder()
                .name(customer.getName())
                .address(customer.getAddress())
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    CreateCustomerResponse createCustomer(@RequestBody @Valid CreateCustomerDto createCustomerDto) {
        Long customerId = customerService.createCustomer(createCustomerDto);

        return CreateCustomerResponse.builder()
                .customerId(customerId)
                .build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("{id}")
    void updateCustomer(@PathVariable("id") Long customerId, @RequestBody @Valid UpdateCustomerDto updateCustomerDto) {
        customerService.updateCustomer(customerId, updateCustomerDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    void deleteCustomerById(@PathVariable("id") Long customerId) {
        customerService.deleteCustomerById(customerId);
    }
}
