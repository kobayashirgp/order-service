package com.mateus.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mateus.orderservice.dto.OrderRequest;
import com.mateus.orderservice.dto.OrderResponse;
import com.mateus.orderservice.service.OrderService;
import com.mateus.orderservice.stubs.OrderStub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    private static final String BASE_URL = "/orders";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void shouldSaveOrder() throws Exception {
        doNothing().when(orderService).saveOrder(any());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(OrderStub.create())))
                .andExpect(status().isOk());

        verify(orderService).saveOrder(any());
    }

    @Test
    void shouldNotSaveOrderWhenPayloadIncorrect() throws Exception {
        doNothing().when(orderService).saveOrder(any());

        OrderRequest request = OrderStub.create();
        request.setProducts(List.of());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).saveOrder(any());
    }

    @Test
    void shouldFetchOrders() throws Exception {
        OrderResponse response = OrderStub.response();
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<OrderResponse> page = new PageImpl<>(List.of(response), pageable, 1);

        when(orderService.fetchOrders(0, 10))
                .thenReturn(page);

        mockMvc.perform(get(BASE_URL)
                        .queryParam("page", "0")
                        .queryParam("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].totalAmount").value("10"))
                .andExpect(jsonPath("$.content[0].orderStatus").value("PROCESSED"))
                .andExpect(jsonPath("$.page.totalPages").value("1"))
                .andExpect(jsonPath("$.page.totalElements").value("1"));

        verify(orderService).fetchOrders(anyInt(), anyInt());
    }

}