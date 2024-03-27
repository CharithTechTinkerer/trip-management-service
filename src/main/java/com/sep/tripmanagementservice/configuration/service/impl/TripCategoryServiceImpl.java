package com.sep.tripmanagementservice.configuration.service.impl;


import com.sep.tripmanagementservice.configuration.dto.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.entity.TripCategory;
import com.sep.tripmanagementservice.configuration.repository.TripCategoryRepository;
import com.sep.tripmanagementservice.configuration.service.TripCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TripCategoryServiceImpl implements TripCategoryService{

    @Autowired
    private TripCategoryRepository repository;

    @Override
    public TripCategory save(TripCategory tripcategory, String requestId) {
        TripCategory tripCategoryResponse = new TripCategory();
        try {
            tripCategoryResponse = repository.save(tripcategory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripCategoryResponse;
    }

    @Override
    public List<TripCategory> getAllCategories(){
        List<TripCategory> categoryList = repository.findAll();
        
        return categoryList;

    }


    @Override
	public Optional<TripCategory> getCategoryById(Long categoryId) {
    	Optional<TripCategory> category = repository.findById(categoryId);
    	
		return category;
	}
    
    @Override
    public TripCategory updateCategory(Long categoryId, TripCategoryDto updatedTripCategoryDto, String requestId) {
        // Get the existing category by ID
		TripCategory existingCategory = repository.findById(categoryId).orElse(null);

        // Check if the category exists
        if (existingCategory != null) {
            // Update the existing category with new values
            existingCategory.setCategoryName(updatedTripCategoryDto.getCategoryName());
            // You can update other fields similarly

            // Save the updated category
            return repository.save(existingCategory);
        } else {
            // If the category with the given ID doesn't exist, throw an exception
            throw new RuntimeException("TripCategory not found with ID: " + categoryId);
        }
    }



    @Override
    public TripCategory deleteCategory(Long categoryId) {
        // Get the existing category by ID
        TripCategory tripCategoryToDelete = repository.findById(categoryId)
                .orElse(null);

        // Check if the category exists
        if (tripCategoryToDelete != null) {
            repository.delete(tripCategoryToDelete);
            return tripCategoryToDelete;
        } else {
            // If the category with the given ID doesn't exist, throw an exception
            throw new RuntimeException("TripCategory Not Found");
        }
    }
}
