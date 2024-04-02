package com.sep.tripmanagementservice.configuration.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sep.tripmanagementservice.configuration.entity.Approval;
import com.sep.tripmanagementservice.configuration.enums.ApprovalStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.repository.ApprovalRepository;
import com.sep.tripmanagementservice.configuration.service.ApprovalService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;

@Service
public class ApprovalServiceImpl implements ApprovalService {

	@Autowired
	private ApprovalRepository repository;

	@Value("${defaultPageSize}")
	private Integer defaultPageSize;

	private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalServiceImpl.class);

	@Override
	public Approval save(Approval approval, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] save: request={}", requestId,
				CommonUtils.convertToString(approval));

		Approval response = new Approval();

		approval.setCreatedDate(LocalDateTime.now());
		approval.setApprovalStatus(ApprovalStatus.PENDING);

		try {
			// Repository Call
			response = repository.save(approval);

			// TODO update user account active status as Pending
		} catch (Exception e) {

			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  save : exception={}", requestId, e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.APPROVAL_FAILED);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] save: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public List<Approval> getByApprovalStatus(ApprovalStatus approvalStatus, Integer pageNo, Integer pageSize,
			String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] getByApprovalStatus: request={}", requestId,
				CommonUtils.convertToString(approvalStatus));

		Pageable pageable = null;

		if (pageNo != null && pageSize != null) {
			pageable = PageRequest.of(pageNo - 1, pageSize);
		} else if (pageNo != null && pageSize == null) {
			pageable = PageRequest.of(pageNo - 1, defaultPageSize);
		}

		List<Approval> response = new ArrayList<>();
		try {
			response = repository.findByApprovalStatus(approvalStatus, pageable);
		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  getByApprovalStatus : exception={}", requestId,
					e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.APPROVAL_FAILED);
		}

		if (response.isEmpty()) {
			throw new TSMSException(TSMSError.APPROVAL_REQUEST_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] getByApprovalStatus: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public List<Approval> getAll(String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] getAll: request={}", requestId);

		List<Approval> response = new ArrayList<>();
		try {

			response = repository.findAll();

		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  getAll : exception={}", requestId, e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.APPROVAL_FAILED);
		}

		if (response.isEmpty()) {
			throw new TSMSException(TSMSError.APPROVAL_REQUEST_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] getAll: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public Approval update(Approval approval, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] update: request={}", requestId,
				CommonUtils.convertToString(approval));

		Approval response = new Approval();

		Approval existingApprovalRequest = repository.findByIdAndEmail(approval.getId(), approval.getEmail());

		if (existingApprovalRequest != null) {

			approval.setCreatedBy(existingApprovalRequest.getCreatedBy());
			approval.setCreatedDate(existingApprovalRequest.getCreatedDate());
			approval.setUpdatedDate(LocalDateTime.now());

		} else {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}] update : Approval Request Not Found", requestId);
			throw new TSMSException(TSMSError.APPROVAL_REQUEST_NOT_FOUND);
		}

		try {

			// Repository Call
			response = repository.save(approval);
		} catch (Exception e) {

			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  update : exception={}", requestId, e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.APPROVAL_FAILED);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] update: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public Approval getByUserIdAndEmail(Long id, String email, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] getByIdAndEmail: request={}", requestId);

		Approval response = new Approval();
		try {

			response = repository.findByIdAndEmail(id, email);

		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  getByIdAndEmail : exception={}", requestId,
					e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.APPROVAL_FAILED);
		}

		if (response == null) {
			throw new TSMSException(TSMSError.APPROVAL_REQUEST_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] getByIdAndEmail: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

}
