package com.sep.tripmanagementservice.configuration.service;

import com.sep.tripmanagementservice.configuration.entity.test.Test;

public interface TestService {

	public Test save(Test test, String requestId);

}
