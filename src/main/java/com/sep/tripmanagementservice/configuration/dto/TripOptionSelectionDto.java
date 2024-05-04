package com.sep.tripmanagementservice.configuration.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.tripmanagementservice.configuration.entity.TripOption;
import com.sep.tripmanagementservice.configuration.entity.TripOptionSelection;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripOptionSelectionDto {
	private Long id;
	private String name;
	private String description;
	private Double cost;
	
	public TripOptionSelection convertDtoToTripOptionSelection(LocalDateTime time, Long user, TripOption tripOption, CommonStatus status) {
		TripOptionSelection res = new TripOptionSelection();
			res.setId(this.id);
			res.setName(this.name);
			res.setDescription(this.description);
			res.setCost(this.cost);
			res.setTripOption(tripOption);
			res.setAdded_at(time);
			res.setAdded_by(user);
			res.setStatus(status);
		return res;
	}
	
}
