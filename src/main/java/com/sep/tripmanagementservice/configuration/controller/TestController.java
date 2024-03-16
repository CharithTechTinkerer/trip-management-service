package com.sep.tripmanagementservice.configuration.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sep.tripmanagementservice.configuration.codes.ResponseCodes;
import com.sep.tripmanagementservice.configuration.controller.entity.Test;
import com.sep.tripmanagementservice.configuration.dto.response.ResponseDto;
import com.sep.tripmanagementservice.configuration.dto.response.TSMSResponse;
import com.sep.tripmanagementservice.configuration.dto.user.testDto;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.service.TestService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/private/test")
public class TestController {

	@Autowired
	private TestService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

	@PostMapping
	public ResponseEntity<TSMSResponse> saveTest(@RequestParam("requestId") String requestId,
			@RequestBody testDto userDto) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] saveTest: request={}", requestId,
				CommonUtils.convertToString(userDto));

		TSMSResponse response = new TSMSResponse();

		if (!CommonUtils.checkMandtoryFieldsNullOrEmpty(userDto)) {
			throw new TSMSException(TSMSError.MANDOTORY_FIELDS_EMPTY);
		}

		// Service Call Test.
		testDto dto = convertEntityToDto(service.save(convertDtoToEntity(userDto), requestId));
		response.setRequestId(requestId);
		response.setData(dto);
		response.setMessage("Test Saved Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] saveTest: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-test")
	public ResponseEntity<String> getUser() {

		return ResponseEntity.ok("Deployment Success Trip Management API Gateway!");
	}

	private testDto convertEntityToDto(Test user) {
		testDto testDto = new testDto();
		testDto.setId(user.getId());
		testDto.setName(user.getName());
		testDto.setAddress(user.getAddress());

		return testDto;
	}

	private Test convertDtoToEntity(testDto userDto) {
		Test test = new Test();
		test.setId(userDto.getId());
		test.setName(userDto.getName());
		test.setAddress(userDto.getAddress());

		return test;
	}

}
