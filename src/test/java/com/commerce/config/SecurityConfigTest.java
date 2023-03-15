package com.commerce.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SecurityConfigTest {
    @Autowired private MockMvc mvc;

    @Test
    void authentication() throws Exception {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/"));
        int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("Http Status Code : " + httpStatusCode);
    }

    void authorization() {}
}