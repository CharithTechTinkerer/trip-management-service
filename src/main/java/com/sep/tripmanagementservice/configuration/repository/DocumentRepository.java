package com.sep.tripmanagementservice.configuration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sep.tripmanagementservice.configuration.entity.Document;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
	
	@Query("SELECT d FROM Document d JOIN d.tripList t WHERE t.id=:tripId AND t.status=:status")
	public List<Document> findByTripIdAndStatus(@Param("tripId") Long tripId , @Param("status") CommonStatus status);
	

}
