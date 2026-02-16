package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.MasterType;

@Repository
public interface MasterTypeRepo extends JpaRepository<MasterType, Integer> {

	/**
	 * This query is to fetch all the active types.
	 * 
	 * @return
	 */
	@Query("SELECT t FROM MasterType t WHERE t.active = true AND t.isDeleted = false ORDER BY t.name")
	List<MasterType> findAllActiveTypes();

	/**
	 * This query is to fetch all the active types (With Search).
	 * 
	 * @param query
	 * @return
	 */
	@Query("SELECT t FROM MasterType t WHERE t.name LIKE :query AND t.active = true AND t.isDeleted = false ORDER BY t.name")
	List<MasterType> findAllActiveTypesSearch(@Param("query") String query);

	/**
	 * This query to check the existence of type by name.
	 * 
	 * @param name
	 * @return
	 */
	@Query("SELECT COUNT(t) FROM MasterType t WHERE t.name = :name AND t.isDeleted = false")
	int checkTypeExistenceByName(@Param("name") String name);

	/**
	 * This query to fetch type details by type id.
	 * 
	 * @param typeId
	 * @return
	 */
	@Query("SELECT t FROM MasterType t WHERE t.id = :typeId AND t.isDeleted = false")
	MasterType findByTypeId(@Param("typeId") int typeId);

	/**
	 * This query to check the existence of type by name except current
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	@Query("SELECT COUNT(t) FROM MasterType t WHERE t.name = :name AND t.id != :id AND t.isDeleted = false")
	int checkTypeExistenceByNameExceptCurrent(@Param("name") String name, @Param("id") int id);

	/**
	 * This query is to fetch all the types.
	 * 
	 * @param pageable
	 * @return
	 */
	@Query("SELECT t FROM MasterType t WHERE t.isDeleted = false ORDER BY t.name")
	Page<MasterType> findAllTypes(Pageable pageable);

	/**
	 * This query is to fetch all the types (With Search).
	 * 
	 * @param pageable
	 * @param query
	 * @return
	 */
	@Query("SELECT t FROM MasterType t WHERE t.name LIKE :query AND t.isDeleted = false ORDER BY t.name")
	Page<MasterType> findAllTypesSearch(Pageable pageable, @Param("query") String query);

}
