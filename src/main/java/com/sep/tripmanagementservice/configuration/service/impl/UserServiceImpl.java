package com.sep.tripmanagementservice.configuration.service.impl;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sep.tripmanagementservice.configuration.entity.Approval;
import com.sep.tripmanagementservice.configuration.entity.User;
import com.sep.tripmanagementservice.configuration.enums.Roles;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.repository.UserRepository;
import com.sep.tripmanagementservice.configuration.service.ApprovalService;
import com.sep.tripmanagementservice.configuration.service.S3BucketService;
import com.sep.tripmanagementservice.configuration.service.UserService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private S3BucketService s3Service;

	@Autowired
	private ApprovalService approvalService;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User update(User user, String profilePictureName, String profilePictureContent, String requestId)
			throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info(
				"START [SERVICE-LAYER] [RequestId={}] update: request={}|profilePictureName={}|profilePictureContent={}",
				requestId, CommonUtils.convertToString(user), profilePictureName, profilePictureContent);

		User response = new User();

		// Find user by email.
		User existingUser = repository.findByEmail(user.getEmail());
		String proPicUrl = null;
		try {
			if (profilePictureName != null && profilePictureContent != null) {
				// Delete existing profile picture from s3 bucket.
				if (existingUser.getProfilePictureUrl() != null) {
					s3Service.deleteFile(existingUser.getProfilePictureUrl(), requestId);
				}

				proPicUrl = s3Service.uploadFile(profilePictureContent, profilePictureName, existingUser.getUserName(),
						requestId);
				LOGGER.info("[SERVICE-LAYER] [RequestId={}] update: profilePictureUrl={}", requestId, proPicUrl);
			}

		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  update : exception={}", requestId, e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.PROFILE_PICTURE_UPLOAD_FAILED);
		}

		if (existingUser != null) {
			try {

				Class<?> userClass = User.class;
				Field[] fields = userClass.getDeclaredFields();

				for (Field field : fields) {
					try {
						field.setAccessible(true);
						Object newValue = field.get(user);
						if (newValue != null) {
							field.set(existingUser, newValue);
						}
					} catch (IllegalAccessException e) {
						LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  update : exception={}", requestId,
								e.getMessage());
						e.printStackTrace();
						throw new TSMSException(TSMSError.USER_UPDATE_FAILED);
					}
				}

				existingUser.setUpdatedDate(LocalDateTime.now());
				existingUser.setProfilePictureUrl(proPicUrl);

				if (user.getRole().equals(Roles.TO)) {
					Approval approval = new Approval();
					approval.setId(response.getId());
					approval.setContent(response.getFirstName().concat(" ").concat(response.getLastName())
							.concat(" wants to upgrate his/her account to Trip Organizer account"));
					approval.setCreatedBy(response.getFirstName().concat(" ").concat(response.getLastName()));
					approval.setEmail(response.getEmail());
					approval.setReason("Trip Organizer Account Updation Request");

					// Call Save Method in Approval Service.
					Approval approvalResponse = approvalService.save(approval, requestId);
					if (approvalResponse == null) {
						LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  update : error={}", requestId,
								TSMSError.APPROVAL_REQUEST_CREATION_FAILED.getMessage());
						throw new TSMSException(TSMSError.APPROVAL_REQUEST_CREATION_FAILED);
					}
				}

				// Update user.
				response = repository.save(existingUser);

			} catch (Exception e) {

				// Delete uploaded profile picture from S3.
				s3Service.deleteFile(proPicUrl, requestId);

				LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  update : exception={}", requestId, e.getMessage());
				e.printStackTrace();
				throw new TSMSException(TSMSError.USER_UPDATE_FAILED);
			}
		} else {

			s3Service.deleteFile(proPicUrl, requestId);
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  update : error={}", requestId,
					TSMSError.USER_NOT_FOUND.getMessage());
			throw new TSMSException(TSMSError.USER_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] update: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;

	}

}
