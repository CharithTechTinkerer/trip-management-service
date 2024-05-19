package com.sep.tripmanagementservice.configuration.service;

import java.util.List;

import com.sep.tripmanagementservice.configuration.dto.SiteContentDto;
import com.sep.tripmanagementservice.configuration.dto.UserDto;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;

public interface SiteContentService {
	public void createSiteContent(SiteContentDto siteContentDto, UserDto user, String requestId) throws TSMSException;
	public void updateSiteContent(SiteContentDto siteContentDto, Long user, String requestId) throws TSMSException;
	public List<SiteContentDto> getSiteContentForNow(Long userId, CommonStatus status);
	public void updateSiteContentMedia(SiteContentDto siteContentDto, UserDto user, String requestId) throws TSMSException;
	public void deleteSiteContent(Long siteContentId, Long user, String requestId) throws TSMSException;


}
