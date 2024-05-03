package com.sep.tripmanagementservice.configuration.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.enums.TripCategoryStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Trip_Categories")
public class TripCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "code", nullable = false, unique = true)
	private String code;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private TripCategoryStatus status;

	@Column(name = "created_by", nullable = false)
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "created_date", nullable = false)
	private LocalDateTime createdDate;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;
	
	
	public TripCategoryDto convertTripCategoryToDto() {
    	TripCategoryDto res = new TripCategoryDto();
			res.setId(this.id);
			res.setName(this.name);
			res.setCode(this.code);
			res.setDescription(this.description);
			res.setStatus(this.status);
		return res;
	}


}
