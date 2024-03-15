package com.sep.tripmanagementservice.configuration.controller;

import java.time.LocalDateTime;

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
import com.sep.tripmanagementservice.configuration.dto.user.testDto;
import com.sep.tripmanagementservice.configuration.service.TestService;

@CrossOrigin("*")
@RestController
@RequestMapping("/v1/private/test")
public class TestController {

	@Autowired
	private TestService service;

	@PostMapping
	public ResponseEntity<ResponseDto<testDto>> saveUser(@RequestParam("requestId") String requestId,
			@RequestBody testDto userDto) {

		ResponseDto<testDto> response = new ResponseDto<>();
		response.setRequestId(requestId);

		// Service Call Test.
		testDto dto = convertEntityToDto(service.save(convertDtoToEntity(userDto), requestId));
		response.setData(dto);
		response.setMessage("Test Saved Successfully");
		response.setStatusCode(ResponseCodes.OK.code());
		response.setTimestamp(LocalDateTime.now().toString());

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
