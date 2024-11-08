package com.mateus.orderservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderAlreadyExistsException extends RuntimeException {

    public OrderAlreadyExistsException() {
        super("Order already exists on database");
    }
}
