package com.sep.tripmanagementservice.configuration.utill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sep.tripmanagementservice.configuration.dto.ApprovalDto;
import com.sep.tripmanagementservice.configuration.dto.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.enums.ApprovalStatus;
import com.sep.tripmanagementservice.configuration.enums.Gender;
import com.sep.tripmanagementservice.configuration.enums.Roles;
import com.sep.tripmanagementservice.configuration.enums.Salutation;
import com.sep.tripmanagementservice.configuration.enums.TripCategoryStatus;

public class CommonUtils {

	public static String convertToString(Object object) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getExecutionTime(long startTime) {
		long endTime = System.currentTimeMillis();
		return String.format("%d ms", (endTime - startTime));
	}

	public static boolean isNumberLength(Integer number) {
		if (number == null) {
			return false;
		} else {
			if (String.valueOf(number).length() == 9) {
				return true;
			}
		}
		return false;
	}

	public static boolean haveEmptySpace(String string) {
		Pattern whitespace = Pattern.compile("\\s");
		Matcher matcher = whitespace.matcher(string);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	public static boolean containsOnlyLetters(String str) {
		String regex = "^[a-zA-Z]*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return (matcher.matches() && !haveEmptySpace(str));
	}

	public static boolean validatePhoneNumber(String phoneNo) {
		return (isNumberLength(phoneNo, 10) && isNumeric(phoneNo) && isFirstDigitZero(phoneNo));

	}

	public static boolean isNumberLength(String number, int length) {
		return (number.length() == length);
	}

	public static boolean isNumeric(String stringNumber) {
		Pattern numeric = Pattern.compile(".*[^0-9].*");
		Matcher digits = numeric.matcher(stringNumber);
		return !digits.find();
	}

	public static boolean isFirstDigitZero(String phoneNo) {
		char firstChar = phoneNo.charAt(0);
		return firstChar == '0';
	}

	public static boolean checkMasterTokenNullOrEmpty(String masterToken) {
		return !((masterToken == null || masterToken.isEmpty() || masterToken.isBlank() || masterToken.equals("")));
	}

	public static boolean isValidRole(String role) {
		return (role.equals(Roles.SA.name()) || role.equals(Roles.TO.name()) || role.equals(Roles.TP.name()));
	}

	public static boolean isValidateSalutation(String salutation) {
		return (salutation.equals(Salutation.DR.name()) || salutation.equals(Salutation.HON.name())
				|| salutation.equals(Salutation.MISS.name()) || salutation.equals(Salutation.MR.name())
				|| salutation.equals(Salutation.MRS.name()) || salutation.equals(Salutation.MS.name())
				|| salutation.equals(Salutation.REV.name()));
	}

	public static boolean isValidPassword(String password) {
		/*
		 * Regular expression to validate password Lenght Between 8 - 15 characters
		 * Contains at least 1 special character and 1 capital letter and 1 simple
		 * letter and 1 number
		 */

		String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,15}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public static boolean isValidEmail(String email) {
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean checkMandtoryFieldsNullOrEmptyTripCategory(TripCategoryDto tripcategorydto) {
		return !((tripcategorydto.getName() == null || tripcategorydto.getName().isEmpty()
				|| tripcategorydto.getName().equals(""))
				|| (tripcategorydto.getDescription() == null || tripcategorydto.getDescription().isEmpty()
						|| tripcategorydto.getDescription().equals(""))
				|| (tripcategorydto.getCode() == null || tripcategorydto.getCode().isEmpty()
						|| tripcategorydto.getCode().equals("")
						|| (tripcategorydto.getCreatedBy() == null || tripcategorydto.getCreatedBy().isEmpty()
								|| tripcategorydto.getCreatedBy().equals(""))));
	}

	public static boolean checkApprovalMandtoryFieldsNullOrEmpty(ApprovalDto approvalDto) {
		return !((approvalDto.getId() == null)
				|| (approvalDto.getContent() == null || approvalDto.getContent().isEmpty()
						|| approvalDto.getContent().isBlank() || approvalDto.getContent().equals(""))
				|| (approvalDto.getCreatedBy() == null || approvalDto.getCreatedBy().isEmpty()
						|| approvalDto.getCreatedBy().isBlank() || approvalDto.getCreatedBy().equals(""))
				|| (approvalDto.getEmail() == null || approvalDto.getEmail().isEmpty()
						|| approvalDto.getEmail().isBlank() || approvalDto.getEmail().equals(""))
				|| (approvalDto.getReason() == null || approvalDto.getReason().isEmpty()
						|| approvalDto.getReason().isBlank() || approvalDto.getReason().equals("")));
	}

	public static boolean isValidateApprovalStatus(String approvalStatus) {
		return (approvalStatus.equals(ApprovalStatus.PENDING.name())
				|| approvalStatus.equals(ApprovalStatus.APPROVED.name())
				|| approvalStatus.equals(ApprovalStatus.REJECTED.name()));
	}

	public static boolean NICValidation(String nic) {

		String Format1Regex = "\\d{9}V";
		String Format2Regex = "\\d{12}";

		Pattern pattern1 = Pattern.compile(Format1Regex);
		Pattern pattern2 = Pattern.compile(Format2Regex);

		Matcher matcher1 = pattern1.matcher(nic);
		Matcher matcher2 = pattern2.matcher(nic);

		return ((matcher1.matches() && !haveEmptySpace(nic)) || (matcher2.matches() && !haveEmptySpace(nic)));
	}

	public static boolean isValidGender(String gender) {
		return (gender.equals(Gender.M.name()) || gender.equals(Gender.F.name()) || gender.equals(Gender.O.name()));
	}

	public static boolean isValidDOB(String dob) {

		String regex = "\\d{4}-\\d{2}-\\d{2}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(dob);

		if (!matcher.matches()) {
			return false;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false);

			try {
				Date date = sdf.parse(dob);
				return !date.after(new Date());
			} catch (ParseException e) {
				return false;
			}
		}
	}

	public static boolean isValidStatus(String status) {
		return (status.equals(TripCategoryStatus.ACTIVE.name()) || status.equals(TripCategoryStatus.INACTIVE.name())
				|| status.equals(TripCategoryStatus.DELETED.name()));
	}

}
