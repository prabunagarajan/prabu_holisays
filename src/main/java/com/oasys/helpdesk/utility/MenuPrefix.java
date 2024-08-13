package com.oasys.helpdesk.utility;

public enum MenuPrefix {

	AS("Asset Type", 1), AB("Asset Brand", 2), IF("Issue From", 3), BT("Asset Brand Type", 4),
	CA("Category", 5), SC("Sub Category", 6), DA("Device Accessories Status",7), DPT("Department Code", 8),
	FQ("FAQ", 9),AP("Actual Problem", 10),AT("Action Taken", 11),SL("Sla", 12),P("priority",13),PR("PR Code",14), CON("configuration",15),
	SWD("shiftWorkingDays",16), SN("salutation",17),WC("Workflow",18), EMC("EMC", 19),TS("TS Code",20),ISDETAILS("issue Code",21),
	DISTRICT("District Code",22),DM("Designation", 23),UG("UG Code", 24), DHLN("device_hardware_list",25),
	DHN("device_hardware_name",26),TK("Ticket", 27),  AC("accesssories_code",28), ACL("accesssories_name_id",29),SUB("Subsol Code",30),
	EM("Employee ID", 31), ER("ER", 32), SF("survey_form",33),sk("knowledge_solution",34),TU("TypeOfUser",35),GSREG("grievanceid", 36),
	AA("asset_accessories",37),GC("grievanceCategory",38),GCID("category_issue_Code",39),GCFAQ("grievance_category_faq",40),GCSLA("grievance_category_sla",41),
	GCWF("grievance_category_workflow",42),GPRI("grievance_category_priority",43),GKB("grievance_knowledge_base",44),WF("Grievance Escation Workflow",45);

	private MenuPrefix(String type, int id) {
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
	
	public static MenuPrefix getType(String value) {
		for (MenuPrefix module : values()) {
			if (module.type.equals(value)) {
				return module;
			}
		}
		return null;
	}
}
