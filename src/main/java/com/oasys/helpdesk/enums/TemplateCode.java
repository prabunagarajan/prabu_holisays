package com.oasys.helpdesk.enums;

public enum TemplateCode {
	TICKET_CREATION("Ticket Created", 1), TICKET_REOPENED("Ticket Re-opened", 2),  TICKET_CLOSED("Ticket Closed", 3),  TICKET_RESOLVED("Ticket Resolved", 4), GRIEVANCE_OTP_GENERATION("Grievance OTP generation", 5),HELPDESK_OTP_GENERATION("Helpdesk OTP Generation", 6);

	private TemplateCode(String type, int id) {
		this.type = type;
		this.id = id;

	}

	String type;
	int id;

	public String getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public static TemplateCode getType(String value) {
		for (TemplateCode module : values()) {
			if (module.type.equals(value)) {
				return module;
			}
		}
		return null;
	}
}
