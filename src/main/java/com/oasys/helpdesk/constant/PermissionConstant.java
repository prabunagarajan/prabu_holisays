package com.oasys.helpdesk.constant;

public class PermissionConstant {
	public static final String VIEW_PROFILE = "hasAuthority('HELP_DESK_VIEW_PROFILE') or hasAuthority('GRIEVANCE_VIEW_PROFILE')";
	public static final String POS_ASSET_REQUEST = "hasAuthority('POS_ASSET_REQUEST')";
	public static final String HELPDESK_USER_MANAGEMENT = "hasAuthority('HELPDESK_USER_MANAGEMENT')";
	public static final String USER_BY_ROLE = "hasAuthority('HELPDESK_USER_MANAGEMENT') or hasAuthority('HELPDESK_APP_SUPPORT') or hasAuthority('HELPDESK_FIELD_ENGINEER_LIST') "
			+ "or hasAuthority('HELPDESK_APPLICATION_VIA_PHONE') or hasAuthority('HELP_DESK_NODAL_OFFICER') or hasAuthority('HELPDESK_FIELD_ENGINEER_LIST') "
			+ "or hasAuthority('GRIEVANCE_MASTER_WORKFLOW') or hasAuthority('HELP_DESK_WORKFLOW') or hasAuthority('GRIEVANCE_INSPECTING_OFFICER') or hasAuthority('GRIEVANCE_EXCISE_OFFICER') "
			+ "or hasAuthority('GRIEVANCE_REGISTERATION') or hasAuthority('HELP_DESK_GROUP')";
	public static final String GRIEVANCE_REGISTERATION = "hasAuthority('GRIEVANCE_REGISTERATION')";
	public static final String HELP_DESK_SUB_CATEGORY = "hasAuthority('HELP_DESK_SUB_CATEGORY')";
	public static final String HELP_DESK_CATEGORY = "hasAuthority('HELP_DESK_CATEGORY')";
	public static final String HELP_DESK_ASSET_TYPE = "hasAuthority('HELP_DESK_ASSET_TYPE')";
	public static final String HELP_DESK_ASSET_BRAND = "hasAuthority('HELP_DESK_ASSET_BRAND')";
	public static final String HELP_DESK_MAP_ASSET_TYPE_AND_BRAND = "hasAuthority('HELP_DESK_MAP_ASSET_TYPE_AND_BRAND')";
	public static final String HELP_DESK_PROBLEM_REPORTED = "hasAuthority('HELP_DESK_PROBLEM_REPORTED')";
	public static final String HELP_DESK_ACTUAL_PROBLEM = "hasAuthority('HELP_DESK_ACTUAL_PROBLEM')";
	public static final String HELP_DESK_ACTION_TAKEN = "hasAuthority('HELP_DESK_ACTION_TAKEN')";
	public static final String HELP_DESK_FAQ = "hasAuthority('HELP_DESK_FAQ')";
	public static final String HELP_DESK_ISSUE_DETAILS = "hasAuthority('HELP_DESK_ISSUE_DETAILS')";
	public static final String HELP_DESK_WORKFLOW = "hasAuthority('HELP_DESK_WORKFLOW')";
	public static final String SHIFT_WORKING_DAYS = "hasAuthority('SHIFT_WORKING_DAYS')";
	public static final String HELP_DESK_SLA = "hasAuthority('HELP_DESK_SLA')";
	public static final String HELP_DESK_PRIORITY = "hasAuthority('HELP_DESK_PRIORITY')";
	public static final String HELP_DESK_ISSUE_FROM = "hasAuthority('HELP_DESK_ISSUE_FROM')";
	public static final String SHIFT_CONFIGURATION = "hasAuthority('SHIFT_CONFIGURATION')";
	public static final String HELP_DESK_TICKET_STATUS = "hasAuthority('HELP_DESK_TICKET_STATUS')";
	public static final String GRIEVANCE_FAQ = "hasAuthority('GRIEVANCE_FAQ')";
	public static final String GRIEVANCE_MASTER_TYPE_OF_USER = "hasAuthority('GRIEVANCE_MASTER_TYPE_OF_USER')";
	public static final String GRIEVANCE_PRIORITY = "hasAuthority('GRIEVANCE_PRIORITY')";
	public static final String GRIEVANCE_MASTER_WORKFLOW = "hasAuthority('GRIEVANCE_MASTER_WORKFLOW')";
	public static final String GRIEVANCE_SLA = "hasAuthority('GRIEVANCE_SLA')";
	public static final String GRIEVANCE_ESCALATION_WORKFLOW = "hasAuthority('GRIEVANCE_ESCALATION_WORKFLOW')";
	public static final String POS_ASSET_REPORT = "hasAuthority('POS_ASSET_REPORT')";
}
