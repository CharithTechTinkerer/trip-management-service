package com.sep.tripmanagementservice.configuration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sep.tripmanagementservice.configuration.entity.tripcategory.TripCategory;

@Repository
public interface TripCategoryRepository extends JpaRepository<TripCategory, Long> {

	TripCategory save(TripCategory tripcategory);
	List<TripCategory> findAllByOrderByCategoryName();
	
}
