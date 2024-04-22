package com.sep.tripmanagementservice.configuration.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sep.tripmanagementservice.configuration.entity.TripCategory;
import com.sep.tripmanagementservice.configuration.enums.TripCategoryStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.repository.TripCategoryRepository;
import com.sep.tripmanagementservice.configuration.service.TripCategoryService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;
import com.sep.tripmanagementservice.configuration.vo.TripCategoryResponseVo;

@Service
public class TripCategoryServiceImpl implements TripCategoryService {

	@Autowired
	private TripCategoryRepository repository;

	@Value("${defaultPageSize}")
	private Integer defaultPageSize;

	private static final Logger LOGGER = LoggerFactory.getLogger(TripCategoryServiceImpl.class);

	@Override
	public TripCategory save(TripCategory tripcategory, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] save: request={}", requestId,
				CommonUtils.convertToString(tripcategory));

		TripCategory response = new TripCategory();
		TripCategory exists = repository.findByCode(tripcategory.getCode());

		if (exists != null) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  save : error={}", requestId,
					TSMSError.TRIP_CATEGORY_CODE_ALREADY_EXIST.getMessage());
			throw new TSMSException(TSMSError.TRIP_CATEGORY_CODE_ALREADY_EXIST);
		}

		try {
			tripcategory.setStatus(TripCategoryStatus.ACTIVE);
			tripcategory.setCreatedDate(LocalDateTime.now());
			response = repository.save(tripcategory);
		} catch (Exception e) {

			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  save : exception={}", requestId, e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.TRIP_CATEGORY_CREATION_FAILED);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] save: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public TripCategory update(Long id, TripCategory updatedTripCategory, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] update: request={}", requestId,
				CommonUtils.convertToString(updatedTripCategory));

		TripCategory existingCategory = repository.findById(id).orElse(null);
		TripCategory response = new TripCategory();

		if (existingCategory != null) {

			if (updatedTripCategory.getName() != null) {
				existingCategory.setName(updatedTripCategory.getName());
			}

			if (updatedTripCategory.getDescription() != null) {
				existingCategory.setDescription(updatedTripCategory.getDescription());
			}

			if (updatedTripCategory.getStatus() != null) {
				existingCategory.setStatus(updatedTripCategory.getStatus());
			}

			existingCategory.setUpdatedBy(updatedTripCategory.getUpdatedBy());
			existingCategory.setUpdatedDate(LocalDateTime.now());

			response = repository.save(existingCategory);

		} else {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  update : error={}", requestId,
					TSMSError.TRIP_CATEGORY_NOT_FOUND.getMessage());
			throw new TSMSException(TSMSError.TRIP_CATEGORY_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] update: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return response;
	}

	@Override
	public TripCategoryResponseVo getAllTripCategories(TripCategoryStatus status, String searchBy, Integer pageNo,
			Integer pageSize, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info(
				"START [SERVICE-LAYER] [RequestId={}] getAllTripCategories: status={}|searchBy={}|pageNo={}|pageSize={}",
				requestId, status, searchBy, pageNo, pageSize);

		Pageable pageable = null;

		if (pageNo != null && pageSize != null) {
			pageable = PageRequest.of(pageNo - 1, pageSize);
		} else if (pageNo != null && pageSize == null) {
			pageable = PageRequest.of(pageNo - 1, defaultPageSize);
		}

		List<TripCategory> tripCategories = new ArrayList<>();
		Long count;
		try {
			if (searchBy != null) {
				tripCategories = repository
						.findByCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndStatus(
								searchBy, status, pageable);
				count = repository
						.countByCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndStatus(
								searchBy, status);
			} else {
				tripCategories = repository.findByStatus(status, pageable);
				count = repository.countByStatus(status);
			}

		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  getAllTripCategories : exception={}", requestId,
					e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.NOT_FOUND);
		}

		TripCategoryResponseVo tripCategoryVo = new TripCategoryResponseVo();

		if (tripCategories.isEmpty()) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  getAllTripCategories : error={}", requestId,
					TSMSError.NOT_FOUND.getMessage());
			throw new TSMSException(TSMSError.NOT_FOUND);
		} else {

			tripCategoryVo.setTripCategories(tripCategories);
			tripCategoryVo.setCount(count);

		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] getAllTripCategories: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(tripCategoryVo));

		return tripCategoryVo;

	}

	@Override
	public TripCategory getTripCategoryById(Long id, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] getTripCategoryById: id={}", requestId, id);

		Optional<TripCategory> tripCategory = repository.findById(id);

		if (!tripCategory.isPresent()) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  getTripCategoryById : error={}", requestId,
					TSMSError.TRIP_CATEGORY_NOT_FOUND.getMessage());
			throw new TSMSException(TSMSError.TRIP_CATEGORY_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] getTripCategoryById: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(tripCategory.get()));

		return tripCategory.get();
	}

	@Override
	public Boolean deleteTripCategory(Long id, String deletedBy, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] deleteTripCategory: id={}|deletedBy={}", requestId, id,
				deletedBy);

		TripCategory exists = repository.findById(id).orElse(null);
		Boolean result = Boolean.FALSE;

		if (exists != null) {
			try {
				exists.setStatus(TripCategoryStatus.DELETED);
				exists.setUpdatedBy(deletedBy);
				exists.setUpdatedDate(LocalDateTime.now());
				repository.save(exists);
				result = Boolean.TRUE;
			} catch (Exception e) {
				LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  deleteTripCategory : exception={}", requestId,
						e.getMessage());
				e.printStackTrace();
				throw new TSMSException(TSMSError.TRIP_CATEGORY_DELETE_FAILED);
			}
		} else {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  deleteTripCategory : error={}", requestId,
					TSMSError.TRIP_CATEGORY_NOT_FOUND.getMessage());
			throw new TSMSException(TSMSError.TRIP_CATEGORY_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] deleteTripCategory: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(result));

		return result;

	}
}
