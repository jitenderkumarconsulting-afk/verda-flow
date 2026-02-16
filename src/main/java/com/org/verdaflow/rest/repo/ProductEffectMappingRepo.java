package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.ProductEffectMapping;

@Repository
public interface ProductEffectMappingRepo extends JpaRepository<ProductEffectMapping, Integer> {

	/**
	 * This query to fetch the list of productEffectMapping by productId.
	 * 
	 * @param productId
	 * @return
	 */
	@Query(value = "SELECT pem FROM ProductEffectMapping pem WHERE pem.product.id = :productId AND pem.isDeleted = false")
	List<ProductEffectMapping> findByProductId(@Param("productId") int productId);

}
