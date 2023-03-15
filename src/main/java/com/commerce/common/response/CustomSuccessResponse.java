package com.commerce.common.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomSuccessResponse<T>{
    private String code;
    private String message;
    private T data;
}
