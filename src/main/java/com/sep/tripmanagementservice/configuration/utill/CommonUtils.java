package com.sep.tripmanagementservice.configuration.utill;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sep.tripmanagementservice.configuration.dto.tripcategory.TripCategoryDto;
import com.sep.tripmanagementservice.configuration.dto.user.testDto;
import com.sep.tripmanagementservice.configuration.enums.Roles;
import com.sep.tripmanagementservice.configuration.enums.Salutation;

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

	public static boolean isValidateRole(String role) {
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

	public static boolean checkMandtoryFieldsNullOrEmpty(testDto testDto) {
		return !((testDto.getName() == null || testDto.getName().isEmpty() || testDto.getName().isBlank()
						|| testDto.getName().equals(""))
				|| (testDto.getAddress() == null || testDto.getAddress().isEmpty() || testDto.getAddress().isBlank()
						|| testDto.getAddress().equals("")));
	}
	public static boolean checkMandtoryFieldsNullOrEmptyTripCategory(TripCategoryDto tripcategorydto) {
		return !((tripcategorydto.getCategoryName() == null || tripcategorydto.getCategoryName().isEmpty() || tripcategorydto.getCategoryName().isBlank()
						|| tripcategorydto.getCategoryName().equals(""))
				|| (tripcategorydto.getDescription() == null || tripcategorydto.getDescription().isEmpty() || tripcategorydto.getDescription().isBlank()
						|| tripcategorydto.getDescription().equals(""))
				|| (tripcategorydto.getCode() == null || tripcategorydto.getCode().isEmpty() || tripcategorydto.getCode().isBlank()
						|| tripcategorydto.getCode().equals(""))
				|| (tripcategorydto.getAddedAt() == null || tripcategorydto.getCode().isEmpty() || tripcategorydto.getCode().isBlank()
						|| tripcategorydto.getCode().equals("")));
	}
}
