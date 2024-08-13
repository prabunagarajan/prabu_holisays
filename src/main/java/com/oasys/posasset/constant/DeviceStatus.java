package com.oasys.posasset.constant;

public enum DeviceStatus {
	
	NOTWORKING("notworking", "0"), WORKING("working", "1");
	
	private DeviceStatus(String name, String id) {
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
