package com.sep.tripmanagementservice.configuration.service;

import com.sep.tripmanagementservice.configuration.dto.TripDto;
import com.sep.tripmanagementservice.configuration.dto.response.TripResponseDto;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;

public interface TripService {
	public TripResponseDto createNewTrip(TripDto tripDto, Long user) throws TSMSException;
	public TripResponseDto updateTripDetails(TripDto tripDto, Long user) throws TSMSException;
	public TripResponseDto getTripDetailsForUserId(Long userId, CommonStatus status);
	public TripResponseDto updateTripOptions(TripDto tripDto, Long user) throws TSMSException;
	public TripResponseDto addTripMedia(TripDto tripDto, Long user) throws TSMSException;
	public TripResponseDto deleteTripMedia(Long tripId, Long docId, Long user) throws TSMSException;
	public TripResponseDto deleteTrip(Long tripId, Long user) throws TSMSException;

}
