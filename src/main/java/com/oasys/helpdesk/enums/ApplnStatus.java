package com.oasys.helpdesk.enums;

public enum ApplnStatus {
	
	INPROGRESS("inprogress", "0"),ACCEPTED("accepted", "1"),APPROVED("approved","2"), REQUESTFORCLARIFICATION("request for clarification", "3"),REJECTED("rejected", "4"),
	CANCELLED("cancelled", "5"),FORWARDED("forwarded", "6");
	
	private ApplnStatus(String name, String id) {
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
