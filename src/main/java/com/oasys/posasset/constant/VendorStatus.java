package com.oasys.posasset.constant;

public enum VendorStatus {

	ACCEPTED("accepted", "0"), REQUESTFORCLARIFICATION("request for clarification", "1"), REJECTED("rejected", "2"),
	DISPATCHED("dispatched", "3"), FORCECLOSURE("forceclosure", "4"), ACKNOWLEDGED("acknowledged", "5"),
	CANCELLED("cancelled", "6"), REQUESTED("requested", "7"), INPROGRESS("inprogress", "8"),
	PARTIALDISPATCHED("partial dispatched", "9"),ASSIGNED("assigned","10");

	private VendorStatus(String name, String id) {
		this.name = name;
		this.id = id;
	}

	String name;
	String id;

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

}
