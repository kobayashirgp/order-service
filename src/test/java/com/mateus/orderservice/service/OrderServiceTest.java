package com.mateus.orderservice.service;

import com.mateus.orderservice.enums.OrderStatus;
import com.mateus.orderservice.exception.OrderAlreadyExistsException;
import com.mateus.orderservice.model.Order;
import com.mateus.orderservice.repository.OrderRepository;
import com.mateus.orderservice.repository.ProductRepository;
import com.mateus.orderservice.stubs.OrderStub;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;

    @Test
    void shouldSaveOrderSuccessfully() {
        when(orderRepository.existsByExternalId(any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertThatCode(() -> orderService.saveOrder(OrderStub.create()))
                .doesNotThrowAnyException();

        verify(orderRepository).save(orderArgumentCaptor.capture());
        verify(productRepository).saveAll(anyList());

        Assertions.assertThat(orderArgumentCaptor.getValue())
                .extracting(Order::getOrderStatus, Order::getExternalId)
                .containsExactly(OrderStatus.RECEIVED, 20L);
    }

    @Test
    void shouldNotSaveOrderWhenOrderIdAlreadyExists() {
        when(orderRepository.existsByExternalId(any()))
                .thenReturn(Boolean.TRUE);

        Assertions.assertThatCode(() -> orderService.saveOrder(OrderStub.create()))
                .isInstanceOf(OrderAlreadyExistsException.class)
                .hasMessage("Order already exists on database");

        verify(orderRepository, never()).save(any());
        verify(productRepository, never()).saveAll(anyList());
    }

    @Test
    void shouldFetchOrders() {
        var model = OrderStub.newOrder();
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Order> modelPage = new PageImpl<>(List.of(model), pageable, 1);

        when(orderRepository.search(any())).thenReturn(modelPage);

        Assertions.assertThat(orderService.fetchOrders(0, 10).getContent())
                .hasSize(1);

        verify(orderRepository).search(any());
    }
}