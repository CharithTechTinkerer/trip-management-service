package com.sep.tripmanagementservice.configuration.service;

import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.entity.tripcategory.TripCategory;

import java.util.List;
import java.util.Optional;

public interface TripCategoryService {
    TripCategory save(TripCategory tripcategory, String requestId);

    List<TripCategory> getAllCategories();

    Optional<TripCategory> getCategoryById(Long categoryId);

    TripCategory deleteCategory(Long categoryId);

	TripCategory updateCategory(Long existingTripCategory, TripCategoryDto updatedTripCategoryDto, 
			String requestId);
}
