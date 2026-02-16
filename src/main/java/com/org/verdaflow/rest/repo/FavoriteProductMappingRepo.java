package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.FavoriteProductMapping;

@Repository
public interface FavoriteProductMappingRepo extends JpaRepository<FavoriteProductMapping, Integer> {

	/**
	 * This query to fetch the details on the basis of userId and productId.
	 * 
	 * @param userId
	 * @param productId
	 * @return
	 */
	@Query("SELECT f FROM FavoriteProductMapping f WHERE f.user.id = :userId AND f.product.id = :productId AND f.isDeleted = false")
	FavoriteProductMapping findByUserIdAndProductId(@Param("userId") int userId, @Param("productId") int productId);

}
