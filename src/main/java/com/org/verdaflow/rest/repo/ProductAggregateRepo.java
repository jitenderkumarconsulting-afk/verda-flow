package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.common.enums.ProductAggregateType;
import com.org.verdaflow.rest.entity.ProductAggregate;

@Repository
public interface ProductAggregateRepo extends JpaRepository<ProductAggregate, Integer> {

	/**
	 * This query to check the existence of Product Aggregate by productId and
	 * productAggregateType.
	 * 
	 * @param productId
	 * @param aggregateType
	 * @return
	 */
	@Query("SELECT a FROM ProductAggregate a WHERE a.product.id = :productId AND a.productAggregateType = :aggregateType AND a.isDeleted = false")
	ProductAggregate findByProductIdAndProductAggregateType(@Param("productId") int productId,
			@Param("aggregateType") ProductAggregateType aggregateType);

	// /**
	// * This query to find the count of Product Aggregate by productId and
	// * productAggregateType.
	// *
	// * @param productId
	// * @param aggregateType
	// * @return
	// */
	// @Query("SELECT a.count FROM ProductAggregate a WHERE a.product.id =
	// :productId AND a.productAggregateType = :aggregateType AND a.isDeleted =
	// false")
	// int findCountByProductIdAndProductAggregateType(@Param("productId") int
	// productId,
	// @Param("aggregateType") ProductAggregateType aggregateType);

}
