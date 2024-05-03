package com.sep.tripmanagementservice.configuration.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.entity.Trip;
import com.sep.tripmanagementservice.configuration.entity.TripCategory;
import com.sep.tripmanagementservice.configuration.entity.TripOption;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TripDto {
	private Long id;
	private List<TripCategoryDto> tripCategoryList;
	private String tripName;
	private String description;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private LocalDateTime reservationCloseDate;
	private LocalDateTime finalCancelDate;
	private int maxParticipantCount;
	private String advertise_tripName;
	private String adertise_tripDetails;
	private List<TripOptionDto> tripOptionList;
	private CommonStatus status;
	private MultipartFile[] documents;
	private List<DocumentDto> documentList;
	
	
	public Trip convertDtoToTrip(LocalDateTime time, Long user) {
		Trip trip = new Trip();
			trip.setId(this.id);
			List<TripCategory> tripCategories = new ArrayList<TripCategory>();
			for(TripCategoryDto c :tripCategoryList) {
				tripCategories.add(c.convertDtoToTripCategory());
			}
			trip.setTripCategoryList(tripCategories);
			trip.setTripName(this.tripName);
			trip.setDescription(this.description);
			trip.setFromDate(this.fromDate);
			trip.setToDate(this.toDate);
			trip.setReservationCloseDate(this.reservationCloseDate);
			trip.setFinalCancelDate(this.finalCancelDate);
			trip.setMaxParticipantCount(this.maxParticipantCount);
			trip.setAdvertise_tripName(this.advertise_tripName);
			trip.setAdertise_tripDetails(this.adertise_tripDetails);
			trip.setAdded_at(time);
			trip.setAdded_by(user);
			trip.setStatus(this.status);
		return trip;
	}
	
	public List<TripOption> convertDtoToTripOption(LocalDateTime time, Long user, Trip trip, CommonStatus status) {
		List<TripOption> res = new ArrayList<TripOption>();
		for(TripOptionDto t : this.getTripOptionList()) {
			res.add(t.convertDtoToTripOption(time,user,trip,status));
		}
		return res;
	}

	
	
}




