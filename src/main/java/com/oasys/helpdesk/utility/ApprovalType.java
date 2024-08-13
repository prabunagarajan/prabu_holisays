package com.oasys.helpdesk.utility;

public enum ApprovalType {
	AUTO("Auto", 1), MANUAL("Manual", 2);

	private ApprovalType(String type, int id) {
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

	public static ApprovalType getType(String value) {
		for (ApprovalType module : values()) {
			if (module.type.equals(value)) {
				return module;
			}
		}
		return null;
	}
}
