package com.oasys.helpdesk.conf.exception;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.ResponseHeaderUtility;



@ControllerAdvice
public class UpExciseExceptionHandler {
	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<Object> NoResponseException(HttpServletRequest request, RecordNotFoundException ex) {
		GenericResponse GenericResponseObj=Library.noRecordFoundResponse(ErrorMessages.NO_RECORD_FOUND); 
		return new ResponseEntity<>(GenericResponseObj, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@ExceptionHandler(InvalidDataValidation.class)
	public ResponseEntity<Object> InvalidDataValidation(HttpServletRequest request, InvalidDataValidation ex) {
		GenericResponse GenericResponseObj=Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(), ex.getMessage());
		return new ResponseEntity<>(GenericResponseObj, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	
	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> exception(HttpServletRequest request, Exception ex) {
		ex.printStackTrace();
		GenericResponse GenericResponseObj=Library.getFailResponseCode(ErrorCode.EXCEPTION.getErrorCode(), ErrorMessages.EXCEPTION_MESSAGE); 
		return new ResponseEntity<>(GenericResponseObj, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@ExceptionHandler(ApprovalFailureException.class)
	public ResponseEntity<Object> ApprovalFailureException(HttpServletRequest request, ApprovalFailureException ex) {
		GenericResponse GenericResponseObj=Library.noRecordFoundResponse(ErrorMessages.NO_RECORD_FOUND); 
		return new ResponseEntity<>(GenericResponseObj, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> DataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException ex) {
		GenericResponse GenericResponseObj=Library.getFailResponseCode(ErrorCode.EXCEPTION.getCode(), ex.getMostSpecificCause().getMessage()); 
		return new ResponseEntity<>(GenericResponseObj, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> accessDeniedException(HttpServletRequest request, AccessDeniedException ex) {
		GenericResponse GenericResponseObj=Library.getFailResponseCode(ErrorCode.ACCESS_DENIED.getCode(), ErrorMessages.ACCESS_DENIED); 
		return new ResponseEntity<>(GenericResponseObj, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@ExceptionHandler(DeviceAlreadyExistValidation.class)
	public ResponseEntity<Object> deviceAlreadyExistValidation(HttpServletRequest request, DeviceAlreadyExistValidation ex) {
		GenericResponse GenericResponseObj=Library.getFailResponseCode(ErrorCode.DEVICE_ALREADY_EXIST.getErrorCode(), ErrorMessages.DEVICE_ALREADY_REGISTERED);
		return new ResponseEntity<>(GenericResponseObj, ResponseHeaderUtility.HttpHeadersConfig(), HttpStatus.OK);
	}
	
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public GenericResponse handle(javax.validation.ConstraintViolationException exception, Locale locale) {

		exception.printStackTrace();	
		return processFieldErrors(exception.getConstraintViolations(), locale);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public GenericResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, Locale locale,
			HttpServletRequest request) {

		BindingResult result = ex.getBindingResult();
		ex.printStackTrace();
		return processFieldErrors(new HashSet<FieldError>(result.getFieldErrors()), locale);

	}
	
	private GenericResponse processFieldErrors(Set<ConstraintViolation<?>> fieldErrors, Locale locale) {
		GenericResponse commonResponse = new GenericResponse();
		String defaultMessage = "103";
		String field = "";
		for (ConstraintViolation<?> fieldError : fieldErrors) {

			defaultMessage = fieldError.getMessage();
			field = (String) fieldError.getPropertyPath().toString();

		}
		if (defaultMessage.equalsIgnoreCase("103")) {
			commonResponse.setErrorCode(ResponseMessageConstant.MANDTORY_REQUEST_PARM.getErrorCode());
			commonResponse.setStatus("f");
			commonResponse.setUserDisplayMesg(ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { field }));
			return commonResponse;
		}
		commonResponse.setErrorCode(ResponseMessageConstant.INVALID_REQUEST_PARM.getErrorCode());
		commonResponse.setStatus("f");
		commonResponse.setUserDisplayMesg(ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { field }));
		return commonResponse;

	}

	private GenericResponse processFieldErrors(HashSet<FieldError> fieldErrors, Locale locale) {
		GenericResponse commonResponse = new GenericResponse();
		String defaultMessage = "103";
		String field = "";

		for (FieldError fieldError : fieldErrors) {
			defaultMessage = fieldError.getDefaultMessage();
			field = fieldError.getField();

		}
		if(!StringUtils.isNumeric(defaultMessage)) {
			commonResponse.setStatus("f");
			commonResponse.setErrorCode(ResponseMessageConstant.INVALID_REQUEST_PARM.getErrorCode());
			commonResponse.setUserDisplayMesg(defaultMessage);
			return commonResponse;
		}
		Optional<ResponseMessageConstant> optional=ResponseMessageConstant.getResponseMessageContext(Integer.parseInt(defaultMessage));
		if (optional.isPresent()) {
			commonResponse.setStatus("f");
			commonResponse.setErrorCode(optional.get().getErrorCode());
			commonResponse.setUserDisplayMesg(optional.get().getMessage(new Object[] { field }));
			return commonResponse;

		}
		commonResponse.setStatus("f");
		commonResponse.setErrorCode(ResponseMessageConstant.INVALID_REQUEST_PARM.getErrorCode());
		commonResponse.setUserDisplayMesg(ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { field }));
		return commonResponse;
	}
	
}
