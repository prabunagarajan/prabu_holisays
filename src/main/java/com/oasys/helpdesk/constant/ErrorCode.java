package com.oasys.helpdesk.constant;


public interface ErrorCode {

	public static class Error {
		private Integer errorCode;

		public Integer getErrorCode() {
			return errorCode;
		}

		public Integer getCode() {
			return errorCode;
		}
		
		public Error(Integer errorCode) {
			this.errorCode = errorCode;
		}
	}
	
	public static final Error SUCCESS_RESPONSE = new Error(200);
	public static final Error CREATED = new Error(200);
	public static final Error UNAUTHORIZED = new Error(401);
	public static final Error EXCEPTION = new Error(-1);
	public static final Error INVALID_DATA = new Error(5);
	public static final Error BAD_REQUEST = new Error(400);
	public static final Error NO_RECORD_FOUND = new Error(404);
	public static final Error IN_VALID_OTP = new Error(404);
	public static final Error THIS_CATEGORY_NOTMAPPED = new Error(404);
	
	public static final Error RECORD_CREATION_FAILED = new Error(406);
	public static final Error RECORD_UPDATION_FAILED = new Error(407);
	public static final Error INACTIVE_USER_ACCOUNT = new Error(412);
	public static final Error USER_ACCOUNT_LOCKED = new Error(413);
	public static final Error INVALID_OTP_OR_TOKEN = new Error(414);
	public static final Error EXPIRED_OTP_OR_TOKEN = new Error(415);
	public static final Error INACTIVE_OTP_OR_TOKEN = new Error(416);
	public static final Error ROLES_MISMATCH = new Error(417);
	
	
	public static final Error PASSWORD_UPDATED_SUCCESSFULLY = new Error(203);
	public static final Error ERROR_RESPONSE_FROM_CALLING_SERVICE = new Error(418);
	public static final Error FALSE_RESPONSE_FROM_CALLING_SERVICE = new Error(419);
	public static final Error SHIFT_END_DATE_LESS_THAN_START_DATE = new Error(420);
	public static final Error SHIFT_START_DATE_LESS_THAN_TODAY_DATE = new Error(421);
	public static final Error ASSIGNTOID_HANDLINGOFFICERID_SAME = new Error(431);
	
	public static final Error JOINING_DATE_LESS_THAN_TODAY_DATE = new Error(422);
	public static final Error SUCCESS_RESPONSE_FROM_CALLING_SERVICE = new Error(204);
	
	public static final Error CONNECTION_ERROR = new Error(501);
	public static final Error FAILURE_RESPONSE = new Error(500);
	public static final Error ACCESS_DENIED = new Error(403);
	public static final Error DEVICE_ALREADY_EXIST = new Error(6);
	public static final Error ALREADY_VERIFIED = new Error(205);
	public static final Error RECORD_DELETION_FAILED = new Error(453);
	public static final Error NO_CHANGE_TO_UPDATE = new Error(409);

}
