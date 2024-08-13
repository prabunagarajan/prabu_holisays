package com.oasys.helpdesk.enums;

public enum TemplateType {
	EMAIL("Email", 1), SMS("SMS", 2);

	private TemplateType(String type, int id) {
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

	public static TemplateType getType(String value) {
		for (TemplateType module : values()) {
			if (module.type.equals(value)) {
				return module;
			}
		}
		return null;
	}
}
