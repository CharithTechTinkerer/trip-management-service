package com.sep.tripmanagementservice.configuration.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sep.tripmanagementservice.configuration.dto.SiteContentDto;
import com.sep.tripmanagementservice.configuration.dto.UserDto;
import com.sep.tripmanagementservice.configuration.entity.SiteContent;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.repository.SiteContentRepository;
import com.sep.tripmanagementservice.configuration.service.S3BucketService;
import com.sep.tripmanagementservice.configuration.service.SiteContentService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;

@Service
public class SiteContentServiceImpl implements SiteContentService {
	@Autowired
	private S3BucketService s3BucketService;
	@Value("${aws.media.bucketName}")
	private String mediaBucketName;
	@Autowired
	private SiteContentRepository siteContentRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SiteContentServiceImpl.class);

	@Override
	@Transactional
	public void createSiteContent(SiteContentDto siteContentDto, UserDto user, String requestId) throws TSMSException{
		String s3URL;
		try {
			MultipartFile file = siteContentDto.getDocuments()[0];
			s3URL = s3BucketService.uploadFile(CommonUtils.convertMultipartFileToBase64(file),file.getOriginalFilename(), user.getUserName(), mediaBucketName, requestId);
		} catch (TSMSException e) {
			LOGGER.error("(createNewTrip) requestId={} | TSMSError={}", requestId, TSMSError.DOCUMENT_UPLOAD_FAILD);
			e.printStackTrace();
			throw new TSMSException(TSMSError.DOCUMENT_UPLOAD_FAILD);
		} catch (IOException e) {
			LOGGER.error("(createNewTrip) requestId={} | TSMSError={}", requestId, TSMSError.DOCUMENT_CONVERSION_FAILED);
			e.printStackTrace();
			throw new TSMSException(TSMSError.DOCUMENT_CONVERSION_FAILED);
		}
		siteContentDto.setStatus(CommonStatus.ACTIVE);
		SiteContent content = siteContentDto.convertDtoToSiteContent(LocalDateTime.now(), user.getId());
		content.setMediaURL(s3URL);
		siteContentRepository.save(content);
		return;
	}
	
	@Override
	public void updateSiteContent(SiteContentDto siteContentDto, Long user, String requestId) throws TSMSException{
		SiteContent existing;
		if((existing = siteContentRepository.findByIdAndUser(siteContentDto.getId(),user))==null) {
			LOGGER.error("(updateSiteContent) requestId={} | TSMSError={}", requestId, TSMSError.NON_EXISTING_SITE_CONTENT_ID_FOR_USER);
			throw new TSMSException(TSMSError.NON_EXISTING_SITE_CONTENT_ID_FOR_USER);
		}
		SiteContent content = siteContentDto.convertDtoToSiteContent(LocalDateTime.now(), user);
			content.setMediaURL(existing.getMediaURL());
		siteContentRepository.save(content);
	}
	
	@Override
	public List<SiteContentDto> getSiteContentForNow(Long userId, CommonStatus status){
		List<SiteContent> content = siteContentRepository.findByUserIdAndStatus(userId, status);
		List<SiteContentDto> siteContentDtos = new ArrayList<SiteContentDto>();
		for(SiteContent c: content) {
			siteContentDtos.add(c.convertToSiteContentDto());
		}
		return siteContentDtos;
	}
	
	@Override
	public void updateSiteContentMedia(SiteContentDto siteContentDto, UserDto user, String requestId) throws TSMSException{
		if(siteContentRepository.findCountByIdForUser(siteContentDto.getId(),user.getId())==0) {
			LOGGER.error("(updateSiteContent) requestId={} | TSMSError={}", requestId, TSMSError.NON_EXISTING_SITE_CONTENT_ID_FOR_USER);
			throw new TSMSException(TSMSError.NON_EXISTING_SITE_CONTENT_ID_FOR_USER);
		}
		String s3URL;
		try {
			MultipartFile file = siteContentDto.getDocuments()[0];
			s3URL = s3BucketService.uploadFile(CommonUtils.convertMultipartFileToBase64(file),file.getOriginalFilename(), user.getUserName(), mediaBucketName, requestId);
		} catch (TSMSException e) {
			LOGGER.error("(createNewTrip) requestId={} | TSMSError={}", requestId, TSMSError.DOCUMENT_UPLOAD_FAILD);
			e.printStackTrace();
			throw new TSMSException(TSMSError.DOCUMENT_UPLOAD_FAILD);
		} catch (IOException e) {
			LOGGER.error("(createNewTrip) requestId={} | TSMSError={}", requestId, TSMSError.DOCUMENT_CONVERSION_FAILED);
			e.printStackTrace();
			throw new TSMSException(TSMSError.DOCUMENT_CONVERSION_FAILED);
		}
		siteContentRepository.updateMediaById(siteContentDto.getId(),s3URL);
	}
	
	@Override
	public void deleteSiteContent(Long siteContentId, Long user, String requestId) throws TSMSException{
		if(siteContentRepository.findCountByIdForUser(siteContentId,user)==0) {
			LOGGER.error("(updateSiteContent) requestId={} | TSMSError={}", requestId, TSMSError.NON_EXISTING_SITE_CONTENT_ID_FOR_USER);
			throw new TSMSException(TSMSError.NON_EXISTING_SITE_CONTENT_ID_FOR_USER);
		}
		siteContentRepository.updateStatusById(CommonStatus.DELETED, siteContentId);
	}
	
	
}
