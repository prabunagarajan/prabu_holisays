package com.oasys.helpdesk.utility;

public enum EmploymentStatus {

	ACTIVE("Active", 1), INACTIVE("Inactive", 2), RETIRED("Retired", 3);

	private EmploymentStatus(String type, int id) {
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

	public static EmploymentStatus getType(String value) {
		for (EmploymentStatus module : values()) {
			if (module.type.equals(value)) {
				return module;
			}
		}
		return null;
	}
}