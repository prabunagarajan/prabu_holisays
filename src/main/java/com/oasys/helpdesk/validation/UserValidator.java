package com.oasys.helpdesk.validation;

import com.oasys.helpdesk.conf.exception.InvalidUserValidation;

public class UserValidator {
	public static void userIdValidator(String id) {
		if (id != null) {
			try {
				Long.parseLong(id);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new InvalidUserValidation();
			}
		}
	}
}
