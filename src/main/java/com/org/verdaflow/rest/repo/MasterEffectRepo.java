package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.MasterEffect;

@Repository
public interface MasterEffectRepo extends JpaRepository<MasterEffect, Integer> {

	/**
	 * This query is to fetch all the active effects.
	 * 
	 * @return
	 */
	@Query("SELECT e FROM MasterEffect e WHERE e.active = true AND e.isDeleted = false ORDER BY e.name")
	List<MasterEffect> findAllActiveEffects();

	/**
	 * This query is to fetch all the active effects (With Search).
	 * 
	 * @param query
	 * @return
	 */
	@Query("SELECT e FROM MasterEffect e WHERE e.name LIKE :query AND e.active = true AND e.isDeleted = false ORDER BY e.name")
	List<MasterEffect> findAllActiveEffectsSearch(@Param("query") String query);

	/**
	 * This query to check the existence of effect by name.
	 * 
	 * @param name
	 * @return
	 */
	@Query("SELECT COUNT(e) FROM MasterEffect e WHERE e.name = :name AND e.isDeleted = false")
	int checkEffectExistenceByName(@Param("name") String name);

	/**
	 * This query to fetch effect details by effect id.
	 * 
	 * @param effectId
	 * @return
	 */
	@Query("SELECT e FROM MasterEffect e WHERE e.id = :effectId AND e.isDeleted = false")
	MasterEffect findByEffectId(@Param("effectId") int effectId);

	/**
	 * This query to check the existence of effect by name except current
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	@Query("SELECT COUNT(e) FROM MasterEffect e WHERE e.name = :name AND e.id != :id AND e.isDeleted = false")
	int checkEffectExistenceByNameExceptCurrent(@Param("name") String name, @Param("id") int id);

	/**
	 * This query is to fetch all the effects.
	 * 
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM MasterEffect e WHERE e.isDeleted = false ORDER BY e.name")
	Page<MasterEffect> findAllEffects(Pageable pageable);

	/**
	 * This query is to fetch all the effects (With Search).
	 * 
	 * @param pageable
	 * @param query
	 * @return
	 */
	@Query("SELECT e FROM MasterEffect e WHERE e.name LIKE :query AND e.isDeleted = false ORDER BY e.name")
	Page<MasterEffect> findAllEffectsSearch(Pageable pageable, @Param("query") String query);

}
