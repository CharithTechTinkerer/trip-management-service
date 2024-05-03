package com.sep.tripmanagementservice.configuration.dto.response;

import java.util.List;

import com.sep.tripmanagementservice.configuration.dto.TripDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TripResponseDto {
	private List<TripDto> tripDto;

}
