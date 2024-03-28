package com.sep.tripmanagementservice.configuration.service;

import com.sep.tripmanagementservice.configuration.exception.TSMSException;

public interface S3BucketService {

	public String uploadFile(String base64Data, String imageName, String userName, String requestId)
			throws TSMSException;

	public String deleteFile(String url, String requestId) throws TSMSException;

}
