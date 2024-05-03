package com.sep.tripmanagementservice.configuration.dto;

import com.sep.tripmanagementservice.configuration.enums.CommonStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DocumentDto {
	private Long id;
	private String originalName;
	private String uniqueName;
	private CommonStatus status;	
	

}




