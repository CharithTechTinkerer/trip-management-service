package com.sep.tripmanagementservice.configuration.repository;

import com.sep.tripmanagementservice.configuration.entity.tripcategory.TripCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripCategoryRepo extends JpaRepository<TripCategory, UUID> {
}
