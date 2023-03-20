package com.commerce.domain.seller;

import com.commerce.domain.seller.dto.CreateSellerDto;
import com.commerce.domain.seller.dto.UpdateSellerDto;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    public Seller findSellerById(Long id) {
        return sellerRepository.findById(id).orElseThrow(NoResultException::new);
    }

    public Long createSeller(CreateSellerDto createSellerDto) {
        Seller seller = Seller.builder()
                .name(createSellerDto.getName())
                .address(createSellerDto.getAddress())
                .build();

        return sellerRepository.save(seller).getId();
    }

    public void updateSeller(Long id, UpdateSellerDto updateSellerDto) {
        Seller seller = sellerRepository.findById(id).orElseThrow(NoResultException::new);

        if (updateSellerDto.getName() != null) {
           seller.setName(updateSellerDto.getName());
        }

        if(updateSellerDto.getAddress() != null) {
            seller.setAddress(updateSellerDto.getAddress());
        }

        sellerRepository.save(seller);
    }

    public void deleteSellerById(Long id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(NoResultException::new);
        seller.delete();
    }

}
