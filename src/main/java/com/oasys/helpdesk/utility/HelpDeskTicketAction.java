package com.oasys.helpdesk.utility;

public enum HelpDeskTicketAction {
	CREATED("Created", 1), UPDATED("Updated", 2);

	private HelpDeskTicketAction(String type, int id) {
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

	public static HelpDeskTicketAction getType(String value) {
		for (HelpDeskTicketAction module : values()) {
			if (module.type.equals(value)) {
				return module;
			}
		}
		return null;
	}
}
