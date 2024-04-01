package com.sep.tripmanagementservice.configuration.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sep.tripmanagementservice.configuration.dto.UserDto;
import com.sep.tripmanagementservice.configuration.dto.response.TSMSResponse;
import com.sep.tripmanagementservice.configuration.entity.User;
import com.sep.tripmanagementservice.configuration.enums.Gender;
import com.sep.tripmanagementservice.configuration.enums.Roles;
import com.sep.tripmanagementservice.configuration.enums.Salutation;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.service.UserService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;

@RestController
@RequestMapping("/api/v1/private/users")
public class UserController {

	@Autowired
	private UserService service;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@PutMapping("/update")
	public ResponseEntity<TSMSResponse> update(@RequestParam("requestId") String requestId,
			@RequestBody UserDto userDto) throws TSMSException {

		TSMSResponse response = new TSMSResponse();
		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] update: request={}", requestId,
				CommonUtils.convertToString(userDto));

		if (userDto.getEmail() == null || userDto.getEmail().equals("") || userDto.getEmail().isEmpty()) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] update : Email Filed is Mandatory", requestId);
			throw new TSMSException(TSMSError.EMAIL_FIELD_EMPTY);
		}
		if (userDto.getUserName() != null) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] update : You are not allowed to update the username",
					requestId);
			throw new TSMSException(TSMSError.USERNAME_UPDATE_NOT_ALLOWED);
		}

		if (!CommonUtils.containsOnlyLetters(userDto.getFirstName())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] update : Invalid First Name", requestId);
			throw new TSMSException(TSMSError.INVALID_FIRST_NAME);
		}

		if (!CommonUtils.containsOnlyLetters(userDto.getLastName())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] update : Invalid Last Name", requestId);
			throw new TSMSException(TSMSError.INVALID_LAST_NAME);
		}

		if (!CommonUtils.NICValidation(userDto.getNic())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] update : Invalid NIC Number", requestId);
			throw new TSMSException(TSMSError.INVALID_NIC);
		}

		if (!CommonUtils.isValidGender(userDto.getGender())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] update : Invalid Gender", requestId);
			throw new TSMSException(TSMSError.INVALID_GENDER);
		}

		if (!CommonUtils.validatePhoneNumber(userDto.getContactNo())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] update : Invalid Contact Number", requestId);
			throw new TSMSException(TSMSError.INVALID_CONTACT_NO);
		}

		if (!CommonUtils.isValidRole(userDto.getRole())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] update : Invalid Role", requestId);
			throw new TSMSException(TSMSError.INVALID_ROLE);
		}

		if (!CommonUtils.isValidateSalutation(userDto.getSalutation())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] update : Invalid Salutation", requestId);
			throw new TSMSException(TSMSError.INVALID_SALUTATION);
		}

		if (!CommonUtils.isValidDOB(userDto.getDateOfBirth())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] update : Invalid Date of Birth", requestId);
			throw new TSMSException(TSMSError.INVALID_DOB);
		}

		if (!CommonUtils.isValidPassword(userDto.getPassword())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] update : Invalid Password", requestId);
			throw new TSMSException(TSMSError.INVALID_PASSWORD);
		}

		if (userDto.getRole() != null && userDto.getRole().equals(Roles.SA.name())) {
			LOGGER.error(
					"ERROR [REST-LAYER] [RequestId={}] update : Cannot update user role to ".concat(Roles.SA.getRole()),
					requestId);
			throw new TSMSException(TSMSError.INVALID_PASSWORD);
		}

		// Service Call.
		UserDto dto = convertEntityToDto(service.update(convertDtoToEntity(userDto), userDto.getProfilePictureName(),
				userDto.getProfilePictureContent(), requestId));

		response.setSuccess(true);
		response.setRequestId(requestId);
		response.setData(dto);
		response.setMessage("User Updated Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] update: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return ResponseEntity.ok(response);
	}

	private User convertDtoToEntity(UserDto userDto) {
		User user = new User();

		if (userDto.getRole() != null) {

			String role = userDto.getRole();

			if (role.equals(Roles.SA.name())) {
				user.setRole(Roles.SA);
			} else if (role.equals(Roles.TO.name())) {
				user.setRole(Roles.TO);
			} else if (role.equals(Roles.TP.name())) {
				user.setRole(Roles.TP);
			}
		}

		if (userDto.getSalutation() != null) {

			String salutation = userDto.getSalutation();

			if (salutation.equals(Salutation.DR.name())) {
				user.setSalutation(Salutation.DR);
			} else if (salutation.equals(Salutation.HON.name())) {
				user.setSalutation(Salutation.HON);
			} else if (salutation.equals(Salutation.MISS.name())) {
				user.setSalutation(Salutation.MISS);
			} else if (salutation.equals(Salutation.MR.name())) {
				user.setSalutation(Salutation.MR);
			} else if (salutation.equals(Salutation.MRS.name())) {
				user.setSalutation(Salutation.MRS);
			} else if (salutation.equals(Salutation.MS.name())) {
				user.setSalutation(Salutation.MS);
			} else if (salutation.equals(Salutation.REV.name())) {
				user.setSalutation(Salutation.REV);
			}

		}

		if (userDto.getGender() != null) {

			String gender = userDto.getGender();

			if (gender.equals(Gender.M.name())) {
				user.setGender(Gender.M);
			} else if (gender.equals(Gender.F.name())) {
				user.setGender(Gender.F);
			} else if (gender.equals(Gender.O.name())) {
				user.setGender(Gender.O);
			}
		}

		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setNic(userDto.getNic());

		user.setDateOfBirth(userDto.getDateOfBirth());
		user.setContactNo(userDto.getContactNo());
		user.setAddressLine1(userDto.getAddressLine1());
		user.setAddressLine2(userDto.getAddressLine2());
		user.setAddressLine3(userDto.getAddressLine3());

		if (userDto.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		}

		return user;
	}

	private UserDto convertEntityToDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setNic(user.getNic());
		userDto.setGender(user.getGender().name());
		userDto.setSalutation(user.getSalutation().name());
		userDto.setDateOfBirth(user.getDateOfBirth());
		userDto.setContactNo(user.getContactNo());
		userDto.setAddressLine1(user.getAddressLine1());
		userDto.setAddressLine2(user.getAddressLine2());
		userDto.setAddressLine3(user.getAddressLine3());
		userDto.setPassword(passwordEncoder.encode(user.getPassword()));
		userDto.setRole(user.getRole().name());
		userDto.setProfilePictureUrl(user.getProfilePictureUrl());

		return userDto;
	}
}
