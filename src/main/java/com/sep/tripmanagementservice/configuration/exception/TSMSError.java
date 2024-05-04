package com.sep.tripmanagementservice.configuration.exception;

public enum TSMSError {

	OK("001", 200, "OK"), 
	ALREADY_EXIST("002", 409, "Already Exists"), 
	INVALID_REQUEST("003", 400, "Invalid Request"),
	INVALID_ROLE("004", 400, "Invalid Role"), 
	INVALID_CONTACT_NO("005", 400, "Invalid Contact Number"),
	NOT_FOUND("006", 404, "Data Not Found"), 
	CREATED("007", 201, "Created"),
	MANDOTORY_FIELDS_EMPTY("008", 400, "Mandatory fields are null. Please ensure all required fields are provided"),
	INVALID_SALUTATION("009", 400, "Invalid Salutation"), 
	INVALID_FIRST_NAME("010", 400, "Invalid First Name"),
	INVALID_LAST_NAME("011", 400, "Invalid Last Name"), 
	INVALID_PASSWORD("012", 400, "Invalid Password"),
	USER_NOT_FOUND("013", 404, "User Not Found"),
	REGISTRATION_FAILED("014", 400, "Registration Failed"),
	INCORRECT_PASSWORD("015", 401, "Password incorrect. Please verify your password and try again"),
	MASTER_TOKEN_NOT_FOUND("016", 404, "Master Token Not Found"),
	MASTER_TOKEN_MANDATORY("017", 400, "Master Token mandatory for System Admin"),
	INVALID_MASTER_TOKEN("018", 400, "Invalid Master Token"),
	AUTH_TOKEN_EXPIRED("019", 401, "Authentication Token Expired"),
	INVALID_EMAIL("020", 400, "Invalid Email Address"),
	INVALID_USERNAME("022", 400, "Invalid UserName"),
	EMAIL_EXIST("023", 409, "An account associated with this email already exists"),
	USERNAME_EXIST("024", 409, "An account associated with this username already exists"),
	UNAUTHORIZED("025", 401, "Unauthorized"),
	APPROVAL_FAILED("026", 400, "Approval Failed"),
	INVALID_APPROVAL_STATUS("027", 400, "Invalid Approval Status"),
	APPROVAL_REQUEST_NOT_FOUND("028", 404, "Approval Request Not Found"),
	ID_FIELD_EMPTY("029", 400, "Id Field is Empty"),
	EMAIL_FIELD_EMPTY("030", 400, "Email Field is Empty"),
	APPROVAL_STATUS_FIELD_EMPTY("031", 400, "Approval Status Field is Empty"),
	USER_UPDATE_FAILED("032", 400, "User Profile Update Failed"),
	USERNAME_UPDATE_NOT_ALLOWED("033", 405, "You are not allowed to update the username"),
	PROFILE_PICTURE_UPLOAD_FAILED("034", 400, "Profile Picture Upload Failed"),
	APPROVAL_REQUEST_CREATION_FAILED("035", 400, "Approval Request Creation Failed"),
	PROFILE_PICTURE_DELETE_FAILED("035", 400, "Profile Picture Deletion Failed"),
	INVALID_NIC("036", 400, "Invalid NIC Number"),
	INVALID_GENDER("037", 400, "Invalid Gender"),
	INVALID_DOB("038", 400, "Invalid Date of Birth"),
	TRIP_CATEGORY_CREATION_FAILED("039", 400, "Trip Category Creation Failed"),
	UPDATE_BY_FIELD_EMPTY("040", 400, "Update By Field is Empty"),
	TRIP_CATEGORY_NOT_FOUND("041", 404, "Trip Category Not Found"),
	INVALID_TRIP_CATEGORY_STATUS("042", 400, "Invalid Trip Category Status"), 
	TRIP_CATEGORY_CODE_ALREADY_EXIST("043", 409, "Trip Category Code Already Exists"),
	TRIP_CATEGORY_DELETE_FAILED("044", 400, "Trip Category Deletion Failed"),
	FAILED("045", 401, "FAILED"),
	TRIP_CATEGORY_STATUS_MANDATORY("046", 400, "Trip Category Status is Mandatory"),
	DOCUMENT_NOT_FOUND("047", 404, "Document not found"),
	USER_ID_NOT_FOUND("048", 404, "User id not found"),
	NON_EXISTING_TRIP_ID_FOR_USER("049", 400, "Non existing trip id for user"),
	DOC_ID_NOT_EXIST_FOR_TRIP_ID("050", 400, "Document Id does not existing for the trip id"),
	DOCUMENT_CONVERSION_FAILED("051", 404, "Document conversion failed"),
	DOCUMENT_UPLOAD_FAILD("052", 404, "Document upload failed"),
	DOCUMENT_DELETE_FAILD("053", 404, "Document delete failed"),
	USERNAME_NOT_FOUND("054", 404, "Username not found");
	
	
	private int status;
	private String code;
	private String message;

	private TSMSError(String errorCode, int errorStatus, String errorMessage) {
		this.status = errorStatus;
		this.code = errorCode;
		this.message = errorMessage;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String errorCode) {
		this.code = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String errorMessage) {
		this.message = errorMessage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
