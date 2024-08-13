package com.oasys.posasset.constant;

public enum ActiveStatus {
	
Active("active", 1), InActive("inactive", 0);
	
	private ActiveStatus(String name, int i) {
		this.name = name;
		this.status = i;
	}
	
	String name;
	int status;
	
	public String getName() {
		return name;
	}

	public int getStatus() {
		return status;
	}


}
