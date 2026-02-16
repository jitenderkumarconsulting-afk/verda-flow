package com.org.verdaflow.rest.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.MasterEta;

@Repository
public interface MasterEtaRepo extends JpaRepository<MasterEta, Integer> {

	/**
	 * This query to check the existence of ETA by name.
	 * 
	 * @param name
	 * @return
	 */
	@Query("SELECT COUNT(e) FROM MasterEta e WHERE e.name = :name AND e.isDeleted = false")
	int checkEtaExistenceByName(@Param("name") String name);

	/**
	 * This query to fetch ETA details by etaId.
	 * 
	 * @param etaId
	 * @return
	 */
	@Query("SELECT e FROM MasterEta e WHERE e.id = :etaId AND e.isDeleted = false")
	MasterEta findByEtaId(@Param("etaId") int etaId);

	/**
	 * This query to check the existence of ETA by name except current.
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	@Query("SELECT COUNT(e) FROM MasterEta e WHERE e.name = :name AND e.id != :id AND e.isDeleted = false")
	int checkEtaExistenceByNameExceptCurrent(@Param("name") String name, @Param("id") int id);

	/**
	 * This query is to fetch all the ETAs.
	 * 
	 * @param pageable
	 * @return
	 */
	@Query("SELECT e FROM MasterEta e WHERE e.isDeleted = false ORDER BY e.id")
	Page<MasterEta> findAllEtas(Pageable pageable);

	/**
	 * This query is to fetch all the ETAs (With Search).
	 * 
	 * @param pageable
	 * @param query
	 * @return
	 */
	@Query("SELECT e FROM MasterEta e WHERE e.name LIKE :query AND e.isDeleted = false ORDER BY e.id")
	Page<MasterEta> findAllEtasSearch(Pageable pageable, @Param("query") String query);

	/**
	 * This query is to fetch all the active ETAs.
	 * 
	 * @return
	 */
	@Query("SELECT e FROM MasterEta e WHERE e.active = true AND e.isDeleted = false ORDER BY e.id")
	List<MasterEta> findAllActiveEtas();

	/**
	 * This query is to fetch all the active ETAs (With Search).
	 * 
	 * @param query
	 * @return
	 */
	@Query("SELECT e FROM MasterEta e WHERE e.name LIKE :query AND e.active = true AND e.isDeleted = false ORDER BY e.id")
	List<MasterEta> findAllActiveEtasSearch(@Param("query") String query);
}