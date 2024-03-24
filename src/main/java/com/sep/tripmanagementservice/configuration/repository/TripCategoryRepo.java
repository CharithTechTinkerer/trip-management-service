package com.sep.tripmanagementservice.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sep.tripmanagementservice.configuration.entity.tripcategory.TripCategoryRepository;

@Repository
public interface TripCategoryRepo extends JpaRepository<TripCategoryRepository, Long> {
}
