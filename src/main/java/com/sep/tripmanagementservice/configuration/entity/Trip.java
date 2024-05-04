package com.sep.tripmanagementservice.configuration.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import com.sep.tripmanagementservice.configuration.dto.DocumentDto;
import com.sep.tripmanagementservice.configuration.dto.TripDto;
import com.sep.tripmanagementservice.configuration.dto.TripOptionDto;
import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
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
@Table(name = "trip")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    @ManyToMany(cascade={ CascadeType.MERGE })
    @JoinTable(
        name = "trip_trip_category",
        joinColumns = { @JoinColumn(name = "trip_id") },
        inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
	private List<TripCategory> tripCategoryList;
    
	@Column(name = "trip_name", nullable = false)
	private String tripName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "from_date", nullable = false)
	private LocalDateTime fromDate;
	
	@Column(name = "to_date", nullable = false)
	private LocalDateTime toDate;
	
	@Column(name = "reservation_close_date")
	private LocalDateTime reservationCloseDate;
	
	@Column(name = "final_cancel_date")
	private LocalDateTime finalCancelDate;
	
	@Column(name = "max_participant_count")
	private Integer maxParticipantCount;
	
	@Column(name = "advertise_trip_name")
	private String advertise_tripName;
	
	@Column(name = "adertise_trip_details")
	private String adertise_tripDetails;
	
	@OneToMany(mappedBy="trip",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<TripOption> tripOptionList;
	
	@Column(name = "added_at", nullable = false)
	private LocalDateTime added_at;
	
	@Column(name = "added_by", nullable = false)
	private Long added_by;
	
	@Enumerated(EnumType.STRING)
	private CommonStatus status;
	
	@ManyToMany(cascade={ CascadeType.MERGE })
    @JoinTable(
        name = "trip_document",
        joinColumns = { @JoinColumn(name = "trip_id") },
        inverseJoinColumns = { @JoinColumn(name = "doc_id") }
    )
	private List<Document> documents;
    

	public Trip(Long id){
		this.id = id;
	}

	public TripDto convertTripToDto() {
		TripDto trip = new TripDto();
			trip.setId(this.id);
			List<TripCategoryDto> tripCategories = new ArrayList<TripCategoryDto>();
			for(TripCategory c :tripCategoryList) {
				tripCategories.add(c.convertTripCategoryToDto());
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
			trip.setStatus(this.status);
			
			List<TripOptionDto> tripOptionDtoList = new ArrayList<TripOptionDto>(); 
			for(TripOption t : this.tripOptionList) {
				tripOptionDtoList.add(t.convertToTripOptiontDto());
			}
			trip.setTripOptionList(tripOptionDtoList);
			
			List<DocumentDto> documentDtoList = new ArrayList<DocumentDto>(); 
			for(Document d : this.documents) {
				documentDtoList.add(d.convertDocumentToDto());
			}
			trip.setDocumentList(documentDtoList);
		return trip;
	}
	
	
	
}
