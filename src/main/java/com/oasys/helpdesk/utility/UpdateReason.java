package com.oasys.helpdesk.utility;

public enum UpdateReason {

	TRANSFER("Transfer", 1), PROMOTION("Promotion", 2), LONG_LEAVE("Long Leave", 3), SUSPEND("Suspend", 4),NEW_USER("New User", 5);

	private UpdateReason(String type, int id) {
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

	public static UpdateReason getType(String value) {
		for (UpdateReason module : values()) {
			if (module.type.equals(value)) {
				return module;
			}
		}
		return null;
	}
}
