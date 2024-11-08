package com.mateus.orderservice.dto;

import com.mateus.orderservice.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private Long externalId;
    private ZonedDateTime createdDate;
    private OrderStatus orderStatus;
    private ZonedDateTime lastModifiedDate;
    private BigDecimal totalAmount;
    private List<ProductResponse> products;

}
