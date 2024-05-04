package com.sep.tripmanagementservice.configuration.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.tripmanagementservice.configuration.entity.Trip;
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
public class TripOptionDto {
	private Long id;
	private String name;
	private List<TripOptionSelectionDto> tripOptionSelection;
	
	public TripOption convertDtoToTripOption(LocalDateTime time, Long user, Trip trip, CommonStatus status) {
		TripOption res = new TripOption();
			res.setId(this.id);
			res.setName(this.name);
			res.setAdded_at(time);
			res.setAdded_by(user);
			res.setTrip(trip);
			res.setStatus(status);
		List<TripOptionSelection> tripOptionSelection = new ArrayList<TripOptionSelection>(); 
		for(TripOptionSelectionDto t : this.tripOptionSelection) {
			tripOptionSelection.add(t.convertDtoToTripOptionSelection(time, user, res, status));
		}
		res.setTripOptionSelection(tripOptionSelection);
		return res;
	}
	
	
}
