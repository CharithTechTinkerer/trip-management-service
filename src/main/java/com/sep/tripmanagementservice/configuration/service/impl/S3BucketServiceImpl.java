package com.sep.tripmanagementservice.configuration.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.service.S3BucketService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;

@Service
public class S3BucketServiceImpl implements S3BucketService {

	@Value("${aws.region}")
	private String region;

	@Autowired
	private AmazonS3 s3Client;

	private static final Logger LOGGER = LoggerFactory.getLogger(S3BucketServiceImpl.class);

	@Override
	public String uploadFile(String base64Data, String imageName, String userName, String bucketName, String requestId)
			throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] uploadFile: base64Data={}|imageName={}|userName={}",
				requestId, base64Data, imageName, userName);

		String fileName = userName.concat("_").concat(imageName);

		byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

		File file = new File(fileName);

		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(decodedBytes);
			fos.flush();

			s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
			file.delete();

			String imageUrl = "https://".concat(bucketName).concat(".s3.").concat(region).concat(".amazonaws.com/")
					.concat(fileName);

			LOGGER.info("END [SERVICE-LAYER] [RequestId={}] uploadFile: timeTaken={}|response={}", requestId,
					CommonUtils.getExecutionTime(startTime), imageUrl);
			return imageUrl;

		} catch (IOException e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  uploadFile : exception={}", requestId, e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.PROFILE_PICTURE_UPLOAD_FAILED);
		}
	}

	@Override
	public String deleteFile(String url, String bucketName, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] deleteFile: url={}", requestId, url);

		int lastIndex = url.lastIndexOf('/');
		// Extract the substring after the last '/'
		String fileName = url.substring(lastIndex + 1);

		try {
			s3Client.deleteObject(bucketName, fileName);
		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  deleteFile : exception={}", requestId, e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.PROFILE_PICTURE_DELETE_FAILED);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] deleteFile: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), fileName);
		return fileName + " removed ...";
	}

}
