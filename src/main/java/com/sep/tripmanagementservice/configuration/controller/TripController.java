package com.sep.tripmanagementservice.configuration.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.dto.TripOptionDto;
import com.sep.tripmanagementservice.configuration.dto.TripOptionSelectionDto;
import com.sep.tripmanagementservice.configuration.dto.request.TSMSRequest;
import com.sep.tripmanagementservice.configuration.dto.response.TSMSResponse;
import com.sep.tripmanagementservice.configuration.dto.response.TripResponseDto;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.service.TripService;
import com.sep.tripmanagementservice.configuration.utill.REST_CONTROLLER_URL;

@RestController
@RequestMapping("/api/v1/private")
public class TripController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TripController.class);
	
	@Autowired
	TripService tripService;

	@RequestMapping(value = REST_CONTROLLER_URL.CREATE_TRIP, method = RequestMethod.POST)
	public @ResponseBody TSMSResponse createTrip(@RequestPart(value="TSMSRequest", required=true) TSMSRequest tSMSRequest, @RequestPart(value="documents", required=true) MultipartFile[] documents, HttpServletRequest request) throws TSMSException{
		if(tSMSRequest.getUserDto()==null || tSMSRequest.getUserDto().getId()==null) { throw new TSMSException(TSMSError.USER_ID_NOT_FOUND); }
		boolean isValidationFail = false;
		if(tSMSRequest.getTripRequest().getTripName()==null || tSMSRequest.getTripRequest().getTripName().trim().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getDescription()==null || tSMSRequest.getTripRequest().getDescription().trim().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getTripCategoryList()==null || tSMSRequest.getTripRequest().getTripCategoryList().size()==0) { isValidationFail= true; }
		for(TripCategoryDto category: tSMSRequest.getTripRequest().getTripCategoryList()) {
			if(category==null || category.getId()==null) {
				 isValidationFail = true;
				 break;
			}
		}
		if(tSMSRequest.getTripRequest().getFromDate()==null) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getToDate()==null) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getToDate().isBefore(tSMSRequest.getTripRequest().getFromDate())) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getReservationCloseDate()==null) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getReservationCloseDate().isAfter(tSMSRequest.getTripRequest().getToDate()) || tSMSRequest.getTripRequest().getReservationCloseDate().isBefore(tSMSRequest.getTripRequest().getFromDate())) {
			 isValidationFail= true;
		}
		if(tSMSRequest.getTripRequest().getAdvertise_tripName()==null || tSMSRequest.getTripRequest().getAdvertise_tripName().trim().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getAdertise_tripDetails()==null || tSMSRequest.getTripRequest().getAdertise_tripDetails().trim().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getTripOptionList()!=null && tSMSRequest.getTripRequest().getTripOptionList().size()!=0) {
			outer:
			for(TripOptionDto option: tSMSRequest.getTripRequest().getTripOptionList()) {
				if(option==null || option.getName()==null || option.getName().trim().length()==0) {
					 isValidationFail = true;
					 break;
				}
				if(option.getTripOptionSelection()==null || option.getTripOptionSelection().size()<2){
					 isValidationFail = true;
					 break;
				}
				for(TripOptionSelectionDto optionSelection: option.getTripOptionSelection()) {
					if(optionSelection==null || optionSelection.getName()==null || optionSelection.getName().trim().length()==0) {
						 isValidationFail = true;
						 break outer;
					}
				}
			}
		}
		if(isValidationFail) { throw new TSMSException(TSMSError.INVALID_REQUEST); }
		if(documents.length==0) { throw new TSMSException(TSMSError.DOCUMENT_NOT_FOUND); }
		
		tSMSRequest.getTripRequest().setDocuments(documents);
		tripService.createNewTrip(tSMSRequest.getTripRequest(), tSMSRequest.getUserDto().getId());
		TSMSResponse res = new TSMSResponse();
			res.setSuccess(true);
			res.setMessage("Success");
			res.setStatus(200);
			res.setTimestamp(LocalDateTime.now().toString());
		return res;
	}
	
	@RequestMapping(value = REST_CONTROLLER_URL.UPDATE_TRIP_MAIN_DETAILS, method = RequestMethod.POST)
	public @ResponseBody TSMSResponse updateTripMainDetails(@RequestBody TSMSRequest tSMSRequest, HttpServletRequest request) throws TSMSException{
		if(tSMSRequest.getUserDto()==null || tSMSRequest.getUserDto().getId()==null) { throw new TSMSException(TSMSError.USER_ID_NOT_FOUND); }
		boolean isValidationFail = false;
		if(tSMSRequest.getTripRequest().getId()==null || tSMSRequest.getTripRequest().getId()<0) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getTripName()==null || tSMSRequest.getTripRequest().getTripName().trim().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getDescription()==null || tSMSRequest.getTripRequest().getDescription().trim().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getTripCategoryList()==null || tSMSRequest.getTripRequest().getTripCategoryList().size()==0) { isValidationFail= true; }
		for(TripCategoryDto category: tSMSRequest.getTripRequest().getTripCategoryList()) {
			if(category==null || category.getId()==null) {
				 isValidationFail = true;
			}
		}
		if(tSMSRequest.getTripRequest().getFromDate()==null) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getToDate()==null) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getToDate().isBefore(tSMSRequest.getTripRequest().getFromDate())) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getReservationCloseDate()==null) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getReservationCloseDate().isAfter(tSMSRequest.getTripRequest().getToDate()) || tSMSRequest.getTripRequest().getReservationCloseDate().isBefore(tSMSRequest.getTripRequest().getFromDate())) {
			 isValidationFail = true;
		}
		if(tSMSRequest.getTripRequest().getAdvertise_tripName()==null || tSMSRequest.getTripRequest().getAdvertise_tripName().trim().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getAdertise_tripDetails()==null || tSMSRequest.getTripRequest().getAdertise_tripDetails().trim().length()==0) { isValidationFail= true; }
		if(isValidationFail) { throw new TSMSException(TSMSError.INVALID_REQUEST); }
		
		tripService.updateTripDetails(tSMSRequest.getTripRequest(), tSMSRequest.getUserDto().getId());
		TSMSResponse res = new TSMSResponse();
			res.setSuccess(true);
			res.setMessage("Success");
			res.setStatus(200);
			res.setTimestamp(LocalDateTime.now().toString());
		return res;
	}
	
	@RequestMapping(value = REST_CONTROLLER_URL.UPDATE_TRIP_OPTIONS, method = RequestMethod.POST)
	public @ResponseBody TSMSResponse updateTripOptions(@RequestBody TSMSRequest tSMSRequest, HttpServletRequest request) throws TSMSException{
		if(tSMSRequest.getUserDto()==null || tSMSRequest.getUserDto().getId()==null) { throw new TSMSException(TSMSError.USER_ID_NOT_FOUND); }
		boolean isValidationFail = false;
		if(tSMSRequest.getTripRequest().getId()==null || tSMSRequest.getTripRequest().getId()<0) { isValidationFail= true; }
		if(tSMSRequest.getTripRequest().getTripOptionList()!=null && tSMSRequest.getTripRequest().getTripOptionList().size()!=0) {
			outer:
			for(TripOptionDto option: tSMSRequest.getTripRequest().getTripOptionList()) {
				if(option==null || option.getName()==null || option.getName().trim().length()==0) {
					 isValidationFail = true;
					 break;
				}
				if (option.getTripOptionSelection()==null || option.getTripOptionSelection().size()<2){
					 isValidationFail = true;
					 break;
				}
				for(TripOptionSelectionDto optionSelection: option.getTripOptionSelection()) {
					if(optionSelection==null || optionSelection.getName()==null || optionSelection.getName().trim().length()==0) {
						 isValidationFail = true;
						 break outer;
					}
				}
			}
		}
		if(isValidationFail) { throw new TSMSException(TSMSError.INVALID_REQUEST); }
		
		tripService.updateTripOptions(tSMSRequest.getTripRequest(), tSMSRequest.getUserDto().getId());
		TSMSResponse res = new TSMSResponse();
			res.setSuccess(true);
			res.setMessage("Success");
			res.setStatus(200);
			res.setTimestamp(LocalDateTime.now().toString());
		return res;
	}
	
	@RequestMapping(value = REST_CONTROLLER_URL.GET_TRIP_DETAILS_BY_ID, method = RequestMethod.GET)
	public @ResponseBody TSMSResponse getTripByUserID(@RequestParam Long userId, @RequestParam CommonStatus status, HttpServletRequest request) throws TSMSException{
		boolean isValidationFail = false;
		if(userId==null || userId<0l) { isValidationFail = true; }
		if(status==null) { isValidationFail = true; }
		if(isValidationFail) { throw new TSMSException(TSMSError.INVALID_REQUEST); }

		TripResponseDto data = tripService.getTripDetailsForUserId(userId, status);
		TSMSResponse res = new TSMSResponse();
			res.setData(data);
			res.setSuccess(true);
			res.setMessage("Success");
			res.setStatus(200);
			res.setTimestamp(LocalDateTime.now().toString());
		return res;
	}
	
	@RequestMapping(value = REST_CONTROLLER_URL.ADD_TRIP_MEDIA, method = RequestMethod.POST)
	public @ResponseBody TSMSResponse addTripMedia(@RequestPart(value="TSMSRequest", required=true) TSMSRequest tSMSRequest, @RequestPart(value="documents", required=true) MultipartFile[] documents, HttpServletRequest request) throws TSMSException{
		if(tSMSRequest.getUserDto()==null || tSMSRequest.getUserDto().getId()==null) { throw new TSMSException(TSMSError.USER_ID_NOT_FOUND); }
		if(documents.length==0) { throw new TSMSException(TSMSError.DOCUMENT_NOT_FOUND); }
		
		tSMSRequest.getTripRequest().setDocuments(documents);
		tripService.addTripMedia(tSMSRequest.getTripRequest(), tSMSRequest.getUserDto().getId());
		TSMSResponse res = new TSMSResponse();
			res.setSuccess(true);
			res.setMessage("Success");
			res.setStatus(200);
			res.setTimestamp(LocalDateTime.now().toString());
		return res;
	}

	@RequestMapping(value = REST_CONTROLLER_URL.REMOVE_TRIP_MEDIA, method = RequestMethod.DELETE)
	public @ResponseBody TSMSResponse removeTripMedia(@RequestBody TSMSRequest tSMSRequest, HttpServletRequest request) throws TSMSException{
		if(tSMSRequest.getUserDto()==null || tSMSRequest.getUserDto().getId()==null) { throw new TSMSException(TSMSError.USER_ID_NOT_FOUND); }
		boolean isValidationFail = false;
		if(tSMSRequest.getTripId()==null || tSMSRequest.getTripId()<0) { isValidationFail= true; }
		if(tSMSRequest.getDocId()==null || tSMSRequest.getDocId()<0) { isValidationFail= true; }
		if(isValidationFail) { throw new TSMSException(TSMSError.INVALID_REQUEST); }

		tripService.deleteTripMedia(tSMSRequest.getTripId(),tSMSRequest.getDocId(), tSMSRequest.getUserDto().getId());
		TSMSResponse res = new TSMSResponse();
			res.setSuccess(true);
			res.setMessage("Success");
			res.setStatus(200);
			res.setTimestamp(LocalDateTime.now().toString());
		return res;
	}
	
	@RequestMapping(value = REST_CONTROLLER_URL.REMOVE_TRIP, method = RequestMethod.DELETE)
	public @ResponseBody TSMSResponse removeTrip(@RequestBody TSMSRequest tSMSRequest, HttpServletRequest request) throws TSMSException{
		if(tSMSRequest.getUserDto()==null || tSMSRequest.getUserDto().getId()==null) { throw new TSMSException(TSMSError.USER_ID_NOT_FOUND); }
		boolean isValidationFail = false;
		if(tSMSRequest.getTripId()==null || tSMSRequest.getTripId()<0) { isValidationFail= true; }
		if(isValidationFail) { throw new TSMSException(TSMSError.INVALID_REQUEST); }

		tripService.deleteTrip(tSMSRequest.getTripId(), tSMSRequest.getUserDto().getId());
		TSMSResponse res = new TSMSResponse();
			res.setSuccess(true);
			res.setMessage("Success");
			res.setStatus(200);
			res.setTimestamp(LocalDateTime.now().toString());
		return res;
	}
	
	
}
