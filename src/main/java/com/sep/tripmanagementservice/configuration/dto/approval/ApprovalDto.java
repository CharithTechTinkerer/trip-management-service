package com.sep.tripmanagementservice.configuration.dto.approval;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalDto {

	private Long id;

	private String email;

	private String content;

	private String approvalStatus;

	private String reason;

	private String createdBy;

	private String approvedBy;

	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

}
