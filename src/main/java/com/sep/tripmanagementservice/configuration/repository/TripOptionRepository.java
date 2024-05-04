package com.sep.tripmanagementservice.configuration.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sep.tripmanagementservice.configuration.entity.Trip;
import com.sep.tripmanagementservice.configuration.entity.TripOption;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;

@Repository
public interface TripOptionRepository extends JpaRepository<TripOption, Long> {
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE TripOption o SET o.status=:status WHERE o.trip.id=:id")
	public int updateStatusByTripIdStatus(@Param("status") CommonStatus status, @Param("id") Long id);
	
	@Query("SELECT o FROM TripOption o WHERE o.trip.id=:id")
	public List<TripOption> getTripOptionsByTripId(@Param("id") Long id);
	
	
	
}
