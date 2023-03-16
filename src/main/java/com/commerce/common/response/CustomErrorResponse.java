package com.commerce.common.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomErrorResponse {
    private String code;
    private  String field;
    private String message;
}
