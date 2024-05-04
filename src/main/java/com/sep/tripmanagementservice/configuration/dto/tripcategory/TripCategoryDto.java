package com.sep.tripmanagementservice.configuration.dto.tripcategory;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.tripmanagementservice.configuration.entity.TripCategory;
import com.sep.tripmanagementservice.configuration.enums.TripCategoryStatus;

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
	private TripCategoryStatus status;
	private String createdBy;
	private String updatedBy;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	
	public TripCategory convertDtoToTripCategory() {
		TripCategory res = new TripCategory();
			res.setId(this.id);
			res.setName(this.name);
			res.setCode(this.code);
			res.setDescription(this.description);
			res.setStatus(this.status);
		return res;
	}
	
	
}
