package com.org.verdaflow.rest.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.api.dispatcher.model.ProductCountGraphModel;
import com.org.verdaflow.rest.api.dispatcher.model.ProductCountGraphModelForWeekOrMonthOrYear;
import com.org.verdaflow.rest.common.enums.ProductAggregateType;
import com.org.verdaflow.rest.entity.ProductAggregateDetail;

@Repository
public interface ProductAggregateDetailRepo extends CrudRepository<ProductAggregateDetail, Integer> {

	/**
	 * This query to check the existence of Product Aggregate Detail by Product
	 * AggregateId and AggregateType.
	 * 
	 * @param productId
	 * @param userId
	 * @param aggregateType
	 * @return
	 */
	@Query("SELECT a FROM ProductAggregateDetail a WHERE a.productAggregate.product.id = :productId AND a.user.id = :userId AND a.productAggregate.productAggregateType = :aggregateType AND a.isDeleted = false")
	ProductAggregateDetail findByProductIdAndUserIdAndProductAggregateType(@Param("productId") int productId,
			@Param("userId") int userId, @Param("aggregateType") ProductAggregateType aggregateType);

	// /**
	// * This query to check the existence of Product Aggregate by dispatcherId and
	// * startDate and endDate.
	// *
	// * @param dispatcherId
	// * @param startDate
	// * @param endDate
	// * @return
	// */
	// @Query(value = "SELECT new
	// com.org.verdaflow.rest.api.dispatcher.model.SoldProductCount(COUNT(pa),
	// DATE(pa.createdAt)) FROM ProductAggregateDetail pa,ProductAggregate a,
	// Product p, UserDispatcherDetail u WHERE DATE(pa.createdAt) BETWEEN
	// DATE(:startDate) AND DATE(:endDate) AND p.userDispatcherDetail.id = u.id AND
	// p.id = a.product.id AND a.id = pa.productAggregate.id AND
	// a.productAggregateType = 'SOLD' AND u.id = :dispatcherId AND pa.isDeleted =
	// false AND a.isDeleted = false AND p.isDeleted = false AND u.isDeleted = false
	// GROUP BY DATE(pa.createdAt)")
	// List<SoldProductCount>
	// getProductsCountByDispatcherIdAndDate(@Param("startDate") Date startDate,
	// @Param("endDate") Date endDate, @Param("dispatcherId") int dispatcherId);
	//
	// /**
	// * This query to check the existence of Product Aggregate by dispatcherId and
	// * startDate and endDate.
	// *
	// * @param dispatcherId
	// * @param startDate
	// * @param endDate
	// *
	// * @return
	// */
	// @Query(value = "SELECT new
	// com.org.verdaflow.rest.api.dispatcher.model.SoldProductCount(COUNT(pa),
	// DATE(pa.createdAt)) FROM ProductAggregateDetail pa,ProductAggregate a,
	// Product p, UserDispatcherDetail u WHERE MONTH(pa.createdAt) = MONTH(
	// :startDate ) AND p.userDispatcherDetail.id = u.id AND p.id = a.product.id AND
	// a.id = pa.productAggregate.id AND a.productAggregateType = 'SOLD' AND u.id =
	// :dispatcherId AND pa.isDeleted = false AND a.isDeleted = false AND
	// p.isDeleted = false AND u.isDeleted = false GROUP BY DATE(pa.createdAt)")
	// List<SoldProductCount>
	// getProductsCountByDispatcherIdAndMonth(@Param("startDate") Date startDate,
	// @Param("dispatcherId") int dispatcherId);

	// /**
	// * This query to check the existence of Product Aggregate by dispatcherId and
	// * startDate and endDate.
	// *
	// * @param dispatcherId
	// * @param startDate
	// * @param endDate
	// * @return
	// */
	// @Query("SELECT new
	// com.org.verdaflow.rest.api.dispatcher.model.SoldProductCount(COUNT(pa),
	// MONTH(pa.createdAt)) FROM ProductAggregateDetail pa,ProductAggregate a,
	// Product p, UserDispatcherDetail u WHERE YEAR(pa.createdAt) = YEAR(:startDate)
	// AND YEAR(pa.createdAt) = YEAR(:endDate) AND p.userDispatcherDetail.id = u.id
	// AND p.id = a.product.id AND a.id = pa.productAggregate.id AND
	// a.productAggregateType = 'SOLD' AND u.id = :dispatcherId AND pa.isDeleted =
	// false AND a.isDeleted = false AND p.isDeleted = false AND u.isDeleted = false
	// GROUP BY MONTH(pa.createdAt)")
	// List<SoldProductCount>
	// getProductsCountByDispatcherIdAndYear(@Param("startDate") Date startDate,
	// @Param("endDate") Date endDate, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query to check the existence of Product Aggregate by dispatcherId
	 * between startDate and endDate.
	 * 
	 * @param dispatcherId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query(value = "SELECT new com.org.verdaflow.rest.api.dispatcher.model.ProductCountGraphModel(COUNT(pa), DATE(pa.createdAt)) FROM ProductAggregateDetail pa, ProductAggregate a, Product p, UserDispatcherDetail u WHERE DATE(pa.createdAt) BETWEEN DATE(:startDate) AND DATE(:endDate) AND p.userDispatcherDetail.id = u.id AND p.id = a.product.id AND a.id = pa.productAggregate.id AND a.productAggregateType = 'SOLD' AND u.id = :dispatcherId AND pa.isDeleted = false AND a.isDeleted = false AND u.isDeleted = false GROUP BY DATE(pa.createdAt)")
	List<ProductCountGraphModel> getProductsCountByDispatcherIdDateWise(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate, @Param("dispatcherId") int dispatcherId);

	/**
	 * This query to check the existence of Product Aggregate by dispatcherId
	 * between startDate and endDate.
	 * 
	 * @param dispatcherId
	 * @param startDate
	 * @param endDate
	 * 
	 * @return
	 */
	@Query(value = "SELECT new com.org.verdaflow.rest.api.dispatcher.model.ProductCountGraphModelForWeekOrMonthOrYear(COUNT(pa), WEEK(pa.createdAt)) FROM ProductAggregateDetail pa, ProductAggregate a, Product p, UserDispatcherDetail u WHERE WEEK(pa.createdAt) BETWEEN WEEK(:startDate) AND  WEEK(:endDate) AND p.userDispatcherDetail.id = u.id AND p.id = a.product.id AND a.id = pa.productAggregate.id AND a.productAggregateType = 'SOLD' AND u.id = :dispatcherId AND pa.isDeleted = false AND a.isDeleted = false AND u.isDeleted = false GROUP BY WEEK(pa.createdAt)")
	List<ProductCountGraphModelForWeekOrMonthOrYear> getProductsCountByDispatcherIdWeekly(
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("dispatcherId") int dispatcherId);

	/**
	 * This query to check the existence of Product Aggregate by dispatcherId and
	 * startDate and endDate.
	 * 
	 * @param dispatcherId
	 * @param startDate
	 * @param endDate
	 * 
	 * @return
	 */
	@Query(value = "SELECT new com.org.verdaflow.rest.api.dispatcher.model.ProductCountGraphModelForWeekOrMonthOrYear(COUNT(pa), MONTH(pa.createdAt)) FROM ProductAggregateDetail pa, ProductAggregate a, Product p, UserDispatcherDetail u WHERE MONTH(pa.createdAt) BETWEEN MONTH(:startDate) AND MONTH(:endDate) AND p.userDispatcherDetail.id = u.id AND p.id = a.product.id AND a.id = pa.productAggregate.id AND a.productAggregateType = 'SOLD' AND u.id = :dispatcherId AND pa.isDeleted = false AND a.isDeleted = false AND u.isDeleted = false GROUP BY MONTH(pa.createdAt)")
	List<ProductCountGraphModelForWeekOrMonthOrYear> getProductsCountByDispatcherIdMonthly(
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("dispatcherId") int dispatcherId);

	/**
	 * This query to check the existence of Product Aggregate by dispatcherId and
	 * startDate and endDate.
	 * 
	 * @param dispatcherId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query("SELECT new com.org.verdaflow.rest.api.dispatcher.model.ProductCountGraphModelForWeekOrMonthOrYear(COUNT(pa), YEAR(pa.createdAt)) FROM ProductAggregateDetail pa, ProductAggregate a, Product p, UserDispatcherDetail u WHERE YEAR(pa.createdAt) BETWEEN YEAR(:startDate) AND YEAR(:endDate) AND p.userDispatcherDetail.id = u.id AND p.id = a.product.id AND a.id = pa.productAggregate.id AND a.productAggregateType = 'SOLD' AND u.id = :dispatcherId AND pa.isDeleted = false AND a.isDeleted = false AND u.isDeleted = false GROUP BY YEAR(pa.createdAt)")
	List<ProductCountGraphModelForWeekOrMonthOrYear> getProductsCountByDispatcherIdYearly(
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("dispatcherId") int dispatcherId);

}
