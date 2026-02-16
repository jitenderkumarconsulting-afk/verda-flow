package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

	/**
	 * This query to fetch product details by product id.
	 * 
	 * @param productId
	 * @return
	 */
	@Query("SELECT p FROM Product p WHERE p.id = :productId AND p.isDeleted = false")
	Product findByProductId(@Param("productId") int productId);

	/**
	 * This query is to check the existence of Product by name and dispatcherId.
	 * 
	 * @param name
	 * @param dispatcherId
	 * @return
	 */
	@Query(value = "SELECT COUNT(p) FROM Product p WHERE p.name = :name AND p.userDispatcherDetail.id = :dispatcherId AND p.isDeleted = false")
	int checkProductExistenceByNameAndDispatcherId(@Param("name") String name, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query to check the existence of Product by name and dispatcherId except
	 * current.
	 * 
	 * @param name
	 * @param dispatcherId
	 * @param productId
	 * @return
	 */
	@Query("SELECT COUNT(p) FROM Product p WHERE p.name = :name AND p.userDispatcherDetail.id = :dispatcherId AND p.id != :productId AND p.isDeleted = false")
	int checkProductExistenceByNameAndDispatcherIdExceptCurrent(@Param("name") String name,
			@Param("dispatcherId") int dispatcherId, @Param("productId") int productId);

	/**
	 * This query is to populate the list of Products by dispatcherId
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @return
	 */
	@Query(value = "SELECT p FROM Product p WHERE p.userDispatcherDetail.id = :dispatcherId AND p.isDeleted = false")
	Page<Product> findAllProductsByDispatcherId(Pageable pageable, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query is to populate the list of Products by dispatcherId (With Search).
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param query
	 * @return
	 */
	@Query(value = "SELECT p FROM Product p WHERE p.userDispatcherDetail.id = :dispatcherId AND p.name LIKE :query AND p.isDeleted = false")
	Page<Product> findAllProductsByDispatcherIdSearch(Pageable pageable, @Param("dispatcherId") int dispatcherId,
			@Param("query") String query);

	/**
	 * This query is to populate the list of Products by dispatcherId and groupType
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param groupType
	 * @return
	 */
	@Query(value = "SELECT p FROM Product p WHERE p.userDispatcherDetail.id = :dispatcherId AND p.groupType = :groupType AND p.isDeleted = false")
	Page<Product> findAllProductsByDispatcherIdAndGroupType(Pageable pageable, @Param("dispatcherId") int dispatcherId,
			@Param("groupType") int groupType);

	/**
	 * This query is to populate the list of Products by dispatcherId and groupType
	 * (With Search).
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param groupType
	 * @param query
	 * @return
	 */
	@Query(value = "SELECT p FROM Product p WHERE p.userDispatcherDetail.id = :dispatcherId AND p.groupType = :groupType AND p.name LIKE :query AND p.isDeleted = false")
	Page<Product> findAllProductsByDispatcherIdAndGroupTypeSearch(Pageable pageable,
			@Param("dispatcherId") int dispatcherId, @Param("groupType") int groupType, @Param("query") String query);

	/**
	 * This query is to populate the list of active Products by dispatcherId and
	 * typeId.
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param typeId
	 * @return
	 */
	@Query(value = "SELECT p FROM Product p WHERE p.userDispatcherDetail.id = :dispatcherId AND p.type.id = :typeId AND p.active = true AND p.isDeleted = false")
	Page<Product> findAllActiveProductsByDispatcherIdAndTypeId(Pageable pageable,
			@Param("dispatcherId") int dispatcherId, @Param("typeId") int typeId);

	/**
	 * This query is to populate the list of active Products by dispatcherId and
	 * typeId (With Search).
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param typeId
	 * @param query
	 * @return
	 */
	@Query(value = "SELECT p FROM Product p WHERE p.userDispatcherDetail.id = :dispatcherId AND p.type.id = :typeId AND p.name LIKE :query AND p.active = true AND p.isDeleted = false")
	Page<Product> findAllActiveProductsByDispatcherIdAndTypeIdSearch(Pageable pageable,
			@Param("dispatcherId") int dispatcherId, @Param("typeId") int typeId, @Param("query") String query);

	/**
	 * This query is to find the Product details by productId and dispatcherId.
	 * 
	 * @param productId
	 * @param dispatcherId
	 * @return
	 */
	@Query(value = "SELECT p FROM Product p WHERE p.id = :productId AND p.userDispatcherDetail.id = :dispatcherId AND p.isDeleted = false")
	Product findProductByIdAndDispatcherId(@Param("productId") int productId, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query to fetch all the favorite Products list on the basis of user id of
	 * customer.
	 *
	 * @param pageable
	 * @param userId
	 * @return
	 */
	@Query("SELECT p FROM Product p, FavoriteProductMapping f WHERE p.id = f.product.id AND f.user.id = :userId AND f.isFav = true AND p.isDeleted = false ORDER BY p.name")
	Page<Product> findAllFavoriteProductsByUserIdOrderByName(Pageable pageable, @Param("userId") int userId);

	/**
	 * This query to fetch all the Products list on the basis of productIds and
	 * dispatcherId and promoCodeId.
	 *
	 * @param productsIds
	 * @param dispatcherId
	 * @param promoCodeId
	 * @return
	 */
	@Query("SELECT p FROM Product p, PromoCode c WHERE p.promoCode.id = c.id AND p.id IN (:productsIds) AND p.userDispatcherDetail.id = :dispatcherId AND c.id = :promoCodeId AND p.isDeleted = false AND c.isDeleted = false")
	List<Product> getProductsByIdsAndDispatcherIdAndPromoCodeId(@Param("productsIds") List<Integer> productsIds,
			@Param("dispatcherId") int dispatcherId, @Param("promoCodeId") int promoCodeId);

	/**
	 * This query is to populate the list of in stock driver inventory Products by
	 * dispatcherId and driverId.
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param driverId
	 * @return
	 */
	@Query(value = "SELECT p FROM Product p, DriverInventory i WHERE p.id = i.product.id AND p.userDispatcherDetail.id = :dispatcherId AND i.userDriverDetail.id = :driverId AND i.isInStock = true AND p.isDeleted = false AND i.isDeleted = false")
	Page<Product> findAllInStockDriverInventoryProductsByDispatcherIdAndDriverId(Pageable pageable,
			@Param("dispatcherId") int dispatcherId, @Param("driverId") int driverId);

	/**
	 * This query is to populate the list of in stock driver inventory Products by
	 * dispatcherId and driverId (With Search).
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param driverId
	 * @param query
	 * @return
	 */
	@Query(value = "SELECT p FROM Product p, DriverInventory i WHERE p.id = i.product.id AND p.userDispatcherDetail.id = :dispatcherId AND i.userDriverDetail.id = :driverId AND i.isInStock = true AND p.name LIKE :query AND p.isDeleted = false AND i.isDeleted = false")
	Page<Product> findAllInStockDriverInventoryProductsByDispatcherIdAndDriverIdSearch(Pageable pageable,
			@Param("dispatcherId") int dispatcherId, @Param("driverId") int driverId, @Param("query") String query);

	/**
	 * This query is to populate the list of out of stock driver inventory Products
	 * by dispatcherId and driverId.
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param driverId
	 * @return
	 */
	@Query(value = "SELECT p FROM Product p WHERE p.userDispatcherDetail.id = :dispatcherId AND p.id NOT IN (SELECT p1.id FROM Product p1, DriverInventory i WHERE p1.id = i.product.id AND p1.userDispatcherDetail.id = :dispatcherId AND i.userDriverDetail.id = :driverId AND i.isInStock = true AND p1.isDeleted = false AND i.isDeleted = false) AND p.isDeleted = false")
	Page<Product> findAllOutOfStockDriverInventoryProductsByDispatcherIdAndDriverId(Pageable pageable,
			@Param("dispatcherId") int dispatcherId, @Param("driverId") int driverId);

	/**
	 * This query is to populate the list of out of stock driver inventory Products
	 * by dispatcherId and driverId (With Search).
	 * 
	 * @param pageable
	 * @param dispatcherId
	 * @param driverId
	 * @param query
	 * @return
	 */
	@Query(value = "SELECT p FROM Product p WHERE p.userDispatcherDetail.id = :dispatcherId AND p.id NOT IN (SELECT p1.id FROM Product p1, DriverInventory i WHERE p1.id = i.product.id AND p1.userDispatcherDetail.id = :dispatcherId AND i.userDriverDetail.id = :driverId AND i.isInStock = true AND p1.isDeleted = false AND i.isDeleted = false) AND p.name LIKE :query AND p.isDeleted = false")
	Page<Product> findAllOutOfStockDriverInventoryProductsByDispatcherIdAndDriverIdSearch(Pageable pageable,
			@Param("dispatcherId") int dispatcherId, @Param("driverId") int driverId, @Param("query") String query);

}
