package com.sep.tripmanagementservice.configuration.service;

import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.entity.tripcategory.TripCategoryRepository;

import java.util.List;

public interface TripCategoryService {
    TripCategoryRepository save(TripCategoryRepository tripcategory, String requestId);

    List<TripCategoryRepository> getAllCategories();

    TripCategoryRepository getCategoryById(Long categoryId);

    TripCategoryRepository deleteCategory(Long categoryId);

	TripCategoryRepository updateCategory(Long existingTripCategory, TripCategoryDto updatedTripCategoryDto, String requestId);
}