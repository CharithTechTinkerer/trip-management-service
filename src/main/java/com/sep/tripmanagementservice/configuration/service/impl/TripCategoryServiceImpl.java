package com.sep.tripmanagementservice.configuration.service.impl;

import com.sep.tripmanagementservice.configuration.controller.entity.TripCategory;
import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.service.TripCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sep.tripmanagementservice.configuration.repository.TripCategoryRepo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripCategoryServiceImpl implements TripCategoryService{

    @Autowired
    private TripCategoryRepo repository;

    public TripCategoryServiceImpl(TripCategoryRepo repository) {
        this.repository = repository;
    }

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
        return repository.findAll();
    }

    @Override
    public Optional<TripCategory> getCategoryById(UUID categoryId) {
        return repository.findById(categoryId);
    }

    @Override
    public TripCategory updateCategory(Optional<TripCategory> tripcategory, @RequestBody TripCategoryDto updatedTripCategoryDto, String requestId) {

        try {
            TripCategory updatingCategory = tripcategory.orElseThrow(
                    () -> new RuntimeException("TripCategory not found"));
            updatingCategory.setCategory_name(updatedTripCategoryDto.getCategory_name());

            return repository.save(updatingCategory);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public TripCategory deleteCategory(UUID categoryId) {

        Optional<TripCategory>deleteingTripCategory = repository.findById(categoryId);
        try {
            if(deleteingTripCategory.isPresent()) {
                TripCategory tripCategoryToDelete = deleteingTripCategory.get();

                repository.delete(tripCategoryToDelete);

                return tripCategoryToDelete;
            } else {
                throw new RuntimeException("TripCategory Not Found");
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format");
        }
    }


}
