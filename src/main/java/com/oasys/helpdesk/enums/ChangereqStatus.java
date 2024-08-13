package com.oasys.helpdesk.enums;

public enum ChangereqStatus {
	
	INPROGRESS("inprogress", "0"),ACCEPTED("accepted", "1"),APPROVED("approved","2"),ASSIGNED("assigned","3"),
	PENDING("pending","4"), REQUESTFORCLARIFICATION("request for clarification", "5"),REJECTED("rejected", "6"),
	CANCELLED("cancelled", "7"),COMPLETED("completed","8"),DRAFT("DRAFT","9"),FORWARDED("forwarded", "10");;
	
	private ChangereqStatus(String name, String id) {
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
