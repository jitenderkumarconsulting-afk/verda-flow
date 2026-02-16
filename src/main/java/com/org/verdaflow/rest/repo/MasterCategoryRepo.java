package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.MasterCategory;

@Repository
public interface MasterCategoryRepo extends JpaRepository<MasterCategory, Integer> {

	/**
	 * This query is to fetch all the active categories.
	 * 
	 * 
	 * @return
	 */
	@Query("SELECT c FROM MasterCategory c WHERE c.active = true AND c.isDeleted = false ORDER BY c.name")
	List<MasterCategory> findAllActiveCategories();

	/**
	 * This query is to fetch all the active categories (With Search).
	 * 
	 * 
	 * @param query
	 * @return
	 */
	@Query("SELECT c FROM MasterCategory c WHERE c.name LIKE :query AND c.active = true AND c.isDeleted = false ORDER BY c.name")
	List<MasterCategory> findAllActiveCategoriesSearch(@Param("query") String query);

	/**
	 * This query to fetch category details by category id.
	 * 
	 * @param categoryId
	 * @return
	 */
	@Query("SELECT c FROM MasterCategory c WHERE c.id = :categoryId AND c.isDeleted = false")
	MasterCategory findByCategoryId(@Param("categoryId") int categoryId);

	/**
	 * This query to check the existence of category by name.
	 * 
	 * @param name
	 * @return
	 */
	@Query("SELECT COUNT(c) FROM MasterCategory c WHERE c.name = :name AND c.isDeleted = false")
	int checkCategoryExistenceByName(@Param("name") String name);

	/**
	 * This query to check the existence of category by name except current
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	@Query("SELECT COUNT(c) FROM MasterCategory c WHERE c.name = :name AND c.id != :id AND c.isDeleted = false")
	int checkCategoryExistenceByNameExceptCurrent(@Param("name") String name, @Param("id") int id);

	/**
	 * This query is to fetch all the categories.
	 * 
	 * @param pageable
	 * @return
	 */
	@Query("SELECT c FROM MasterCategory c WHERE c.isDeleted = false ORDER BY c.name")
	Page<MasterCategory> findAllCategories(Pageable pageable);

	/**
	 * This query is to fetch all the categories (With Search).
	 * 
	 * @param pageable
	 * @param query
	 * @return
	 */
	@Query("SELECT c FROM MasterCategory c WHERE c.name LIKE :query AND c.isDeleted = false ORDER BY c.name")
	Page<MasterCategory> findAllCategoriesSearch(Pageable pageable, @Param("query") String query);

}
