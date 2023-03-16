package com.commerce.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

public class ErrorResponseUtil {
    public static ResponseEntity<ErrorResponse> badRequest () {
//        final ErrorResponse errorResponse = ErrorResponse.builder()
//                .title("")
//                .titleMessageCode("")
//                .detailMessageCode("")
//                .build();

        return ResponseEntity.badRequest().body(null);
    }
}
