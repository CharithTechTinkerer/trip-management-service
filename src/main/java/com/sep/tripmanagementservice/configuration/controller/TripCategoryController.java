package com.sep.tripmanagementservice.configuration.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sep.tripmanagementservice.configuration.dto.response.TSMSResponse;
import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.entity.tripcategory.TripCategory;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/private/tripcategories")
public class TripCategoryController {

    @Autowired
    TripCategoryService service;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TripCategoryController.class);

    @PostMapping
    public ResponseEntity<TSMSResponse> saveTripCategory(@RequestParam("requestId") String requestId, @RequestBody TripCategoryDto tripcategoryDto) throws TSMSException{
    	long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] saveTest: request={}", requestId,
				CommonUtils.convertToString(tripcategoryDto));
		TSMSResponse response = new TSMSResponse();
		
		if (!CommonUtils.checkMandtoryFieldsNullOrEmptyTripCategory(tripcategoryDto)) {
			throw new TSMSException(TSMSError.MANDOTORY_FIELDS_EMPTY);
		}
		
        TripCategoryDto dto = convertEntityToDto(service.save(convertDtoToEntity(tripcategoryDto), requestId));
        response.setRequestId(requestId);
        response.setData(dto);
        response.setMessage("TripCategory Saved Successfully");
        response.setStatus(TSMSError.OK.getStatus());
        response.setTimestamp(LocalDateTime.now().toString());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TSMSResponse> updateTripCategory(@PathVariable UUID id, @RequestBody TripCategoryDto updatedTripCategoryDto){
        TSMSResponse response = new TSMSResponse();
        try {
            Optional<TripCategory> existingTripCategory = service.getCategoryById(id);

            if (existingTripCategory.isPresent()) {

                TripCategory tripCategory = existingTripCategory.get();
                tripCategory.setCategory_name(updatedTripCategoryDto.getCategory_name());
                TripCategory updatedTripCategory = service.updateCategory(existingTripCategory, updatedTripCategoryDto, "reqeuestId");
                TripCategoryDto updatedTripCategoryDtoResponse = convertEntityToDto(updatedTripCategory);

                response.setData(updatedTripCategoryDtoResponse);
                response.setMessage("TripCategory updated successfully");
                response.setStatus(TSMSError.OK.getStatus());
                response.setTimestamp(LocalDateTime.now().toString());
                return ResponseEntity.ok(response);
            } else {
                response.setMessage("TripCategory not found");
                response.setStatus(TSMSError.NOT_FOUND.getStatus());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } finally {
            response.setRequestId("requestId");
        }
    }


    @GetMapping
    public ResponseEntity<TSMSResponse> getAllTripCategories(){

        TSMSResponse response = new TSMSResponse();
        try {
            List<TripCategory>tripCategories = service.getAllCategories();
            List<TripCategoryDto> tripCategorydtos = tripCategories.stream().map(this::convertEntityToDto).collect(Collectors.toList());
            response.setData(tripCategorydtos);
            response.setMessage("TripCategory List");
            response.setSuccess(true);
            response.setStatus(TSMSError.OK.getStatus());
            response.setTimestamp(LocalDateTime.now().toString());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Trip Category Can't Retrieved"+ e.getMessage());
            response.setSuccess(false);
            response.setStatus(TSMSError.NOT_FOUND.getStatus());
            return ResponseEntity.badRequest().body(response);

        } finally {
            response.setRequestId("requestId");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TSMSResponse> deleteTripCategory(@PathVariable UUID id) {
    	
        TSMSResponse response = new TSMSResponse();

        try {
            Optional<TripCategory> existingTripCategory = service.getCategoryById(id);

            if (existingTripCategory.isPresent()) {
                service.deleteCategory(id);
                response.setMessage("Trip Category Deleted Successfully");
                response.setSuccess(true);
                response.setStatus(TSMSError.OK.getStatus());
                response.setTimestamp(LocalDateTime.now().toString());
                return ResponseEntity.ok(response);
            } else {
                response.setMessage("Trip Category Not Found");
                response.setStatus(TSMSError.NOT_FOUND.getStatus());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } finally {
            response.setRequestId("requestId");
        }
    }
    private TripCategoryDto convertEntityToDto(TripCategory tripcategory) {

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

    private TripCategory convertDtoToEntity(TripCategoryDto tripCategoryDto) {
        TripCategory tripcategory = new TripCategory();
        tripcategory.setId(tripCategoryDto.getId());
        tripcategory.setCategory_name(tripCategoryDto.getCategory_name());
        tripcategory.setDescription(tripCategoryDto.getDescription());
        tripcategory.setCode(tripCategoryDto.getCode());
        tripcategory.setStatus(tripCategoryDto.isStatus());
        tripcategory.setAdded_at(tripCategoryDto.getAdded_at());
        tripcategory.setRemoved_at(tripCategoryDto.getRemoved_at());

        return tripcategory;
    }
}
