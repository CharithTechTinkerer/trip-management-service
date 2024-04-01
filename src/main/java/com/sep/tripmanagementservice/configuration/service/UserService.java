package com.sep.tripmanagementservice.configuration.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.sep.tripmanagementservice.configuration.entity.User;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;

public interface UserService {

	public User update(User user, String profilePictureName, String profilePictureContent, String requestId)
			throws TSMSException;

	public User getByEmail(String email, String requestId) throws TSMSException;

	public Page<User> getAllWithPagination(Integer pageNo, Integer pageSize, String requestId) throws TSMSException;

	public List<User> getAll(String requestId) throws TSMSException;

}
