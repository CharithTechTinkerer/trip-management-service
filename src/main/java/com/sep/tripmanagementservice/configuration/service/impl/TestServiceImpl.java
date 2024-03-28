package com.sep.tripmanagementservice.configuration.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sep.tripmanagementservice.configuration.controller.entity.systemadmin.Test;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.repository.TestRepository;
import com.sep.tripmanagementservice.configuration.service.TestService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;

@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private TestRepository repository;

	private static final Logger LOGGER = LoggerFactory.getLogger(TestServiceImpl.class);

	@Override
	public Test save(Test test, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] save: request={}", requestId,
				CommonUtils.convertToString(test));

		Test testResponse = new Test();

		try {
			// Repository Call
			testResponse = repository.save(test);
		} catch (Exception e) {
			throw new TSMSException(TSMSError.REGISTRATION_FAILED);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] save: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(testResponse));
		return testResponse;

	}

}
