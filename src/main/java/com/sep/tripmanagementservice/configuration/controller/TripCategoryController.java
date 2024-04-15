package com.sep.tripmanagementservice.configuration.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sep.tripmanagementservice.configuration.dto.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.dto.response.TSMSResponse;
import com.sep.tripmanagementservice.configuration.entity.TripCategory;
import com.sep.tripmanagementservice.configuration.enums.TripCategoryStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.service.TripCategoryService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;

@RestController
@RequestMapping("/api/v1/private/trip-categories")
public class TripCategoryController {

	@Autowired
	private TripCategoryService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(TripCategoryController.class);

	@PostMapping("/save")
	public ResponseEntity<TSMSResponse> save(@RequestParam("requestId") String requestId,
			@RequestBody TripCategoryDto tripcategoryDto) throws TSMSException {

		TSMSResponse response = new TSMSResponse();

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] save: request={}", requestId,
				CommonUtils.convertToString(tripcategoryDto));

		if (!CommonUtils.checkMandtoryFieldsNullOrEmptyTripCategory(tripcategoryDto)) {
			LOGGER.error(
					"ERROR [REST-LAYER] [RequestId={}] save : Mandatory fields are null. Please ensure all required fields are provided",
					requestId);
			throw new TSMSException(TSMSError.MANDOTORY_FIELDS_EMPTY);
		}

		TripCategoryDto dto = convertEntityToDto(service.save(convertDtoToEntity(tripcategoryDto), requestId));
		response.setRequestId(requestId);
		response.setSuccess(true);
		response.setData(dto);
		response.setMessage("Trip Category Saved Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] save: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return ResponseEntity.ok(response);

	}

	@GetMapping("/get-all")
	public ResponseEntity<TSMSResponse> getAllTripCategories(
			@RequestParam(name = "requestId", required = true) String requestId,
			@RequestParam(name = "status", required = false) String status,
			@RequestParam(name = "pageNo", required = true) Integer pageNo,
			@RequestParam(name = "pageSize", required = false) Integer pageSize) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] getAllTripCategories: request={}", requestId,
				CommonUtils.convertToString(requestId));

		TSMSResponse response = new TSMSResponse();

		TripCategoryStatus tripCategoryStatus = TripCategoryStatus.ACTIVE;

		if (status != null) {

			if (!CommonUtils.isValidStatus(status)) {
				LOGGER.error("ERROR [REST-LAYER] [RequestId={}] getAllTripCategories : Invalid Trip Category Status",
						requestId);
				throw new TSMSException(TSMSError.INVALID_TRIP_CATEGORY_STATUS);
			} else {
				tripCategoryStatus = TripCategoryStatus.valueOf(status);
			}
		}

		List<TripCategory> tripCategories = service.getAllTripCategories(tripCategoryStatus, pageNo, pageSize,
				requestId);

		List<TripCategoryDto> tripCategoryDto = tripCategories.stream().map(this::convertEntityToDto)
				.collect(Collectors.toList());

		response.setRequestId(requestId);
		response.setSuccess(true);
		response.setData(tripCategoryDto);
		response.setMessage("Trip Categories Retreived Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] getAllTripCategories: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return ResponseEntity.ok(response);

	}

	@GetMapping("/get-by-id/{id}")
	public ResponseEntity<TSMSResponse> getTripCategoryById(
			@RequestParam(name = "requestId", required = true) String requestId,
			@PathVariable(name = "id", required = true) Long id) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] getTripCategoryById: request={}", requestId,
				CommonUtils.convertToString(requestId));

		TSMSResponse response = new TSMSResponse();

		TripCategoryDto tripCategoryDto = convertEntityToDto(service.getTripCategoryById(id, requestId));

		response.setRequestId(requestId);
		response.setSuccess(true);
		response.setData(tripCategoryDto);
		response.setMessage("Trip Category Retreived Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] getTripCategoryById: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return ResponseEntity.ok(response);

	}

	@PutMapping("/update/{id}")
	public ResponseEntity<TSMSResponse> update(@PathVariable(name = "id", required = true) Long id,
			@RequestBody TripCategoryDto tripCategoryDto,
			@RequestParam(name = "requestId", required = true) String requestId) throws TSMSException {

		TSMSResponse response = new TSMSResponse();

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] update: request={}", requestId,
				CommonUtils.convertToString(tripCategoryDto));

		if (tripCategoryDto.getUpdatedBy().equals("") || tripCategoryDto.getUpdatedBy().isEmpty()
				|| tripCategoryDto.getUpdatedBy() == null) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] update : Trip Category Updating User is Mandatory",
					requestId);
			throw new TSMSException(TSMSError.UPDATE_BY_FIELD_EMPTY);

		}

		if (tripCategoryDto.getStatus() != null && !CommonUtils.isValidStatus(tripCategoryDto.getStatus())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] register : Invalid Role", requestId);
			throw new TSMSException(TSMSError.INVALID_TRIP_CATEGORY_STATUS);
		}

		TripCategoryDto dto = convertEntityToDto(service.update(id, convertDtoToEntity(tripCategoryDto), requestId));

		response.setRequestId(requestId);
		response.setSuccess(true);
		response.setData(dto);
		response.setMessage("Trip Category Updated Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] update: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<TSMSResponse> deleteTripCategory(@PathVariable(name = "id", required = true) Long id,
			@RequestParam(name = "deletedBy", required = true) String deletedBy,
			@RequestParam(name = "requestId", required = true) String requestId) throws TSMSException {

		TSMSResponse response = new TSMSResponse();

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] deleteTripCategory: request={}", requestId,
				CommonUtils.convertToString(id));

		Boolean result = service.deleteTripCategory(id, deletedBy, requestId);

		if (result) {
			response.setMessage("Trip Category Deleted Successfully");
			response.setSuccess(true);
			response.setStatus(TSMSError.OK.getStatus());
		} else {
			response.setMessage("Trip Category Deletion Failed");
			response.setSuccess(true);
			response.setStatus(TSMSError.FAILED.getStatus());
		}

		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] deleteTripCategory: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return ResponseEntity.ok(response);

	}

	private TripCategoryDto convertEntityToDto(TripCategory tripCategory) {

		TripCategoryDto tripCategoryDto = new TripCategoryDto();

		tripCategoryDto.setId(tripCategory.getId());
		tripCategoryDto.setName(tripCategory.getName());
		tripCategoryDto.setDescription(tripCategory.getDescription());
		tripCategoryDto.setCode(tripCategory.getCode());
		tripCategoryDto.setCreatedBy(tripCategory.getCreatedBy());
		tripCategoryDto.setCreatedDate(tripCategory.getCreatedDate());
		tripCategoryDto.setUpdatedBy(tripCategory.getUpdatedBy());
		tripCategoryDto.setUpdatedDate(tripCategory.getUpdatedDate());

		if (tripCategory.getStatus() != null) {
			tripCategoryDto.setStatus(tripCategory.getStatus().name());
		}

		return tripCategoryDto;
	}

	private TripCategory convertDtoToEntity(TripCategoryDto tripCategoryDto) {
		TripCategory tripcategory = new TripCategory();

		if (tripCategoryDto.getStatus() != null) {
			String status = tripCategoryDto.getStatus();

			if (status.equals(TripCategoryStatus.ACTIVE.name())) {
				tripcategory.setStatus(TripCategoryStatus.ACTIVE);
			} else if (status.equals(TripCategoryStatus.INACTIVE.name())) {
				tripcategory.setStatus(TripCategoryStatus.INACTIVE);
			} else if (status.equals(TripCategoryStatus.DELETED.name())) {
				tripcategory.setStatus(TripCategoryStatus.DELETED);
			}
		}

		tripcategory.setId(tripCategoryDto.getId());
		tripcategory.setName(tripCategoryDto.getName());
		tripcategory.setDescription(tripCategoryDto.getDescription());
		tripcategory.setCode(tripCategoryDto.getCode());
		tripcategory.setCreatedBy(tripCategoryDto.getCreatedBy());
		tripcategory.setCreatedDate(tripCategoryDto.getCreatedDate());
		tripcategory.setUpdatedBy(tripCategoryDto.getUpdatedBy());
		tripcategory.setUpdatedDate(tripCategoryDto.getUpdatedDate());

		return tripcategory;
	}
}
