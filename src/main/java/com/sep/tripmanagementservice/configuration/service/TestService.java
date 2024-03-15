package com.sep.tripmanagementservice.configuration.service;

import com.sep.tripmanagementservice.configuration.controller.entity.Test;

public interface TestService {

	public Test save(Test test, String requestId);

}
