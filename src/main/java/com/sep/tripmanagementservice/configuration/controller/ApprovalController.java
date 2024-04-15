package com.sep.tripmanagementservice.configuration.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sep.tripmanagementservice.configuration.dto.ApprovalDto;
import com.sep.tripmanagementservice.configuration.dto.response.TSMSResponse;
import com.sep.tripmanagementservice.configuration.entity.Approval;
import com.sep.tripmanagementservice.configuration.enums.ApprovalStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.service.ApprovalService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;

@RestController
@RequestMapping("/api/v1/private/approval")
public class ApprovalController {

	@Autowired
	private ApprovalService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalController.class);

	@PostMapping("/save")
	public ResponseEntity<TSMSResponse> save(@RequestParam(name = "requestId", required = true) String requestId,
			@RequestBody(required = true) ApprovalDto approvalDto) throws TSMSException {

		TSMSResponse response = new TSMSResponse();
		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] save: request={}", requestId,
				CommonUtils.convertToString(approvalDto));

		if (!CommonUtils.checkApprovalMandtoryFieldsNullOrEmpty(approvalDto)) {
			LOGGER.error(
					"ERROR [REST-LAYER] [RequestId={}] save : Mandatory fields are null. Please ensure all required fields are provided",
					requestId);
			throw new TSMSException(TSMSError.MANDOTORY_FIELDS_EMPTY);
		}

		// Service Call.
		ApprovalDto dto = convertEntityToDto(service.save(convertDtoToEntity(approvalDto), requestId));
		response.setRequestId(requestId);
		response.setSuccess(true);
		response.setData(dto);
		response.setMessage("Approval Request Saved Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] save: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-by-status/{status}")
	public ResponseEntity<TSMSResponse> getApprovalRequests(
			@PathVariable(name = "status", required = true) String status,
			@RequestParam(name = "requestId", required = true) String requestId,
			@RequestParam(name = "pageNo", required = true) Integer pageNo,
			@RequestParam(name = "pageSize", required = false) Integer pageSize) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] getApprovalRequests: request={}", requestId, status);

		if (!CommonUtils.isValidateApprovalStatus(status)) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] getApprovalRequests : Invalid Approval Status", requestId);
			throw new TSMSException(TSMSError.INVALID_APPROVAL_STATUS);
		}

		TSMSResponse response = new TSMSResponse();

		// Service Call.
		ApprovalStatus approvalStatus = null;
		if (status.equals(ApprovalStatus.APPROVED.name())) {
			approvalStatus = ApprovalStatus.APPROVED;
		} else if (status.equals(ApprovalStatus.PENDING.name())) {
			approvalStatus = ApprovalStatus.PENDING;
		} else if (status.equals(ApprovalStatus.REJECTED.name())) {
			approvalStatus = ApprovalStatus.REJECTED;
		}

		List<Approval> approvals = service.getByApprovalStatus(approvalStatus, pageNo, pageSize, requestId);

		List<ApprovalDto> approvalDto = new ArrayList<>();

		for (Approval approval : approvals) {
			approvalDto.add(convertEntityToDto(approval));
		}

		response.setRequestId(requestId);
		response.setSuccess(true);
		response.setData(approvalDto);
		response.setMessage("Approval Requests Retreived Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] getApprovalRequests: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-by-id-email/{id}/{email}")
	public ResponseEntity<TSMSResponse> getApprovalRequestByIdAndEmail(
			@PathVariable(name = "id", required = true) Long id,
			@PathVariable(name = "email", required = true) String email,
			@RequestParam(name = "requestId", required = true) String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] getApprovalRequestByIdAndEmail: id={}|email={}", requestId, id,
				email);

		TSMSResponse response = new TSMSResponse();

		// Service Call.
		ApprovalDto approvalDto = convertEntityToDto(service.getByUserIdAndEmail(id, email, requestId));

		response.setRequestId(requestId);
		response.setSuccess(true);
		response.setData(approvalDto);
		response.setMessage("Approval Request Retreived Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] getApprovalRequestByIdAndEmail: timeTaken={}|response={}",
				requestId, CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-all")
	public ResponseEntity<TSMSResponse> getAllApprovalRequests(
			@RequestParam(name = "requestId", required = true) String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] getAllApprovalRequests: request={}", requestId);

		TSMSResponse response = new TSMSResponse();

		// Service Call.
		List<Approval> approvals = service.getAll(requestId);

		List<ApprovalDto> approvalDto = new ArrayList<>();

		for (Approval approval : approvals) {
			approvalDto.add(convertEntityToDto(approval));
		}

		response.setRequestId(requestId);
		response.setSuccess(true);
		response.setData(approvalDto);
		response.setMessage("Approval Requests Retreived Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] getAllApprovalRequests: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return ResponseEntity.ok(response);
	}

	@PutMapping("/update-status/{id}/{email}/{status}")
	public ResponseEntity<TSMSResponse> updateApprovalStatus(@PathVariable(name = "id", required = true) Long id,
			@PathVariable(name = "email", required = true) String email,
			@PathVariable(name = "status", required = true) String status,
			@RequestParam(name = "requestId", required = true) String requestId,
			@RequestBody(required = true) ApprovalDto approvalDto) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] updateApprovalStatus: request={}", requestId, status);

		if (status == null || status.equals("") || status.isBlank() || status.isEmpty()) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] updateApprovalStatus : Approval Status Field is Empty",
					requestId);
			throw new TSMSException(TSMSError.APPROVAL_STATUS_FIELD_EMPTY);
		} else if (!CommonUtils.isValidateApprovalStatus(status)) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] updateApprovalStatus : Invalid Approval Status", requestId);
			throw new TSMSException(TSMSError.INVALID_APPROVAL_STATUS);
		}

		if (id == null) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] updateApprovalStatus : Id Field is Empty", requestId);
			throw new TSMSException(TSMSError.ID_FIELD_EMPTY);
		} else if (email.isEmpty() || email.isBlank() || email.equals("")) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] updateApprovalStatus : Email Field is Empty", requestId);
			throw new TSMSException(TSMSError.EMAIL_FIELD_EMPTY);
		}

		TSMSResponse response = new TSMSResponse();

		// Service Call.
		approvalDto.setId(id);
		approvalDto.setEmail(email);
		approvalDto.setApprovalStatus(status);

		ApprovalDto dto = convertEntityToDto(service.update(convertDtoToEntity(approvalDto), requestId));
		response.setRequestId(requestId);
		response.setSuccess(true);
		response.setData(dto);
		response.setMessage("Approval Request Updated Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] updateApprovalStatus: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return ResponseEntity.ok(response);
	}

	private Approval convertDtoToEntity(ApprovalDto approvalDto) {
		Approval apprroval = new Approval();

		if (approvalDto.getApprovalStatus() != null) {
			ApprovalStatus approvalStatus = null;
			if (approvalDto.getApprovalStatus().equals(ApprovalStatus.APPROVED.name())) {
				approvalStatus = ApprovalStatus.APPROVED;
			} else if (approvalDto.getApprovalStatus().equals(ApprovalStatus.PENDING.name())) {
				approvalStatus = ApprovalStatus.PENDING;
			} else if (approvalDto.getApprovalStatus().equals(ApprovalStatus.REJECTED.name())) {
				approvalStatus = ApprovalStatus.REJECTED;
			}
			apprroval.setApprovalStatus(approvalStatus);
		}

		apprroval.setId(approvalDto.getId());
		apprroval.setApprovedBy(approvalDto.getApprovedBy());
		apprroval.setContent(approvalDto.getContent());
		apprroval.setEmail(approvalDto.getEmail());
		apprroval.setCreatedBy(approvalDto.getCreatedBy());
		apprroval.setCreatedDate(approvalDto.getCreatedDate());
		apprroval.setReason(approvalDto.getReason());
		apprroval.setUpdatedDate(approvalDto.getUpdatedDate());

		return apprroval;
	}

	private ApprovalDto convertEntityToDto(Approval approval) {
		ApprovalDto approvalDto = new ApprovalDto();
		approvalDto.setId(approval.getId());
		approvalDto.setApprovedBy(approval.getApprovedBy());
		approvalDto.setContent(approval.getContent());
		approvalDto.setEmail(approval.getEmail());
		approvalDto.setCreatedBy(approval.getCreatedBy());
		approvalDto.setCreatedDate(approval.getCreatedDate());
		approvalDto.setReason(approval.getReason());
		approvalDto.setUpdatedDate(approval.getUpdatedDate());
		approvalDto.setApprovalStatus(approval.getApprovalStatus().name());

		return approvalDto;
	}

}
