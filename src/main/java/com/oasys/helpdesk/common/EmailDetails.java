package com.oasys.helpdesk.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.oasys.helpdesk.utility.CommonUtil;



public class EmailDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String from;

	private List<String> to;

	private List<String> cc;

	private String subject;

	private String message;
	
	private boolean isHtml;
	
	private String langCode;
	
	private String event;
	
	private String subEvent;
	
	private List<EmailAttachment> attachmentList;
	
	private Map<String,Object> inputMap;

	public EmailDetails() {
		this.to = new ArrayList<String>();
		this.cc = new ArrayList<String>();
	}

	public EmailDetails(String from, String toList, String subject, String message) {
		this();
		this.from = from;
		this.subject = subject;
		this.message = message;
		this.to.addAll(Arrays.asList(splitByComma(toList)));
	}

	public EmailDetails(String from, String toList, String ccList, String subject, String message) {
		this();
		this.from = from;
		this.subject = subject;
		this.message = message;
		this.to.addAll(Arrays.asList(splitByComma(toList)));
		this.cc.addAll(Arrays.asList(splitByComma(ccList)));
	}
	
	public EmailDetails(String toList, String ccList, String subject, String message,Boolean isHMTL) {
		this();
		this.subject = subject;
		this.message = message;
		this.to.addAll(Arrays.asList(splitByComma(toList)));
		if(ccList!=null && !ccList.isEmpty()){
			this.cc.addAll(Arrays.asList(splitByComma(ccList)));
		}
		this.isHtml = isHMTL;
	}
	
	public EmailDetails(String from,String toList, String ccList, String subject, String message,String langCode,String event,Boolean isHMTL) {
		this();
		this.from = from;
		this.subject = subject;
		this.message = message;
		this.to.addAll(Arrays.asList(splitByComma(toList)));
		if(ccList!=null && !ccList.isEmpty()){
			this.cc.addAll(Arrays.asList(splitByComma(ccList)));
		}
		this.isHtml = isHMTL;
		this.langCode = langCode;
		this.event=event;
	}

	public EmailDetails(String toList, String ccList, String event,String subEvent,Map<String,Object> inputMap,String langCode,Boolean isHMTL) {
		this();
		this.to.addAll(Arrays.asList(splitByComma(toList)));
		if(ccList!=null && !ccList.isEmpty()){
			this.cc.addAll(Arrays.asList(splitByComma(ccList)));
		}
		this.event = event;
		this.subEvent = subEvent;
		this.inputMap = inputMap;
		this.langCode = langCode;
		this.isHtml = isHMTL;
	}
	
	public EmailDetails(String emailFrom, List<String> to, List<String> cc, String subject, String message,
			String language,String event, String subEvent, boolean isHMTL) {
		this();
		this.from = emailFrom;
		this.to = to;
		this.cc = cc;
		this.subject = subject;
		this.message = message;
		this.event = event;
		this.subEvent = subEvent;
		this.langCode = language;
		this.isHtml = isHMTL;
	}
	
        //getters and setters not mentioned for brevity

	private String[] splitByComma(String toMultiple) {
		String[] toSplit = toMultiple.split(",");
		return toSplit;
	}

	public String getToAsList() {
		return CommonUtil.concatenate(this.to, ",");
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public List<String> getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(List<String> to) {
		this.to = to;
	}

	/**
	 * @return the cc
	 */
	public List<String> getCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the isHtml
	 */
	public boolean getIsHtml() {
		return isHtml;
	}

	/**
	 * @param isHtml the isHtml to set
	 */
	public void setIsHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return the attachmentList
	 */
	public List<EmailAttachment> getAttachmentList() {
		return attachmentList;
	}

	/**
	 * @param attachmentList the attachmentList to set
	 */
	public void setAttachmentList(List<EmailAttachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	/**
	 * @return the inputMap
	 */
	public Map<String, Object> getInputMap() {
		return inputMap;
	}

	/**
	 * @param inputMap the inputMap to set
	 */
	public void setInputMap(Map<String, Object> inputMap) {
		this.inputMap = inputMap;
	}

	/**
	 * @return the subEvent
	 */
	public String getSubEvent() {
		return subEvent;
	}

	/**
	 * @param subEvent the subEvent to set
	 */
	public void setSubEvent(String subEvent) {
		this.subEvent = subEvent;
	}
}
