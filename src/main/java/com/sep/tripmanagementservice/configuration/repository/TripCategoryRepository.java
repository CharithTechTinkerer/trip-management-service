package com.sep.tripmanagementservice.configuration.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sep.tripmanagementservice.configuration.entity.TripCategory;
import com.sep.tripmanagementservice.configuration.enums.TripCategoryStatus;

@Repository
public interface TripCategoryRepository extends JpaRepository<TripCategory, Long> {

	TripCategory findByCode(String code);

	List<TripCategory> findByStatus(TripCategoryStatus status, Pageable pageable);

	List<TripCategory> findAllByOrderByName();

}
