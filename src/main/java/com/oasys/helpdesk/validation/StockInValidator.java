package com.oasys.helpdesk.validation;

import com.oasys.helpdesk.conf.exception.InvalidUserValidation;

public class StockInValidator {
	public static void stockinIdValidator(String id) {
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
