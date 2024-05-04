package com.sep.tripmanagementservice.configuration.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sep.tripmanagementservice.configuration.dto.TripDto;
import com.sep.tripmanagementservice.configuration.dto.UserDto;
import com.sep.tripmanagementservice.configuration.dto.response.TripResponseDto;
import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.entity.Document;
import com.sep.tripmanagementservice.configuration.entity.Trip;
import com.sep.tripmanagementservice.configuration.entity.TripCategory;
import com.sep.tripmanagementservice.configuration.entity.TripOption;
import com.sep.tripmanagementservice.configuration.entity.TripOptionSelection;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.repository.TripRepository;
import com.sep.tripmanagementservice.configuration.repository.DocumentRepository;
import com.sep.tripmanagementservice.configuration.repository.TripCategoryRepository;
import com.sep.tripmanagementservice.configuration.repository.TripOptionRepository;
import com.sep.tripmanagementservice.configuration.repository.TripOptionSelectionRepository;
import com.sep.tripmanagementservice.configuration.service.S3BucketService;
import com.sep.tripmanagementservice.configuration.service.TripService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;

@Service
public class TripServiceImpl implements TripService {
	@Autowired
	private TripRepository tripRepository;
	@Autowired
	private TripOptionRepository tripOptionRepository;
	@Autowired
	private TripOptionSelectionRepository tripOptionSelectionRepository;
	@Autowired
	private TripCategoryRepository tripCategoryRepository;
	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private S3BucketService s3BucketService;
	@Value("${aws.media.bucketName}")
	private String mediaBucketName;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TripServiceImpl.class);

	@Override
	@Transactional
	public TripResponseDto createNewTrip(TripDto tripDto, UserDto user, String requestId) throws TSMSException{
		List<Document> documents = new ArrayList<Document>();
		try {
			for(MultipartFile file : tripDto.getDocuments()) {
				String s3URL = s3BucketService.uploadFile(CommonUtils.convertMultipartFileToBase64(file),file.getOriginalFilename(), user.getUserName(), mediaBucketName, requestId);
				Document tempDoc = new Document(file.getOriginalFilename(), s3URL, LocalDateTime.now(), user.getId(), CommonStatus.ACTIVE);
			    documents.add(tempDoc);
			}
		} catch (TSMSException e) {
			LOGGER.error("(createNewTrip) requestId={} | TSMSError={}", requestId, TSMSError.DOCUMENT_UPLOAD_FAILD);
			e.printStackTrace();
			throw new TSMSException(TSMSError.DOCUMENT_UPLOAD_FAILD);
		} catch (IOException e) {
			LOGGER.error("(createNewTrip) requestId={} | TSMSError={}", requestId, TSMSError.DOCUMENT_CONVERSION_FAILED);
			e.printStackTrace();
			throw new TSMSException(TSMSError.DOCUMENT_CONVERSION_FAILED);
		}
		List<Document> savedDocs = documentRepository.saveAll(documents);
		
		List<Long> req_tripCategoryIds = new ArrayList<Long>();
		for(TripCategoryDto t : tripDto.getTripCategoryList()) {
			req_tripCategoryIds.add(t.getId());
		}
		List<TripCategory> tripCategories = tripCategoryRepository.findAllById(req_tripCategoryIds);
		
		tripDto.setStatus(CommonStatus.ACTIVE);
		Trip trip = tripDto.convertDtoToTrip(LocalDateTime.now(), user.getId());
			trip.setTripCategoryList(tripCategories);
			trip.setDocuments(savedDocs);
		Trip savedTrip = tripRepository.save(trip);
		
		if(tripDto.getTripOptionList()!=null && tripDto.getTripOptionList().size()>0) {
			List<TripOption> tripOptionList = tripDto.convertDtoToTripOption(LocalDateTime.now(), user.getId(), savedTrip, CommonStatus.ACTIVE);
			tripOptionRepository.saveAll(tripOptionList);
		}
		return new TripResponseDto();
	}

	@Override
	@Transactional
	public TripResponseDto updateTripDetails(TripDto tripDto, Long user, String requestId) throws TSMSException {
		if(tripRepository.findCountByIdForUser(tripDto.getId(),user)==0) {
			LOGGER.error("(updateTripDetails) requestId={} | TSMSError={}", requestId, TSMSError.NON_EXISTING_TRIP_ID_FOR_USER);
			throw new TSMSException(TSMSError.NON_EXISTING_TRIP_ID_FOR_USER);
		}
		List<Long> req_tripCategoryIds = new ArrayList<Long>();
		for(TripCategoryDto t : tripDto.getTripCategoryList()) {
			req_tripCategoryIds.add(t.getId());
		}
		List<TripCategory> tripCategories = tripCategoryRepository.findAllById(req_tripCategoryIds);
		List<Document> docs = documentRepository.findByTripIdAndStatus(tripDto.getId(), CommonStatus.ACTIVE);
		tripDto.setStatus(CommonStatus.ACTIVE);
		Trip trip = tripDto.convertDtoToTrip(LocalDateTime.now(), user);
			trip.setTripCategoryList(tripCategories);
			trip.setDocuments(docs);
		tripRepository.save(trip);
		return new TripResponseDto();
	}

	@Override
	@Transactional
	public TripResponseDto updateTripOptions(TripDto tripDto, Long user, String requestId) throws TSMSException {
		if(tripRepository.findCountByIdForUser(tripDto.getId(),user)==0) {
			LOGGER.error("(updateTripOptions) requestId={} | TSMSError={}", requestId, TSMSError.NON_EXISTING_TRIP_ID_FOR_USER);
			throw new TSMSException(TSMSError.NON_EXISTING_TRIP_ID_FOR_USER);
		}
		List<TripOption> reqTripOptions = tripDto.convertDtoToTripOption(LocalDateTime.now(), user, new Trip(tripDto.getId()), CommonStatus.ACTIVE);
		for(TripOption o : reqTripOptions) {
			o.setId(null);
			for(TripOptionSelection s: o.getTripOptionSelection()) {
				s.setId(null);
			}
		}
		tripOptionRepository.updateStatusByTripIdStatus(CommonStatus.INACTIVE, tripDto.getId());
		tripOptionSelectionRepository.updateStatusByTripIdStatus(CommonStatus.INACTIVE.name(), tripDto.getId());
		tripOptionRepository.saveAll(reqTripOptions);
		return new TripResponseDto();
	}
	
	@Override
	public TripResponseDto getTripDetailsForUserId(Long userId, CommonStatus status) {
		List<Trip> trips = tripRepository.findByUserIdAndStatus(userId, status);
		for(int i=0; i<trips.size(); i++) {
			for(int j=0; j<trips.get(i).getTripOptionList().size(); j++) {
				if(trips.get(i).getTripOptionList().get(j).getStatus()==CommonStatus.INACTIVE) {
					trips.get(i).getTripOptionList().remove(j);
					j--;
					continue;
				}
				for(int k=0; k<trips.get(i).getTripOptionList().get(j).getTripOptionSelection().size(); k++) {
					if(trips.get(i).getTripOptionList().get(j).getTripOptionSelection().get(k).getStatus()==CommonStatus.INACTIVE) {
						trips.get(i).getTripOptionList().get(j).getTripOptionSelection().remove(k);
						k--;
						continue;
					}
				}
			}
		}
		List<TripDto> tripDtos = new ArrayList<TripDto>();
		for(Trip t: trips) {
			tripDtos.add(t.convertTripToDto());
		}
		TripResponseDto res = new TripResponseDto();
			res.setTripDto(tripDtos);
		return res;
	}
	
	@Override
	@Transactional
	public TripResponseDto addTripMedia(TripDto tripDto, UserDto user, String requestId) throws TSMSException {
		Trip trip = tripRepository.getTripByIdForUser(tripDto.getId(), user.getId());
		if(trip==null) {
			LOGGER.error("(addTripMedia) requestId={} | TSMSError={}", requestId, TSMSError.NON_EXISTING_TRIP_ID_FOR_USER);
			throw new TSMSException(TSMSError.NON_EXISTING_TRIP_ID_FOR_USER);
		}
		
		List<Document> documents = new ArrayList<Document>();
		try {
			for(MultipartFile file : tripDto.getDocuments()) {
				String s3URL = s3BucketService.uploadFile(CommonUtils.convertMultipartFileToBase64(file),file.getOriginalFilename(), user.getUserName(), mediaBucketName, requestId);
				Document tempDoc = new Document(file.getOriginalFilename(), s3URL, LocalDateTime.now(), user.getId(), CommonStatus.ACTIVE);
			    documents.add(tempDoc);
			}
		} catch (TSMSException e) {
			LOGGER.error("(addTripMedia) requestId={} | TSMSError={}", requestId, TSMSError.DOCUMENT_UPLOAD_FAILD);
			e.printStackTrace();
			throw new TSMSException(TSMSError.DOCUMENT_UPLOAD_FAILD);
		} catch (IOException e) {
			LOGGER.error("(addTripMedia) requestId={} | TSMSError={}", requestId, TSMSError.DOCUMENT_CONVERSION_FAILED);
			e.printStackTrace();
			throw new TSMSException(TSMSError.DOCUMENT_CONVERSION_FAILED);
		}
		
		List<Document> savedDocs = documentRepository.saveAll(documents);
		trip.getDocuments().addAll(savedDocs);
		tripRepository.save(trip);
		return new TripResponseDto();
	}
	
	@Override
	public TripResponseDto deleteTripMedia(Long tripId, Long docId, Long user, String requestId) throws TSMSException {
		Trip trip = tripRepository.getTripByIdForUser(tripId, user);
		if(trip==null) {
			LOGGER.error("(deleteTripMedia) requestId={} | TSMSError={}", requestId, TSMSError.NON_EXISTING_TRIP_ID_FOR_USER);
			throw new TSMSException(TSMSError.NON_EXISTING_TRIP_ID_FOR_USER);
		}
		try {
			for(int i=0; i<trip.getDocuments().size();i++) {
				if(trip.getDocuments().get(i).getId()==docId) {
					s3BucketService.deleteFile(trip.getDocuments().get(i).getUniqueName(), mediaBucketName, requestId);
					trip.getDocuments().remove(i);
					tripRepository.save(trip);
					return new TripResponseDto();
				}
			}
		}catch(TSMSException e) {
			LOGGER.error("(deleteTripMedia) requestId={} | TSMSError={}", requestId, TSMSError.DOCUMENT_DELETE_FAILD);
			throw new TSMSException(TSMSError.DOCUMENT_DELETE_FAILD);
		}
		LOGGER.error("(deleteTripMedia) requestId={} | TSMSError={}", requestId, TSMSError.DOC_ID_NOT_EXIST_FOR_TRIP_ID);
		throw new TSMSException(TSMSError.DOC_ID_NOT_EXIST_FOR_TRIP_ID);
	}
	
	@Override
	public TripResponseDto deleteTrip(Long tripId, Long user, String requestId) throws TSMSException {
		if(tripRepository.findCountByIdForUser(tripId,user)==0) {
			LOGGER.error("(deleteTrip) requestId={} | TSMSError={}", requestId, TSMSError.NON_EXISTING_TRIP_ID_FOR_USER);
			throw new TSMSException(TSMSError.NON_EXISTING_TRIP_ID_FOR_USER);
		}
		tripRepository.updateStatusByIdStatus(CommonStatus.DELETED, tripId);
		return new TripResponseDto();
	}
	
	
}
