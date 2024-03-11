package com.sep.tripmanagementservice.configuration.service;

import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.entity.tripcategory.TripCategory;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripCategoryService {
    TripCategory save(TripCategory tripcategory, String requestId);

    List<TripCategory> getAllCategories();

    Optional<TripCategory> getCategoryById(UUID categoryId);

    TripCategory updateCategory(Optional<TripCategory> existingTripCategory, @RequestBody TripCategoryDto updatedTripCategoryDto, String requestId);

    TripCategory deleteCategory(UUID categoryId);
}
