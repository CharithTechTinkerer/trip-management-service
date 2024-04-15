package com.sep.tripmanagementservice.configuration.service;

import java.util.List;

import com.sep.tripmanagementservice.configuration.entity.TripCategory;
import com.sep.tripmanagementservice.configuration.enums.TripCategoryStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;

public interface TripCategoryService {

	TripCategory save(TripCategory tripCategory, String requestId) throws TSMSException;

	TripCategory update(Long id, TripCategory updatedTripCategory, String requestId) throws TSMSException;

	List<TripCategory> getAllTripCategories(TripCategoryStatus status, Integer pageNo, Integer pageSize,
			String requestId) throws TSMSException;

	TripCategory getTripCategoryById(Long id, String requestId) throws TSMSException;

	Boolean deleteTripCategory(Long id, String deletedBy, String requestId) throws TSMSException;

}
