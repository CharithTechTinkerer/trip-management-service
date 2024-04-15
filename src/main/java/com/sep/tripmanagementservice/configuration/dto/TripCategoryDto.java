package com.sep.tripmanagementservice.configuration.dto;

import java.time.LocalDateTime;

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
public class TripCategoryDto {

	private Long id;

	private String name;

	private String code;

	private String description;

	private String status;

	private String createdBy;

	private String updatedBy;

	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;
}
