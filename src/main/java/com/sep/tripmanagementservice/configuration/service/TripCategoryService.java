package com.sep.tripmanagementservice.configuration.service;

import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.entity.tripcategory.TripCategoryRepository;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripCategoryService {
    TripCategoryRepository save(TripCategoryRepository tripcategory, String requestId);

    List<TripCategoryRepository> getAllCategories();

    TripCategoryRepository getCategoryById(Long categoryId);

    TripCategoryRepository deleteCategory(Long categoryId);

	TripCategoryRepository updateCategory(Long existingTripCategory, TripCategoryDto updatedTripCategoryDto, String requestId);
}