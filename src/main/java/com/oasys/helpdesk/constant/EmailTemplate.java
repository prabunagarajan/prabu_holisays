package com.oasys.helpdesk.constant;

public enum EmailTemplate {
	ACKNOWLEDGE_RECEIPT("acknowledge-receipt.flth","Acknowledge Receipt"), ASK_MORE_INFORMATION("ask-more-information.flth","Asking for More Information"),
	FEEDBACK_TICKET("feedback-ticket.flth","Asking for Feedback"), FOLLOW_NEGTIVE_FEEDBACK("follow-negtive-feedback.flth","Follow-up on Negative Feedback"),
	FOLLOW_POSTIVE_FEEDBACK("follow-postive-feedback.flth","Follow-up on Positive Feedback"), RESOLVING_TICKET("resolving-ticket.flth","Resolving a Ticket"),
	UPDATE_TICKET_PROCESS("update-ticket-process.flth","Updating the Ticket Progress");

	private String fileName;
	
	private String subject;

	EmailTemplate(String filName,String subject) {
		this.fileName = filName;
		this.subject = subject;
	}

	public String getFileName() {
		return fileName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	
}
