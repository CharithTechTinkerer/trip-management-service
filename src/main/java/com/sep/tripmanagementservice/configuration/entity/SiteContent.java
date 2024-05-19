package com.sep.tripmanagementservice.configuration.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sep.tripmanagementservice.configuration.dto.SiteContentDto;
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
@Table(name = "site_content")
public class SiteContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private String searchEngineText;
	private LocalDateTime displayFromDate;
	private LocalDateTime displayToDate;
	private Integer noDisplayPDay;
	private String mediaURL;
	private LocalDateTime added_at;
	private Long added_by;
	
	@Enumerated(EnumType.STRING)
	private CommonStatus status;
	

	public SiteContentDto convertToSiteContentDto() {
		SiteContentDto res = new SiteContentDto();
			res.setId(this.id);
			res.setName(this.name);
			res.setDescription(this.description);
			res.setSearchEngineText(this.searchEngineText);
			res.setDisplayFromDate(this.displayFromDate);
			res.setDisplayToDate(this.displayToDate);
			res.setNoDisplayPDay(this.noDisplayPDay);
			res.setMediaURL(this.mediaURL);
			res.setStatus(this.status);
		return res;
	}
	
	
}
