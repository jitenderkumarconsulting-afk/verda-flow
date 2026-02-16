package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.ProductPriceDetail;

@Repository
public interface ProductPriceDetailRepo extends JpaRepository<ProductPriceDetail, Integer> {

	/**
	 * This query to fetch the list of productPriceDetail by productId.
	 * 
	 * @param productId
	 * @return
	 */
	@Query(value = "SELECT p FROM ProductPriceDetail p WHERE p.product.id = :productId AND p.isDeleted = false")
	List<ProductPriceDetail> findByProductId(@Param("productId") int productId);

	/**
	 * This query to fetch the list of productPriceDetail ids by productId.
	 * 
	 * @param productId
	 * @return
	 */
	@Query(value = "SELECT p.id FROM ProductPriceDetail p WHERE p.product.id = :productId AND p.isDeleted = false")
	List<String> findIdsByProductId(@Param("productId") int productId);

	/**
	 * This query to fetch the list of productPriceDetails by productPriceDetailId
	 * and productId.
	 * 
	 * @param productPriceDetailId
	 * @param productId
	 * @return
	 */
	@Query(value = "SELECT p FROM ProductPriceDetail p WHERE p.product.id = :productId AND p.id = :productPriceDetailId AND p.isDeleted = false")
	ProductPriceDetail findByProductPriceDetailIdAndProductId(@Param("productPriceDetailId") int productPriceDetailId,
			@Param("productId") int productId);

	/**
	 * This query to fetch the list of productPriceDetail by priceType and
	 * productId.
	 * 
	 * @param priceType
	 * @param productId
	 * @return
	 */
	@Query(value = "SELECT p FROM ProductPriceDetail p WHERE p.priceType = :priceType AND p.product.id = :productId")
	ProductPriceDetail findByPriceTypeAndProductId(@Param("priceType") int priceType,
			@Param("productId") int productId);

}
