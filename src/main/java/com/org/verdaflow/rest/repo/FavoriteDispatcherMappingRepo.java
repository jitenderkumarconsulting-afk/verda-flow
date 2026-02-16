package com.org.verdaflow.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.verdaflow.rest.entity.FavoriteDispatcherMapping;

@Repository
public interface FavoriteDispatcherMappingRepo extends JpaRepository<FavoriteDispatcherMapping, Integer> {

	/**
	 * This query to fetch the details on the basis of userId and dispatcherId.
	 * 
	 * @param userId
	 * @param dispatcherId
	 * @return
	 */
	@Query("SELECT f FROM FavoriteDispatcherMapping f WHERE f.user.id = :userId AND f.userDispatcher.id = :dispatcherId AND f.isDeleted = false")
	FavoriteDispatcherMapping findByUserIdAndDispatcherId(@Param("userId") int userId,
			@Param("dispatcherId") int dispatcherId);

}
