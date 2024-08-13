package com.oasys.helpdesk.utility;

public enum HelpdeskUserAuditAction {
	CREATED("Created", 1), UPDATED("Updated", 2);

	private HelpdeskUserAuditAction(String type, int id) {
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

	public static HelpdeskUserAuditAction getType(String value) {
		for (HelpdeskUserAuditAction module : values()) {
			if (module.type.equals(value)) {
				return module;
			}
		}
		return null;
	}
}
