package com.mateus.orderservice.controller;

import com.mateus.orderservice.dto.OrderRequest;
import com.mateus.orderservice.dto.OrderResponse;
import com.mateus.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@Tag(name = "Order Controller")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("orders")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Save an order", description = "Save an order to be processed async")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public void saveOrder(@Validated @RequestBody OrderRequest request) {
        log.info("Receiving order with body {}", request);
        orderService.saveOrder(request);
        log.info("Order successfully saved {}", request);
    }

    @Operation(summary = "Get orders", description = "Return orders by page and size")
    @GetMapping
    public Page<OrderResponse> fetchOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Fetching orders with pagination page:{} size:{}", page, size);
        final var orders = orderService.fetchOrders(page, size);
        log.info("Returning orders data successfully {}", orders);

        return orders;
    }
}
