package com.oasys.helpdesk.utility;

public enum POSAssetRequestStatus {
	PENDING("Pending", 1), REJECTED("Rejected", 2),  APPROVED("Approved", 3), RETURN("Return", 4), DISPATCHED("Dispatched", 5);

	private POSAssetRequestStatus(String type, int id) {
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

	public static POSAssetRequestStatus getType(String value) {
		for (POSAssetRequestStatus module : values()) {
			if (module.type.equals(value)) {
				return module;
			}
		}
		return null;
	}
}
