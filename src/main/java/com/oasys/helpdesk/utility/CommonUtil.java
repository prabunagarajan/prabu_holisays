package com.oasys.helpdesk.utility;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oasys.helpdesk.constant.Constant;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CommonUtil {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ObjectMapper objectMapper;

	
	public <T> T modalMap(Object source, Class<T> destination) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(source, destination);

	}
	public static void main(String args[]) {
		MenuPrefix p = MenuPrefix.getType("Asset Type");
		System.out.println(generateRandomPassword());
	}
	
	public Boolean isEmailValid(String email) {
		Pattern pattern = Pattern.compile(Constant.EMAIL_REGEX);
		if (StringUtils.isBlank(email)) {
			return Boolean.FALSE;
		} else {
			return pattern.matcher(email.trim()).matches();
		}
	}
	
	public Boolean isPhoneNumberValid(String phoneNumber) {
		Pattern pattern = Pattern.compile(Constant.PHONE_REGEX);
		if (StringUtils.isBlank(phoneNumber)) {
			return Boolean.FALSE;
		} else {
			return pattern.matcher(phoneNumber.trim()).matches();
		}
	}
	
	public Boolean isNameValid(String name) {
		Pattern pattern = Pattern.compile(Constant.NAME_REGEX);
		if (StringUtils.isBlank(name)) {
			return Boolean.FALSE;
		} else {
			return pattern.matcher(name.trim()).matches();
		}
	}
	
	public static String concatenate(List<String> listOfItems, String separator) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> stit = listOfItems.iterator();

		while (stit.hasNext()) {
			sb.append(stit.next());
			if (stit.hasNext()) {
				sb.append(separator);
			}
		}

		return sb.toString();
	}
	public static Boolean isStrongPassword(String inputPassword) {
		if (inputPassword.matches(Constant.PASSWORD_PATTERN)) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	public static String generateRandomPassword() {
		char[] possibleCharacters = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*?")).toCharArray();
		String randomStr = RandomStringUtils.random( 8, 0, possibleCharacters.length-1, false, false, possibleCharacters, new SecureRandom() );
		System.out.println( randomStr );
		return randomStr;
	}
	
	public static String utf8EncodeString(String value)
	{
		byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		return  new String(bytes, StandardCharsets.UTF_8);
	}
	
	public static Date convertStringToDate(String str) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
		try {
			log.info("parsing approval date : {} ", str);
			return dateFormat.parse(str);
		} catch (Exception exception) {
			log.error("Error occurred while parsing date : {}, {}", str, exception);
		}
		return null;
	}
	
	public static Map<String, Object> objectToMap(Object ob)
	{
		ObjectMapper oMapper = new ObjectMapper();
		return oMapper.convertValue(ob, Map.class);
	}
	public <T> T ojectMap(Object ob, Class<T> type) {
		return objectMapper.convertValue(ob, type);

	}
	
	public void modalMapCopy(Object source, Object destination) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.map(source, destination);

	}
	
	public static Boolean isDateValid(String date) {
		try {
			LocalDate.parse(date, DateTimeFormatter.ofPattern(Constant.VALID_DATE_FORMAT));
		} catch (DateTimeParseException pe) {
			log.info("invalid date : {}", date);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
