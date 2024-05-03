package com.sep.tripmanagementservice.configuration.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Table(name = "trip_option_selection")
public class TripOptionSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private Double cost;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="trip_option_id")
	private TripOption tripOption;
	private LocalDateTime added_at;
	private Long added_by;
	
	@Enumerated(EnumType.STRING)
	private CommonStatus status;
	

	public TripOptionSelectionDto convertToTripOptionSelectionDto() {
		TripOptionSelectionDto res = new TripOptionSelectionDto();
			res.setId(this.id);
			res.setName(this.name);
			res.setDescription(this.description);
			res.setCost(this.cost);
		return res;
	}
	
	
	
}
