package com.mateus.orderservice.job.order;

import com.mateus.orderservice.enums.OrderStatus;
import com.mateus.orderservice.model.Order;
import com.mateus.orderservice.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Slf4j
@Component
public class OrderProcessor implements ItemProcessor<Order, Order> {

    @Override
    public Order process(Order item) throws Exception {
        log.info("Processing order {}", item);

        try {
            var totalAmount = item.getProducts()
                    .stream()
                    .map(Product::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return item
                    .toBuilder()
                    .orderStatus(OrderStatus.PROCESSED)
                    .lastModifiedDate(ZonedDateTime.now())
                    .totalAmount(totalAmount)
                    .build();
        } catch (Exception exception) {
            log.error(String.format("An error occurred when processing order %s, skipping", item), exception);
            return item
                    .toBuilder()
                    .orderStatus(OrderStatus.ERROR)
                    .lastModifiedDate(ZonedDateTime.now())
                    .build();
        }
    }
}
