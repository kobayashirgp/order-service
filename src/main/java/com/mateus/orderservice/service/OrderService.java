package com.mateus.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mateus.orderservice.dto.OrderRequest;
import com.mateus.orderservice.dto.OrderResponse;
import com.mateus.orderservice.enums.OrderStatus;
import com.mateus.orderservice.exception.OrderAlreadyExistsException;
import com.mateus.orderservice.model.Order;
import com.mateus.orderservice.model.Product;
import com.mateus.orderservice.repository.OrderRepository;
import com.mateus.orderservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Transactional
    public void saveOrder(OrderRequest request) {
        checkIfOrderExists(request);

        var newOrder = Order.builder()
                .orderStatus(OrderStatus.RECEIVED)
                .createdDate(ZonedDateTime.now())
                .lastModifiedDate(ZonedDateTime.now())
                .externalId(request.getExternalId())
                .build();

        var newProducts = request.getProducts()
                .stream()
                .map(p -> {
                    Product product = objectMapper.convertValue(p, Product.class);
                    product.setOrder(newOrder);
                    return product;
                })
                .toList();

        orderRepository.save(newOrder);
        productRepository.saveAll(newProducts);
    }

    private void checkIfOrderExists(OrderRequest request) {
        if (orderRepository.existsByExternalId(request.getExternalId())) {
            log.warn("Order already exists on database");
            throw new OrderAlreadyExistsException();
        }
    }

    public Page<OrderResponse> fetchOrders(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable paging = PageRequest.of(page, size, sort);
        return orderRepository.search(paging)
                .map(order -> objectMapper.convertValue(order, OrderResponse.class));
    }
}
