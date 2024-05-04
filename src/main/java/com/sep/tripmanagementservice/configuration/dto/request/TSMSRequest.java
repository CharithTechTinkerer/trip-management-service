package com.sep.tripmanagementservice.configuration.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.tripmanagementservice.configuration.dto.TripDto;
import com.sep.tripmanagementservice.configuration.dto.UserDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TSMSRequest {
	private TripDto tripRequest;
	private UserDto userDto;
	private Long tripId;
	private Long docId;

}
