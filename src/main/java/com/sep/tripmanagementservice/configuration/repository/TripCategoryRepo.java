package com.sep.tripmanagementservice.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sep.tripmanagementservice.configuration.entity.TripCategory;

import java.util.UUID;

public interface TripCategoryRepo extends JpaRepository<TripCategory, UUID> {
}
