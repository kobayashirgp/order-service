package com.mateus.orderservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mateus.orderservice.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "order_cart")
@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
@ToString(of = "id")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long externalId;

    @Column(name = "created_date", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "last_modified_date", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private ZonedDateTime lastModifiedDate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @JsonManagedReference
    private List<Product> products;

}
