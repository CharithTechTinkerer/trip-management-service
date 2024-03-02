package com.sep.tripmanagementservice.configuration.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sep.tripmanagementservice.configuration.entity.systemadmin.Test;
import com.sep.tripmanagementservice.configuration.repository.TestRepository;
import com.sep.tripmanagementservice.configuration.service.TestService;

@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private TestRepository repository;

	@Override
	public Test save(Test test, String requestId) {

		Test testResponse = new Test();

		try {
			// Repository Call
			testResponse = repository.save(test);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return testResponse;

	}

}
