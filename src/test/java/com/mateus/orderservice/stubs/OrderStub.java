package com.mateus.orderservice.stubs;

import com.mateus.orderservice.dto.OrderRequest;
import com.mateus.orderservice.dto.OrderResponse;
import com.mateus.orderservice.dto.ProductRequest;
import com.mateus.orderservice.dto.ProductResponse;
import com.mateus.orderservice.enums.OrderStatus;
import com.mateus.orderservice.model.Order;
import com.mateus.orderservice.model.Product;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderStub {

    public static OrderRequest create() {
        return OrderRequest.builder()
                .externalId(20L)
                .products(List.of(
                        ProductRequest.builder()
                                .amount(BigDecimal.valueOf(20L))
                                .description("Product one")
                                .build(),
                        ProductRequest.builder()
                                .amount(BigDecimal.valueOf(10L))
                                .description("Product two")
                                .build()))
                .build();
    }

    public static OrderResponse response() {
        return OrderResponse.builder()
                .orderStatus(OrderStatus.PROCESSED)
                .products(List.of(ProductResponse.builder()
                                .amount(BigDecimal.ONE)
                                .description("Product one")
                                .id(1L)
                        .build()))
                .createdDate(ZonedDateTime.now())
                .lastModifiedDate(ZonedDateTime.now())
                .id(1L)
                .totalAmount(BigDecimal.TEN)
                .externalId(10L)
                .build();
    }

    public static Order orderWithErrors() {
        return Order.builder()
                .products(List.of(
                        Product.builder()
                                .amount(null)
                                .build()
                ))
                .build();
    }

    public static Order newOrder() {
        return Order.builder()
                .id(1L)
                .externalId(10L)
                .orderStatus(OrderStatus.RECEIVED)
                .createdDate(ZonedDateTime.now())
                .lastModifiedDate(ZonedDateTime.now())
                .products(List.of(
                        Product.builder()
                                .amount(BigDecimal.valueOf(5))
                                .build(),
                        Product.builder()
                                .amount(BigDecimal.valueOf(5))
                                .build(),
                        Product.builder()
                                .amount(BigDecimal.valueOf(5))
                                .build()
                ))
                .build();
    }
}
