package com.sep.tripmanagementservice.configuration.service;

import com.sep.tripmanagementservice.configuration.entity.Test;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;

public interface TestService {

	public Test save(Test test, String requestId) throws TSMSException;

}
