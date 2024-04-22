package com.sep.tripmanagementservice.configuration.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sep.tripmanagementservice.configuration.entity.TripCategory;
import com.sep.tripmanagementservice.configuration.enums.TripCategoryStatus;

@Repository
public interface TripCategoryRepository extends JpaRepository<TripCategory, Long> {

	TripCategory findByCode(String code);

	List<TripCategory> findByStatus(TripCategoryStatus status, Pageable pageable);

	List<TripCategory> findAllByOrderByName();

	Long countByStatus(TripCategoryStatus status);
	
	@Query("SELECT t FROM TripCategory t " +
	           "WHERE (LOWER(t.code) LIKE LOWER(CONCAT('%', :searchWord, '%')) " +
	           "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :searchWord, '%')) " +
	           "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchWord, '%'))) " +
	           "AND t.status = :status")
	    List<TripCategory> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndStatus(
	    		@Param("searchWord") String title, @Param("status") TripCategoryStatus status, Pageable pageable);
	
	@Query("SELECT count(t) FROM TripCategory t " +
	           "WHERE (LOWER(t.code) LIKE LOWER(CONCAT('%', :searchWord, '%')) " +
	           "OR LOWER(t.name) LIKE LOWER(CONCAT('%', :searchWord, '%')) " +
	           "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchWord, '%'))) " +
	           "AND t.status = :status")
	    Long countByCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndStatus(
	    		@Param("searchWord") String title, @Param("status") TripCategoryStatus status);

}
