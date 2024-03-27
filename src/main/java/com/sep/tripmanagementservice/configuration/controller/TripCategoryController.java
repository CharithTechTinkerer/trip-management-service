package com.sep.tripmanagementservice.configuration.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sep.tripmanagementservice.configuration.dto.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.dto.response.TSMSResponse;
import com.sep.tripmanagementservice.configuration.entity.TripCategory;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.service.TripCategoryService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/v1/private/tripcategories")
public class TripCategoryController {

    @Autowired
    private TripCategoryService service;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TripCategoryController.class);

    @PostMapping("/save")
    public ResponseEntity<TSMSResponse> saveTripCategory(@RequestParam("requestId") String requestId, @RequestBody TripCategoryDto tripcategoryDto) throws TSMSException{
		
		TSMSResponse response = new TSMSResponse();
		try {
			
			long startTime = System.currentTimeMillis();
			LOGGER.info("START [REST-LAYER] [RequestId={}] saveTripCategory: request={}", requestId,
					CommonUtils.convertToString(tripcategoryDto));
			
			if (!CommonUtils.checkMandtoryFieldsNullOrEmptyTripCategory(tripcategoryDto)) {
				throw new TSMSException(TSMSError.MANDOTORY_FIELDS_EMPTY);
			}
			
	        TripCategoryDto dto = convertEntityToDto(service.save(convertDtoToEntity(tripcategoryDto), requestId));
	        response.setRequestId(requestId);
	        response.setData(dto);
	        response.setMessage("TripCategory Saved Successfully");
	        response.setStatus(TSMSError.OK.getStatus());
	        response.setTimestamp(LocalDateTime.now().toString());
	        
	        LOGGER.info("END [REST-LAYER] [RequestId={}] saveTripCategory: timeTaken={}|response={}", requestId,
	        		CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
	
	        return ResponseEntity.ok(response);
	        
		} catch (TSMSException e) {
			response.setMessage(e.getMessage());
			response.setStatus(TSMSError.UNAUTHORIZED.getStatus());
			LOGGER.error("Error occurred while saving trip category", e);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		} catch (Exception e) {
			LOGGER.error("Error occurred while saving trip category", e);
			return ResponseEntity.badRequest().build();
		}
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TSMSResponse> updateTripCategory(@PathVariable Long id, @RequestBody TripCategoryDto updatedTripCategoryDto, 
            @RequestParam("requestId") String requestId) throws TSMSException {

        TSMSResponse response = new TSMSResponse();
        try {

            long startTime = System.currentTimeMillis();
            LOGGER.info("START [REST-LAYER] [RequestId={}] updateTripCategory: request={}",requestId,
                    CommonUtils.convertToString(updatedTripCategoryDto));
            
            
            Optional<TripCategory> existingTripCategoryOptional = service.getCategoryById(id);
            if (existingTripCategoryOptional.isEmpty()) {
                response.setMessage("TripCategory not found");
                response.setStatus(TSMSError.NOT_FOUND.getStatus());
                LOGGER.error("TripCategory not found for id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            TripCategory existingTripCategory = existingTripCategoryOptional.get();
            
            if (!CommonUtils.checkMandtoryFieldsNullOrEmptyTripCategory(updatedTripCategoryDto)) {
                throw new TSMSException(TSMSError.MANDOTORY_FIELDS_EMPTY);
            }

            existingTripCategory.setCategoryName(updatedTripCategoryDto.getCategoryName());
            TripCategory updatedTripCategory = service.updateCategory(id, updatedTripCategoryDto, requestId);
            TripCategoryDto updatedTripCategoryDtoResponse = convertEntityToDto(updatedTripCategory);

            response.setData(updatedTripCategoryDtoResponse);
            response.setMessage("TripCategory updated successfully");
            response.setStatus(TSMSError.OK.getStatus());
            response.setTimestamp(LocalDateTime.now().toString());
            LOGGER.info("END [REST-LAYER] [RequestId={}] updateTripCategory: timeTaken={}|response={}", requestId,
                    CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
            return ResponseEntity.ok(response);

        } catch (TSMSException e) {
            response.setMessage(e.getMessage());
            response.setStatus(TSMSError.INVALID_REQUEST.getStatus());
            LOGGER.error("Error occurred while updating trip category", e);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/get-all")
    public ResponseEntity<TSMSResponse> getAllTripCategories(String requestId){

        TSMSResponse response = new TSMSResponse();
        
        long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] getAllTripCategories: request={}", requestId,
				CommonUtils.convertToString(requestId));
        try {
            List<TripCategory>tripCategories = service.getAllCategories();
            List<TripCategoryDto> tripCategorydtos = tripCategories.stream().map(this::convertEntityToDto).collect(Collectors.toList());
            response.setData(tripCategorydtos);
            response.setMessage("TripCategory List");
            response.setSuccess(true);
            response.setStatus(TSMSError.OK.getStatus());
            response.setTimestamp(LocalDateTime.now().toString());
            LOGGER.info("END [REST-LAYER] [RequestId={}] getAllTripCategories: timeTaken={}|response={}", requestId,
            	CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Trip Category Can't Retrieved"+ e.getMessage());
            response.setSuccess(false);
            response.setStatus(TSMSError.NOT_FOUND.getStatus());
            LOGGER.error("Error occurred while getting trip categories", e);
            return ResponseEntity.badRequest().body(response);

        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TSMSResponse> deleteTripCategory(@PathVariable Long id, String requestId) {
    	
        TSMSResponse response = new TSMSResponse();
        
        long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] deleteTripCategory: request={}", requestId,
				CommonUtils.convertToString(id));
        try {
        	
            Optional<TripCategory> existingTripCategory = service.getCategoryById(id);

            if (existingTripCategory != null) {
                service.deleteCategory(id);
                response.setMessage("Trip Category Deleted Successfully");
                response.setSuccess(true);
                response.setStatus(TSMSError.OK.getStatus());
                response.setTimestamp(LocalDateTime.now().toString());
				LOGGER.info("END [REST-LAYER] [RequestId={}] deleteTripCategory: timeTaken={}|response={}", requestId,
						CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
                return ResponseEntity.ok(response);
            } else {
                response.setMessage("Trip Category Not Found");
                response.setStatus(TSMSError.NOT_FOUND.getStatus());
                LOGGER.error("Error occurred while deleting trip category");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
		} catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    private TripCategoryDto convertEntityToDto(TripCategory tripcategory) {

        TripCategoryDto tripCategoryDto = new TripCategoryDto();

        tripCategoryDto.setId(tripcategory.getId());
        tripCategoryDto.setCategoryName(tripcategory.getCategoryName());
        tripCategoryDto.setDescription(tripcategory.getDescription());
        tripCategoryDto.setCode(tripcategory.getCode());
        tripCategoryDto.setStatus(tripcategory.isStatus());
        tripCategoryDto.setAddedAt(tripcategory.getAddedAt());
        tripCategoryDto.setRemovedAt(tripcategory.getRemovedAt());

        return tripCategoryDto;
    }

    private TripCategory convertDtoToEntity(TripCategoryDto tripCategoryDto) {
        TripCategory tripcategory = new TripCategory();
        tripcategory.setId(tripCategoryDto.getId());
        tripcategory.setCategoryName(tripCategoryDto.getCategoryName());
        tripcategory.setDescription(tripCategoryDto.getDescription());
        tripcategory.setCode(tripCategoryDto.getCode());
        tripcategory.setStatus(tripCategoryDto.isStatus());
        tripcategory.setAddedAt(tripCategoryDto.getAddedAt());
        tripcategory.setRemovedAt(tripCategoryDto.getRemovedAt());

        return tripcategory;
    }
}
