package com.oasys.helpdesk.common;


import java.io.Serializable;



public class EmailAttachment implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private byte[] attachments;
	
	private String fileName;
	
	private String fileExtension = ".xlsx";//default name
	
	public EmailAttachment(){
		super();
	}

	public EmailAttachment(byte[] attachments,String fileName){
		super();
		this.attachments = attachments;
		this.fileName = fileName;
	}
	
	/**
	 * @return the attachments
	 */
	public byte[] getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(byte[] attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileExtension
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * @param fileExtension the fileExtension to set
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
}