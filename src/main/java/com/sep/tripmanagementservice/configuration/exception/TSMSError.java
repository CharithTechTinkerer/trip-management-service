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
	REGISTRATION_FAILED("014", 404, "Registration Failed"),
	INCORRECT_PASSWORD("015", 401, "Password incorrect. Please verify your password and try again"),
	MASTER_TOKEN_NOT_FOUND("016", 404, "Master Token Not Found"),
	MASTER_TOKEN_MANDATORY("017", 404, "Master Token mandatory for System Admin"),
	INVALID_MASTER_TOKEN("018", 400, "Invalid Master Token"),
	AUTH_TOKEN_EXPIRED("019", 401, "Authentication Token Expired"),
	INVALID_EMAIL("020", 400, "Invalid Email Address"),
	INVALID_USERNAME("022", 400, "Invalid UserName"),
	EMAIL_EXIST("023", 409, "An account associated with this email already exists"),
	USERNAME_EXIST("024", 409, "An account associated with this username already exists"),
	UNAUTHORIZED("025", 401, "Unauthorized"),
	APPROVAL_FAILED("026", 404, "Approval Failed"),
	INVALID_APPROVAL_STATUS("027", 400, "Invalid Approval Status"),
	APPROVAL_REQUEST_NOT_FOUND("028", 404, "Approval Request Not Found"),
	ID_FIELD_EMPTY("029", 404, "Id Field is Empty"),
	EMAIL_FIELD_EMPTY("030", 404, "Email Field is Empty"),
	APPROVAL_STATUS_FIELD_EMPTY("031", 404, "Approval Status Field is Empty"),
	USER_UPDATE_FAILED("032", 404, "User Profile Update Failed"),
	USERNAME_UPDATE_NOT_ALLOWED("033", 404, "You are not allowed to update the username"),
	PROFILE_PICTURE_UPLOAD_FAILED("034", 404, "Profile Picture Upload Failed"),
	APPROVAL_REQUEST_CREATION_FAILED("035", 404, "Approval Request Creation Failed"),
	PROFILE_PICTURE_DELETE_FAILED("035", 404, "Profile Picture Deletion Failed"),
	INVALID_NIC("036", 400, "Invalid NIC Number"),
	INVALID_GENDER("037", 400, "Invalid Gender"),
	INVALID_DOB("038", 400, "Invalid Date of Birth");
	

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
