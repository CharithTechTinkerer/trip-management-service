package com.sep.tripmanagementservice.configuration.service.impl;


import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.entity.tripcategory.TripCategoryRepository;
import com.sep.tripmanagementservice.configuration.service.TripCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sep.tripmanagementservice.configuration.repository.TripCategoryRepo;

import java.util.List;

@Service
public class TripCategoryServiceImpl implements TripCategoryService{

    @Autowired
    private TripCategoryRepo repository;

    public TripCategoryServiceImpl(TripCategoryRepo repository) {
        this.repository = repository;
    }

    @Override
    public TripCategoryRepository save(TripCategoryRepository tripcategory, String requestId) {
        TripCategoryRepository tripCategoryResponse = new TripCategoryRepository();
        try {
            tripCategoryResponse = repository.save(tripcategory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripCategoryResponse;
    }

    @Override
    public List<TripCategoryRepository> getAllCategories(){
        return repository.findAll();
    }

    @Override
	public TripCategoryRepository getCategoryById(Long categoryId) {
		return repository.findById(categoryId).orElse(null);
	}
    @Override
    public TripCategoryRepository updateCategory(Long categoryId, TripCategoryDto updatedTripCategoryDto, String requestId) {
        // Get the existing category by ID
		TripCategoryRepository existingCategory = repository.findById(categoryId).orElse(null);

        // Check if the category exists
        if (existingCategory != null) {
            // Update the existing category with new values
            existingCategory.setCategory_name(updatedTripCategoryDto.getCategory_name());
            // You can update other fields similarly

            // Save the updated category
            return repository.save(existingCategory);
        } else {
            // If the category with the given ID doesn't exist, throw an exception
            throw new RuntimeException("TripCategory not found with ID: " + categoryId);
        }
    }



    @Override
    public TripCategoryRepository deleteCategory(Long categoryId) {
        // Get the existing category by ID
        TripCategoryRepository tripCategoryToDelete = repository.findById(categoryId)
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
