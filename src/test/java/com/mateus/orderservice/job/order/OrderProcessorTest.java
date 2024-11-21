package com.mateus.orderservice.job.order;

import com.mateus.orderservice.enums.OrderStatus;
import com.mateus.orderservice.model.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static com.mateus.orderservice.stubs.OrderStub.newOrder;
import static com.mateus.orderservice.stubs.OrderStub.orderWithErrors;

@ExtendWith(MockitoExtension.class)
class OrderProcessorTest {

    @InjectMocks
    private OrderProcessor orderProcessor;

    @Test
    void shouldProcessSuccessfully() throws Exception {
        Order order = newOrder();

        Assertions.assertThat(orderProcessor.process(order))
                .extracting(Order::getTotalAmount, Order::getOrderStatus)
                .containsExactly(BigDecimal.valueOf(15), OrderStatus.PROCESSED);
    }

    @Test
    void shouldProcessWithError() throws Exception {
        Order order = orderWithErrors();

        Assertions.assertThat(orderProcessor.process(order))
                .extracting(Order::getOrderStatus)
                .isEqualTo(OrderStatus.ERROR);
    }


}