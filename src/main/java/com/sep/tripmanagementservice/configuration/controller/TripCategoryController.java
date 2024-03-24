package com.sep.tripmanagementservice.configuration.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.sep.tripmanagementservice.configuration.dto.response.TSMSResponse;
import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.entity.tripcategory.TripCategoryRepository;
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
@RequestMapping("/v1/private/tripcategories")
public class TripCategoryController {

    @Autowired
    TripCategoryService service;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TripCategoryController.class);

    @PostMapping
    public ResponseEntity<TSMSResponse> saveTripCategory(@RequestParam("requestId") String requestId, @RequestBody TripCategoryDto tripcategoryDto) throws TSMSException{
		
		TSMSResponse response = new TSMSResponse();
		try {
			
			long startTime = System.currentTimeMillis();
			LOGGER.info("START [REST-LAYER] [RequestId={}] saveTripCategory: request={}", requestId,
					CommonUtils.convertToString(tripcategoryDto));
			
            if (!checkUserRoleIsSystemAdmin(requestId)) {
                throw new TSMSException(TSMSError.UNAUTHORIZED);
            }
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
            
            if (!checkUserRoleIsSystemAdmin(requestId)) {
                throw new TSMSException(TSMSError.UNAUTHORIZED);
            }
            
            TripCategoryRepository existingTripCategory = service.getCategoryById(id);
            if (existingTripCategory == null) {
                response.setMessage("TripCategory not found");
                response.setStatus(TSMSError.NOT_FOUND.getStatus());
                LOGGER.error("TripCategory not found for id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if (!CommonUtils.checkMandtoryFieldsNullOrEmptyTripCategory(updatedTripCategoryDto)) {
                throw new TSMSException(TSMSError.MANDOTORY_FIELDS_EMPTY);
            }

            existingTripCategory.setCategory_name(updatedTripCategoryDto.getCategory_name());
            TripCategoryRepository updatedTripCategory = service.updateCategory(id, updatedTripCategoryDto, requestId);
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


    @GetMapping
    public ResponseEntity<TSMSResponse> getAllTripCategories(String requestId){

        TSMSResponse response = new TSMSResponse();
        
        long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] getAllTripCategories: request={}", requestId,
				CommonUtils.convertToString(requestId));
        try {
            List<TripCategoryRepository>tripCategories = service.getAllCategories();
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
        	
			if (!checkUserRoleIsSystemAdmin(requestId)) {
				throw new TSMSException(TSMSError.UNAUTHORIZED);
			}
        	
            TripCategoryRepository existingTripCategory = service.getCategoryById(id);

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
		} catch (TSMSException e) {
			response.setMessage(e.getMessage());
			response.setStatus(TSMSError.UNAUTHORIZED.getStatus());
			LOGGER.error("User not authorized to delete tripcategory", e);
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    private TripCategoryDto convertEntityToDto(TripCategoryRepository tripcategory) {

        TripCategoryDto tripCategoryDto = new TripCategoryDto();

        tripCategoryDto.setId(tripcategory.getId());
        tripCategoryDto.setCategory_name(tripcategory.getCategory_name());
        tripCategoryDto.setDescription(tripcategory.getDescription());
        tripCategoryDto.setCode(tripcategory.getCode());
        tripCategoryDto.setStatus(tripcategory.isStatus());
        tripCategoryDto.setAdded_at(tripcategory.getAdded_at());
        tripCategoryDto.setRemoved_at(tripcategory.getRemoved_at());

        return tripCategoryDto;
    }

    private TripCategoryRepository convertDtoToEntity(TripCategoryDto tripCategoryDto) {
        TripCategoryRepository tripcategory = new TripCategoryRepository();
        tripcategory.setId(tripCategoryDto.getId());
        tripcategory.setCategory_name(tripCategoryDto.getCategory_name());
        tripcategory.setDescription(tripCategoryDto.getDescription());
        tripcategory.setCode(tripCategoryDto.getCode());
        tripcategory.setStatus(tripCategoryDto.isStatus());
        tripcategory.setAdded_at(tripCategoryDto.getAdded_at());
        tripcategory.setRemoved_at(tripCategoryDto.getRemoved_at());

        return tripcategory;
    }
    
    private boolean checkUserRoleIsSystemAdmin(String requestId) throws TSMSException {
        // Your implementation to check if user has the role of system admin
        // Example: You may check this using user roles stored in database or via some authentication mechanism
        // For now, I'm assuming a simple check
		if (requestId.equals("systemadmin")) {
			return true;
		} else {
			return false;
		}

    }
}
