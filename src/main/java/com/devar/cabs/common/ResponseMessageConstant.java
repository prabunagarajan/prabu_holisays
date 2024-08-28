package com.devar.cabs.common;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public enum ResponseMessageConstant {
	
	ASSIGNTOID_HANDLINGOFFICERID_SAME(431,431),
	MANDTORY_REQUEST_PARM(103,103),
	INVALID_REQUEST_PARM(104,104),
	ALREADY_EXISTS(105,105),
	RECORD_ALREADY_EXISTS(106,106),
	NO_RECORD_FOUND(404,404),
	RECORD_CREATION_FAILED(406, 406),
	RECORD_UPDATION_FAILED(407, 407),
	INACTIVE_ID_PASSED(408, 408),
	BACKUP_USER_ALREADY_MAPPED(410,410),
	BAD_CREDENTIALS(411, 411),
	INACTIVE_USER_ACCOUNT(412, 412),
	USER_ACCOUNT_LOCKED(413, 413),
	INVALID_OTP_OR_TOKEN(414, 414),
	EXPIRED_OTP_OR_TOKEN(415, 415),
	INACTIVE_OTP_OR_TOKEN(416, 416),
	ROLES_MISMATCH(417, 417),
	ERROR_RESPONSE_FROM_CALLING_SERVICE(418, 418),
	FALSE_RESPONSE_FROM_CALLING_SERVICE(419, 419),
	SHIFT_END_DATE_LESS_THAN_START_DATE(420, 420),
	SHIFT_START_DATE_LESS_THAN_TODAY_DATE(421, 421),
	JOINING_DATE_LESS_THAN_TODAY_DATE(422,422),
	MAX_OTP_COUNT_EXCEEDED(423,423),
	EMAIL_NOT_VERIFIED(424,424),
	WEAK_PASSWORD(425,425),
	DEVICE_DETAILS_MISSING(426,426),
	DEVICE_ALREADY_ASSOCIATED_TO_ANOTHER_SHOP(427,427),
	DEVICE_ALREADY_REGISTERED(428,428),
	DEVICE_STATUS_SHOULD_BE_PENDING(429,429),
	FIELD_MISSING(430, 430),
	
	SUCCESSFULL_LOGIN(202,202),
	PASSWORD_UPDATED_SUCCESSFULLY(203,203),
	SUCCESS_RESPONSE_FROM_CALLING_SERVICE(204, 204),
	
	CONNECTION_ERROR(501,501),
	THIS_CATEGORY_NOTMAPPED(404,404),
	INVALID_ASSOCIATE_USER_ID(450,450),
	INVALID_ASSOCIATE_USER_ROLE(451,451),
	PRINTERID_INVALID(452,452);
	
	
	
	
	
	
	
	private int errorCode;
	private int messageCode;
	
	private ResponseMessageConstant(int errorCode,int messageCode) {
		this.errorCode=errorCode;
		this.messageCode=messageCode;
	}
	@Autowired
	private MessageSource messageSource;
	
	public int getErrorCode() {
		return errorCode;
	}
	
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	public String getMessage() 
	{		
		return messageSource.getMessage(String.valueOf(messageCode),new Object[]{},Locale.ENGLISH);
	}
	public String getMessage(Object[] arg1) {
		return messageSource.getMessage(String.valueOf(messageCode), arg1, Locale.ENGLISH);
	}
	
	public String getMessage(String value) {
		Object[]ob=new Object[1];
		ob[0]=value;
		return messageSource.getMessage(String.valueOf(messageCode), ob, Locale.ENGLISH);
	}
	public static Optional<ResponseMessageConstant> getResponseMessageContext(int code)
	{
		for(ResponseMessageConstant responseMessageConstant:ResponseMessageConstant.values())
		{
				if(responseMessageConstant.messageCode==code)
				{
					return  Optional.of(responseMessageConstant);
				}	
		}
		return Optional.of(null);
		
	}
	
}