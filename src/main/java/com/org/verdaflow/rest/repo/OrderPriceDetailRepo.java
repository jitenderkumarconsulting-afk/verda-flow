package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.OrderPriceDetail;

@Repository
public interface OrderPriceDetailRepo extends JpaRepository<OrderPriceDetail, Integer> {

}
