package com.sep.tripmanagementservice.configuration.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sep.tripmanagementservice.configuration.entity.TripOptionSelection;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;

@Repository
public interface TripOptionSelectionRepository extends JpaRepository<TripOptionSelection, Long> {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE trip_option_selection SET status=:status WHERE trip_option_id IN (SELECT id FROM trip_option WHERE trip_id = :id)", nativeQuery = true)
	public int updateStatusByTripIdStatus(@Param("status") String status, @Param("id") Long id);
	
}
