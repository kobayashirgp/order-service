package com.mateus.orderservice.repository;

import com.mateus.orderservice.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>, PagingAndSortingRepository<Order, Long> {

    @Query(value = "select o from Order o join fetch o.products p", countQuery = " select count(o) from Order o")
    Page<Order> search(Pageable pageable);

    boolean existsByExternalId(Long id);

}
