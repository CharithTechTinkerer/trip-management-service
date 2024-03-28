package com.sep.tripmanagementservice.configuration.service;

import com.sep.tripmanagementservice.configuration.entity.User;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;

public interface UserService {

	public User update(User user, String profilePictureName, String profilePictureContent, String requestId)
			throws TSMSException;

}
