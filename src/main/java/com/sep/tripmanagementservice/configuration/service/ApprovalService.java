package com.sep.tripmanagementservice.configuration.service;

import java.util.List;

import com.sep.tripmanagementservice.configuration.entity.Approval;
import com.sep.tripmanagementservice.configuration.enums.ApprovalStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;

public interface ApprovalService {

	Approval save(Approval approval, String requestId) throws TSMSException;

	List<Approval> getByApprovalStatus(ApprovalStatus approvalStatus, Integer pageNo, Integer pageSize,
			String requestId) throws TSMSException;

	List<Approval> getAll(String requestId) throws TSMSException;

	Approval update(Approval approval, String requestId) throws TSMSException;
}
