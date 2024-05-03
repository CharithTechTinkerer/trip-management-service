package com.sep.tripmanagementservice.configuration.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.tripmanagementservice.configuration.dto.TripOptionDto;
import com.sep.tripmanagementservice.configuration.dto.TripOptionSelectionDto;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "trip_option")
public class TripOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@OneToMany(mappedBy="tripOption", cascade = CascadeType.ALL)
	private List<TripOptionSelection> tripOptionSelection;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="trip_id", nullable=false)
	private Trip trip;
	private LocalDateTime added_at;
	private Long added_by;
	
	@Enumerated(EnumType.STRING)
	private CommonStatus status;

	
	public TripOptionDto convertToTripOptiontDto() {
		TripOptionDto res = new TripOptionDto();
			res.setId(this.id);
			res.setName(this.name);
		List<TripOptionSelectionDto> tripOptionSelection = new ArrayList<TripOptionSelectionDto>(); 
		for(TripOptionSelection t : this.tripOptionSelection) {
			tripOptionSelection.add(t.convertToTripOptionSelectionDto());
		}
		res.setTripOptionSelection(tripOptionSelection);
		return res;
	}
	

}
