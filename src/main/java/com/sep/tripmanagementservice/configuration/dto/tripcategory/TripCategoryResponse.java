package com.sep.tripmanagementservice.configuration.dto.tripcategory;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripCategoryResponse {

	Long count;
	List<TripCategoryDto> tripCategoryDtos;

}
