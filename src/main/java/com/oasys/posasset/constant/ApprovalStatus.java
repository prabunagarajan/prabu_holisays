package com.oasys.posasset.constant;

public enum ApprovalStatus {
	
	INPROGRESS("inprogress", "0"), APPROVED("approved", "1"), FORWARDED("forwarded", "2"),
		REQUESTFORCLARIFICATION("requestforclarification", "3"), DRAFT("draft", "4"),REJECT("reject", "5"),DISPATCHED("dispatched","6");

	private ApprovalStatus(String name, String id) {
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

	public static ApprovalStatus getType(String value) {
		for (ApprovalStatus module : values()) {
			if (module.name.equalsIgnoreCase(value)) {
				return module;
			}
		}
		return null;
	}

	
}
