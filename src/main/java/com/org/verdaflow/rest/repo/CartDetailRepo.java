package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.CartDetail;

@Repository
public interface CartDetailRepo extends CrudRepository<CartDetail, Integer> {

	/**
	 * This query is to populate the list of Carts by userId.
	 * 
	 * @param userId
	 * @return
	 */
	@Query(value = "SELECT c FROM CartDetail c WHERE c.user.id = :userId AND c.isDeleted = false")
	List<CartDetail> findAllByUserId(@Param("userId") int userId);

	/**
	 * This query is to populate the list of those Carts for which product exists by
	 * userId .
	 * 
	 * @param userId
	 * @return
	 */
	@Query(value = "SELECT c FROM CartDetail c, Product p WHERE c.product.id = p.id AND c.user.id = :userId AND c.isDeleted = false")
	List<CartDetail> findAllByUserIdAndProduct(@Param("userId") int userId);

	/**
	 * This query is to fetch the Cart's count for which product exists by userId.
	 * 
	 * @param userId
	 * @return
	 */
	@Query(value = "SELECT COUNT(c) FROM CartDetail c, Product p WHERE c.user.id = :userId AND c.product.id = p.id AND c.isDeleted = false")
	int findCartsCountByUserIdAndProduct(@Param("userId") int userId);

	/**
	 * This query is to fetch the Cart's count by userId.
	 * 
	 * @param userId
	 * @return
	 */
	@Query(value = "SELECT COUNT(c) FROM CartDetail c WHERE c.user.id = :userId AND c.isDeleted = false")
	int findCartsCountByUserId(@Param("userId") int userId);

	/**
	 * This query is to find the Cart details by cartId and userId.
	 * 
	 * @param cartId
	 * @param userId
	 * @return
	 */
	@Query(value = "SELECT c FROM CartDetail c WHERE c.id = :cartId AND c.user.id = :userId AND c.isDeleted = false")
	CartDetail findByCartIdAndUserId(@Param("cartId") int cartId, @Param("userId") int userId);

	/**
	 * This query to populate the list of Carts by userId except current dispatcher
	 * id of product.
	 * 
	 * @param userId
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT c FROM CartDetail c, Product p WHERE c.product.id = p.id AND c.user.id = :userId AND p.userDispatcherDetail.id != :dispatcherId AND c.isDeleted = false")
	List<CartDetail> findCartDetailByUserIdExceptCurrentDispatcherId(@Param("userId") int userId,
			@Param("dispatcherId") int dispatcherId);

	/**
	 * This query is to find the Cart details by productId And priceType.
	 * 
	 * @param productId
	 * @param priceType
	 * @return
	 */
	@Query(value = "SELECT c FROM CartDetail c, ProductPriceDetail p WHERE c.productPriceDetail.id = p.id AND c.product.id = :productId AND p.priceType = :priceType AND c.isDeleted = false")
	List<CartDetail> findByProductIdAndPriceDetailIdAndPriceType(@Param("productId") int productId,
			@Param("priceType") int priceType);

}
