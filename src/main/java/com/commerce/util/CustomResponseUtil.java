package com.commerce.util;

import com.commerce.common.response.CustomErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Slf4j
public class CustomResponseUtil {
    static public void unAuthentication(HttpServletResponse response, String message){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                    .code("UnAuthenticated").message(message).build();
            String responseBody = objectMapper.writeValueAsString(errorResponse);
            response.setContentType(MediaType.APPLICATION_JSON.toString());
            response.setStatus(HttpStatus.FORBIDDEN.value());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
