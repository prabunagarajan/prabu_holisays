package com.oasys.helpdesk.conf;

public enum IncomingSmsStatus {

	PENDING("PENDING", 0), SUCESSS("SUCESSS", 1), FAIL("FAIL", 2);

	private IncomingSmsStatus(String name, int id) {
		this.name = name;
		this.id = id;

	}

	String name;
	int id;

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	
}
