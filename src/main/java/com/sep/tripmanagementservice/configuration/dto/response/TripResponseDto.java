package com.sep.tripmanagementservice.configuration.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.tripmanagementservice.configuration.dto.TripDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripResponseDto {
	private List<TripDto> tripDto;

}
