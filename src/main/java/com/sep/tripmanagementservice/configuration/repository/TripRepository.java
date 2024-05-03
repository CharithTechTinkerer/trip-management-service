package com.sep.tripmanagementservice.configuration.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sep.tripmanagementservice.configuration.entity.Trip;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

	@Query("SELECT t FROM Trip t WHERE t.added_by=:userId AND t.status=:status")
	public List<Trip> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") CommonStatus status);
	
	@Query("SELECT COUNT(t) FROM Trip t WHERE t.id=:id AND added_by=:user")
	public long findCountByIdForUser(@Param("id") Long id, @Param("user") Long user);
	
	@Query("SELECT t FROM Trip t WHERE t.id=:id AND added_by=:user")
	public Trip getTripByIdForUser(@Param("id") Long id, @Param("user") Long user);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Trip t SET t.status=:status WHERE t.id=:id")
	public int updateStatusByIdStatus(@Param("status") CommonStatus status, @Param("id") Long id);
	
}
