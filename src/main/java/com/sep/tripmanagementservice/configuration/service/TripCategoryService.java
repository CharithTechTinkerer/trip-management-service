package com.sep.tripmanagementservice.configuration.service;

import com.sep.tripmanagementservice.configuration.entity.TripCategory;
import com.sep.tripmanagementservice.configuration.enums.TripCategoryStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.vo.TripCategoryResponseVo;

public interface TripCategoryService {

	public TripCategory save(TripCategory tripCategory, String requestId) throws TSMSException;

	public TripCategory update(Long id, TripCategory updatedTripCategory, String requestId) throws TSMSException;

	public TripCategoryResponseVo getAllTripCategories(TripCategoryStatus status, String searchBy, Integer pageNo,
			Integer pageSize, String requestId) throws TSMSException;

	public TripCategory getTripCategoryById(Long id, String requestId) throws TSMSException;

	public Boolean deleteTripCategory(Long id, String deletedBy, String requestId) throws TSMSException;

}
