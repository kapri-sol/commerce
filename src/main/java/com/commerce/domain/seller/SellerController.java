package com.commerce.domain.seller;

import com.commerce.domain.seller.dto.CreateSellerDto;
import com.commerce.domain.seller.dto.CreateSellerResponse;
import com.commerce.domain.seller.dto.FindSellerResponse;
import com.commerce.domain.seller.dto.UpdateSellerDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("sellers")
@RestController
public class SellerController {
    private final SellerService sellerService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    FindSellerResponse findSeller(@PathVariable("id") Long sellerId) {
        Seller seller = sellerService.findSellerById(sellerId);

        return FindSellerResponse.builder()
                .name(seller.getName())
                .address(seller.getAddress())
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    CreateSellerResponse createSeller(@RequestBody @Valid CreateSellerDto createSellerDto) {
        Long sellerId = sellerService.createSeller(createSellerDto);
        return CreateSellerResponse.builder()
                .sellerId(sellerId)
                .build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("{id}")
    void updateSeller(@PathVariable("id") Long sellerId , @RequestBody @Valid UpdateSellerDto updateSellerDto) {
        sellerService.updateSeller(sellerId, updateSellerDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    void deleteSeller(@PathVariable("id") Long sellerId) {
        sellerService.deleteSellerById(sellerId);
    }
}
