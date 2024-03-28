package com.sep.tripmanagementservice.configuration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.Getter;

@Configuration
@Getter
public class AWSS3Config {

	@Value("${aws.accessKey}")
	private String accessKey;

	@Value("${aws.secretAccessKey}")
	private String secretAccessKey;

	@Value("${aws.region}")
	private String region;

	@Bean
	public AmazonS3 generateS3Client() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretAccessKey);

		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.withRegion(region).build();
	}
}
