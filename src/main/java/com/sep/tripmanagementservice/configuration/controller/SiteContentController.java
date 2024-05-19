package com.sep.tripmanagementservice.configuration.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sep.tripmanagementservice.configuration.dto.SiteContentDto;
import com.sep.tripmanagementservice.configuration.dto.request.TSMSRequest;
import com.sep.tripmanagementservice.configuration.dto.response.TSMSResponse;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;
import com.sep.tripmanagementservice.configuration.exception.TSMSError;
import com.sep.tripmanagementservice.configuration.exception.TSMSException;
import com.sep.tripmanagementservice.configuration.service.SiteContentService;
import com.sep.tripmanagementservice.configuration.utill.CommonUtils;
import com.sep.tripmanagementservice.configuration.utill.REST_CONTROLLER_URL;

@RestController
@RequestMapping("/api/v1/private/site-content")
public class SiteContentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SiteContentController.class);
	
	@Autowired
	private SiteContentService siteContentService; 

	@RequestMapping(value = REST_CONTROLLER_URL.CREATE_SITE_CONTENT, method = RequestMethod.POST)
	public @ResponseBody TSMSResponse createSiteContent(@RequestPart(value="TSMSRequest", required=true) TSMSRequest tSMSRequest, @RequestPart(value="documents", required=true) MultipartFile[] documents, @RequestParam(value="requestId", required=true) String requestId) throws TSMSException{
		LOGGER.info("START (createSiteContent) requestId={} | request={}", requestId, CommonUtils.convertToString(tSMSRequest));
		if(tSMSRequest.getUserDto()==null || tSMSRequest.getUserDto().getId()==null) { 
			LOGGER.error("(createSiteContent) requestId={} | TSMSError={}", requestId, TSMSError.USER_ID_NOT_FOUND);
			throw new TSMSException(TSMSError.USER_ID_NOT_FOUND);
		}
		if(tSMSRequest.getUserDto().getUserName()==null || tSMSRequest.getUserDto().getUserName().trim().length()==0) { 
			LOGGER.error("(createSiteContent) requestId={} | TSMSError={}", requestId, TSMSError.USERNAME_NOT_FOUND);
			throw new TSMSException(TSMSError.USERNAME_NOT_FOUND);
		}
		boolean isValidationFail = false;
		if(tSMSRequest.getSiteContentDto().getName()==null || tSMSRequest.getSiteContentDto().getName().trim().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getDescription()==null || tSMSRequest.getSiteContentDto().getDescription().trim().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getSearchEngineText()==null || tSMSRequest.getSiteContentDto().getSearchEngineText().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getDisplayFromDate()==null) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getDisplayToDate()==null) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getDisplayToDate().isBefore(tSMSRequest.getSiteContentDto().getDisplayFromDate())) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getNoDisplayPDay()==null || tSMSRequest.getSiteContentDto().getNoDisplayPDay()<1) { isValidationFail= true; }
		if(isValidationFail) { 
			LOGGER.error("(createSiteContent) requestId={} | TSMSError={}", requestId, TSMSError.INVALID_REQUEST);
			throw new TSMSException(TSMSError.INVALID_REQUEST); 
		}
		if(documents.length==0) { 
			LOGGER.error("(createSiteContent) requestId={} | TSMSError={}", requestId, TSMSError.DOCUMENT_NOT_FOUND);
			throw new TSMSException(TSMSError.DOCUMENT_NOT_FOUND); 
		}if(documents.length>1) { 
			LOGGER.error("(createSiteContent) requestId={} | TSMSError={}", requestId, TSMSError.MORE_THAN_ONE_DOCUMENT_FOUND);
			throw new TSMSException(TSMSError.MORE_THAN_ONE_DOCUMENT_FOUND); 
		}
		
		tSMSRequest.getSiteContentDto().setDocuments(documents);
		siteContentService.createSiteContent(tSMSRequest.getSiteContentDto(), tSMSRequest.getUserDto(), requestId);
		TSMSResponse res = new TSMSResponse();
			res.setSuccess(true);
			res.setMessage("Success");
			res.setStatus(200);
			res.setTimestamp(LocalDateTime.now().toString());
			res.setRequestId(requestId);
		LOGGER.info("END (createSiteContent) requestId={}", requestId);
		return res;
	}
	
	@RequestMapping(value = REST_CONTROLLER_URL.UPDATE_SITE_CONTENT, method = RequestMethod.POST)
	public @ResponseBody TSMSResponse updateSiteContent(@RequestBody TSMSRequest tSMSRequest, @RequestParam(value="requestId", required=true) String requestId) throws TSMSException{
		LOGGER.info("START (updateSiteContent) requestId={} | request={}", requestId, CommonUtils.convertToString(tSMSRequest));
		if(tSMSRequest.getUserDto()==null || tSMSRequest.getUserDto().getId()==null) {
			LOGGER.error("(updateSiteContent) requestId={} | TSMSError={}", requestId, TSMSError.USER_ID_NOT_FOUND);
			throw new TSMSException(TSMSError.USER_ID_NOT_FOUND); 
		}
		boolean isValidationFail = false;
		if(tSMSRequest.getSiteContentDto().getId()==null || tSMSRequest.getSiteContentDto().getId()<0) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getName()==null || tSMSRequest.getSiteContentDto().getName().trim().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getDescription()==null || tSMSRequest.getSiteContentDto().getDescription().trim().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getSearchEngineText()==null || tSMSRequest.getSiteContentDto().getSearchEngineText().length()==0) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getDisplayFromDate()==null) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getDisplayToDate()==null) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getDisplayToDate().isBefore(tSMSRequest.getSiteContentDto().getDisplayFromDate())) { isValidationFail= true; }
		if(tSMSRequest.getSiteContentDto().getNoDisplayPDay()==null || tSMSRequest.getSiteContentDto().getNoDisplayPDay()<1) { isValidationFail= true; }
		if(isValidationFail) {
			LOGGER.error("(updateSiteContent) requestId={} | TSMSError={}", requestId, TSMSError.INVALID_REQUEST);
			throw new TSMSException(TSMSError.INVALID_REQUEST);
		}
		
		siteContentService.updateSiteContent(tSMSRequest.getSiteContentDto(), tSMSRequest.getUserDto().getId(), requestId);
		TSMSResponse res = new TSMSResponse();
			res.setSuccess(true);
			res.setMessage("Success");
			res.setStatus(200);
			res.setTimestamp(LocalDateTime.now().toString());
			res.setRequestId(requestId);
		LOGGER.info("END (updateSiteContent) requestId={}", requestId);
		return res;
	}
	
	
	@RequestMapping(value = REST_CONTROLLER_URL.GET_SITE_CONTENT, method = RequestMethod.GET)
	public @ResponseBody TSMSResponse getSiteContentForNow(@RequestParam Long userId, @RequestParam CommonStatus status, @RequestParam(value="requestId", required=true) String requestId) throws TSMSException{
		LOGGER.info("START (getSiteContentForNow) requestId={} | userId={} | status={}", requestId, userId, CommonUtils.convertToString(status));
		boolean isValidationFail = false;
		if(userId==null || userId<0l) { isValidationFail = true; }
		if(status==null) { isValidationFail = true; }
		if(isValidationFail) { 
			LOGGER.error("(getSiteContentForNow) requestId={} | TSMSError={}", requestId, TSMSError.INVALID_REQUEST);
			throw new TSMSException(TSMSError.INVALID_REQUEST); 
		}

		List<SiteContentDto> data = siteContentService.getSiteContentForNow(userId, status);
		TSMSResponse res = new TSMSResponse();
			res.setData(data);
			res.setSuccess(true);
			res.setMessage("Success");
			res.setStatus(200);
			res.setTimestamp(LocalDateTime.now().toString());
			res.setRequestId(requestId);
		LOGGER.info("END (getSiteContentForNow) requestId={}", requestId);
		return res;
	}
	
	@RequestMapping(value = REST_CONTROLLER_URL.UPDATE_SITE_CONTENT_MEDIA, method = RequestMethod.POST)
	public @ResponseBody TSMSResponse updateSiteContentMedia(@RequestPart(value="TSMSRequest", required=true) TSMSRequest tSMSRequest, @RequestPart(value="documents", required=true) MultipartFile[] documents, @RequestParam(value="requestId", required=true) String requestId) throws TSMSException{
		LOGGER.info("START (updateSiteContentMedia) requestId={} | request={}", requestId, CommonUtils.convertToString(tSMSRequest));
		if(tSMSRequest.getUserDto()==null || tSMSRequest.getUserDto().getId()==null) {
			LOGGER.error("(updateSiteContentMedia) requestId={} | TSMSError={}", requestId, TSMSError.USER_ID_NOT_FOUND);
			throw new TSMSException(TSMSError.USER_ID_NOT_FOUND);
		}
		if(tSMSRequest.getUserDto().getUserName()==null || tSMSRequest.getUserDto().getUserName().trim().length()==0) { 
			LOGGER.error("(updateSiteContentMedia) requestId={} | TSMSError={}", requestId, TSMSError.USERNAME_NOT_FOUND);
			throw new TSMSException(TSMSError.USERNAME_NOT_FOUND);
		}
		if(documents.length==0) {
			LOGGER.error("(updateSiteContentMedia) requestId={} | TSMSError={}", requestId, TSMSError.DOCUMENT_NOT_FOUND);
			throw new TSMSException(TSMSError.DOCUMENT_NOT_FOUND);
		}
		
		tSMSRequest.getSiteContentDto().setDocuments(documents);
		siteContentService.updateSiteContentMedia(tSMSRequest.getSiteContentDto(), tSMSRequest.getUserDto(), requestId);
		TSMSResponse res = new TSMSResponse();
			res.setSuccess(true);
			res.setMessage("Success");
			res.setStatus(200);
			res.setTimestamp(LocalDateTime.now().toString());
			res.setRequestId(requestId);
		LOGGER.info("END (updateSiteContentMedia) requestId={}", requestId);
		return res;
	}
	
	@RequestMapping(value = REST_CONTROLLER_URL.REMOVE_SITE_CONTENT, method = RequestMethod.DELETE)
	public @ResponseBody TSMSResponse removeSiteContent(@RequestBody TSMSRequest tSMSRequest, @RequestParam(value="requestId", required=true) String requestId) throws TSMSException{
		LOGGER.info("START (removeSiteContent) requestId={} | request={}", requestId, CommonUtils.convertToString(tSMSRequest));
		if(tSMSRequest.getUserDto()==null || tSMSRequest.getUserDto().getId()==null) {
			LOGGER.error("(removeSiteContent) requestId={} | TSMSError={}", requestId, TSMSError.USER_ID_NOT_FOUND);
			throw new TSMSException(TSMSError.USER_ID_NOT_FOUND);
		}
		boolean isValidationFail = false;
		if(tSMSRequest.getSiteContentId()==null || tSMSRequest.getSiteContentId()<0) { isValidationFail= true; }
		if(isValidationFail) {
			LOGGER.error("(removeSiteContent) requestId={} | TSMSError={}", requestId, TSMSError.INVALID_REQUEST);
			throw new TSMSException(TSMSError.INVALID_REQUEST);
		}

		siteContentService.deleteSiteContent(tSMSRequest.getSiteContentId(), tSMSRequest.getUserDto().getId(), requestId);
		TSMSResponse res = new TSMSResponse();
			res.setSuccess(true);
			res.setMessage("Success");
			res.setStatus(200);
			res.setTimestamp(LocalDateTime.now().toString());
			res.setRequestId(requestId);
		LOGGER.info("END (removeSiteContent) requestId={}", requestId);
		return res;
	}
	
	
}
