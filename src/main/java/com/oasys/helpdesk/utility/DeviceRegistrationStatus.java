package com.oasys.helpdesk.utility;

public enum DeviceRegistrationStatus {

	PENDING("Pending", 1), APPROVED("Approved", 2), REJECTED("Rejected", 3),DEVICELOST("Device Lost", 4);

	private DeviceRegistrationStatus(String type, int id) {
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

	public static DeviceRegistrationStatus getType(String value) {
		for (DeviceRegistrationStatus module : values()) {
			if (module.type.equals(value)) {
				return module;
			}
		}
		return null;
	}
}
