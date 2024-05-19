package com.sep.tripmanagementservice.configuration.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.tripmanagementservice.configuration.entity.SiteContent;
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
public class SiteContentDto {
	private Long id;
	private String name;
	private String description;
	private String searchEngineText;
	private LocalDateTime displayFromDate;
	private LocalDateTime displayToDate;
	private Integer noDisplayPDay;
	private String mediaURL;
	private CommonStatus status;
	private MultipartFile[] documents;


	public SiteContent convertDtoToSiteContent(LocalDateTime time, Long user) {
		SiteContent res = new SiteContent();
			res.setId(this.id);
			res.setName(this.name);
			res.setDescription(this.description);
			res.setSearchEngineText(this.searchEngineText);
			res.setDisplayFromDate(this.displayFromDate);
			res.setDisplayToDate(this.displayToDate);
			res.setNoDisplayPDay(this.noDisplayPDay);
			res.setMediaURL(this.mediaURL);
			res.setStatus(this.status);
			res.setAdded_at(time);
			res.setAdded_by(user);
		return res;
	}
	
	
}
