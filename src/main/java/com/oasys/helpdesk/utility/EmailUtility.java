package com.oasys.helpdesk.utility;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import com.oasys.helpdesk.dto.ContentPojo;

public class EmailUtility {

	/*
	 * public Set<String> getAttachemnt(Message message,String messageID,String
	 * refId) throws MessagingException ,IOException { Set<String> filePath=new
	 * LinkedHashSet<String>(); MimeMultipart multiPart = (MimeMultipart)
	 * message.getContent(); int numberOfParts = multiPart.getCount(); for (int
	 * partCount = 0; partCount < numberOfParts; partCount++) { MimeBodyPart part =
	 * (MimeBodyPart) multiPart.getBodyPart(partCount); if
	 * (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) { try
	 * {System.out.println("contentType::"+part.getContentType()); String
	 * fileDesPath=saveFile(refId, part); filePath.add(fileDesPath); }
	 * catch(Exception ex) {
	 * log.error("Error in saving file messageId="+messageID+",refId="+refId+
	 * ",fileName="+part.getFileName()); } } }
	 * 
	 * return filePath; }
	 */

	/*
	 * private String saveInlineFile(String messageID,InputStream is,String
	 * contentType) { try {
	 * 
	 * String destDir=
	 * emailconfig.getProperty("FILE_PATH")+File.separator+modifyMessageId(messageID
	 * ); File destFileDir = new
	 * File(emailconfig.getProperty("FILE_PATH")+File.separator+modifyMessageId(
	 * messageID)); if(!destFileDir.exists()) { destFileDir.mkdirs(); } String
	 * name=System.currentTimeMillis()+".png"; String
	 * destFilePath=destDir+File.separator+name; try(FileOutputStream output = new
	 * FileOutputStream(destFilePath);InputStream input = is) {
	 * 
	 * 
	 * byte[] buffer = new byte[4096]; int byteRead; while ((byteRead =
	 * input.read(buffer)) != -1) { output.write(buffer, 0, byteRead); } }
	 * System.out.println("contentType inline::"+contentType); return
	 * modifyMessageId(messageID)+File.separator+ name+";ContentType="+contentType;
	 * } catch(Exception ex) {
	 * log.error("Error in saving inline file messageId="+messageID,ex); return
	 * null; }
	 * 
	 * }
	 */
	/*
	 * private String saveFile(String msgId,Part part) throws Exception {
	 * 
	 * String
	 * destDir=emailconfig.getProperty("FILE_PATH")+File.separator+modifyMessageId(
	 * msgId); File destFileDir = new
	 * File(emailconfig.getProperty("FILE_PATH")+File.separator+modifyMessageId(
	 * msgId)); if(!destFileDir.exists()) { destFileDir.mkdirs(); }
	 * 
	 * String destFilePath=destDir+File.separator+ part.getFileName();
	 * try(FileOutputStream output = new FileOutputStream(destFilePath);InputStream
	 * input = part.getInputStream()) {
	 * 
	 * 
	 * byte[] buffer = new byte[4096]; int byteRead; while ((byteRead =
	 * input.read(buffer)) != -1) { output.write(buffer, 0, byteRead); } } return
	 * modifyMessageId(msgId)+File.separator+
	 * part.getFileName()+";ContentType="+part.getContentType();
	 * 
	 * }
	 */
	public static String modifyMessageId(String msgId) {
		msgId = msgId.replaceAll("\\<", "");
		msgId = msgId.replaceAll("\\>", "");
		return msgId.split("\\@")[0];
	}

	public static Address[] parseRecipients(Set<String> repctList) {

		try {
			if (repctList == null || repctList.isEmpty()) {
				return null;
			}
			Address[] addresses = new Address[repctList.size()];
			int index = 0;
			for (String rept : repctList) {

				int index1 = rept.indexOf("<");
				int inde2 = rept.indexOf(">");

				if (index1 != -1 && inde2 != -1) {
					rept = rept.substring(index1 + 1, inde2);

				}
				addresses[index] = new InternetAddress(rept);
				index++;

			}

			return addresses;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static ContentPojo getContent(Message message) throws MessagingException, IOException, Exception {
		ContentPojo contentPojo = new ContentPojo();
		if (message.isMimeType("text/plain")) {
			contentPojo.setContent(message.getContent().toString());
			return contentPojo;
		} else if (message.isMimeType("text/html")) {
			contentPojo.setContent(message.getContent().toString());
			return contentPojo;

		} else if (message.isMimeType("multipart/*")) {

			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			return getTextFromMimeMultipart(mimeMultipart, getMessgeID(message));
		}

		return contentPojo;
	}

	private static boolean isImage(String contentType) {
		if (contentType.split("/")[0].equalsIgnoreCase("IMAGE")) {
			return true;
		} else {
			return false;
		}
	}

	private static ContentPojo getTextFromMimeMultipart(MimeMultipart mimeMultipart, String messageId)
			throws MessagingException, IOException {
		StringBuilder result = new StringBuilder();
		int count = mimeMultipart.getCount();
		boolean isMultiPart = false;
		ContentPojo contentPojo = new ContentPojo();
		for (int i = 0; i < count; i++) {
			Part bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/html") && isMultiPart == false) {
				result.append(bodyPart.getContent());
				break;
			}
			/*
			 * else if(bodyPart.isMimeType("text/plain")&&isMultiPart==false) { result =
			 * result.append("\n").append(bodyPart.getContent()); //break; }
			 */
			else if (bodyPart.getContent() instanceof MimeMultipart) {

				// isMultiPart=true;
				ContentPojo contentPojo2 = readDataFromMultiPart(bodyPart, messageId);
				contentPojo.getAttachments().addAll(contentPojo2.getAttachments());
				result.append(contentPojo2.getContent());

				// result =
				// result.append(getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent()));
			} else if (bodyPart.getContent() instanceof InputStream) {
				InputStream inStream = (InputStream) bodyPart.getContent();

				// this check tell us image is inline image(pasted image not attached)
				// it can be fail if user send image in attachment with name (image.png)
				System.out.println("Content-type" + bodyPart.getContentType());

				if (bodyPart.getContentType().toLowerCase().indexOf("name=image.png") != -1
						|| bodyPart.getContentType().toLowerCase().indexOf("boundary") != -1) {
					String imagePath = "";// saveInlineFile(messageId,inStream,bodyPart.getContentType());
					contentPojo.getAttachments().add(imagePath);

					String div = "<br><div><img src='" + "{{inlineUrl}}/" + imagePath + "'/><div>";
					result.append(div);
				}

			}
		}
		contentPojo.setContent(result.toString());
		return contentPojo;
	}

	public static ContentPojo readDataFromMultiPart(Part bodyPart, String messageId)
			throws MessagingException, IOException {
		ContentPojo contentPojo = new ContentPojo();
		StringBuilder result = new StringBuilder();
		MimeMultipart mimeMultipart1 = (MimeMultipart) bodyPart.getContent();
		for (int j = 0; j < mimeMultipart1.getCount(); j++) {
			Part bodyPart1 = mimeMultipart1.getBodyPart(j);

			if (bodyPart1.isMimeType("text/html")) {
				result.append(bodyPart1.getContent());
				break;
			} else if (bodyPart1.isMimeType("text/plain")) {
				String content = (String) bodyPart1.getContent();
				result = result.append("<br>").append(content.replaceAll("\\n", "<br>"));
				break;
			} else if (bodyPart1.getContent() instanceof MimeMultipart) {
				ContentPojo contentPojo2 = readDataFromMultiPart(bodyPart1, messageId);
				contentPojo.getAttachments().addAll(contentPojo2.getAttachments());
				result.append(contentPojo2.getContent());
			} else if (bodyPart1.getContent() instanceof InputStream) {
				InputStream inStream = (InputStream) bodyPart1.getContent();
				// this check tell us image is inline image(pasted image not attached)
				// it can be fail if user send image in attachment with name (image.png)
				if (bodyPart.getContentType().toLowerCase().indexOf("name=image.png") != -1
						|| bodyPart.getContentType().toLowerCase().indexOf("boundary") != -1) {
					String imagePath = "";// saveInlineFile(messageId,inStream,bodyPart.getContentType());
					contentPojo.getAttachments().add(imagePath);
					String div = "<br><div><img src='" + "{{inlineUrl}}/" + imagePath + "'/><div>";
					result.append(div);
				}

			}

		}
		contentPojo.setContent(result.toString());
		return contentPojo;
	}

	public static String getMessgeID(Message message) throws MessagingException {

		String values[] = message.getHeader("Message-ID");

		if (values != null) {

			return values[0];

		}

		return "";
	}

	public static String convertMailTime(Date date) {

		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy 'at' hh:mm:ss a");
			return dateFormat.format(date);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Long getTimeInMs(String date) {

		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy 'at' hh:mm:ss a");
			return dateFormat.parse(date).getTime();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}

	public static Set<String> getRecipients(Address[] addresses) {
		Set<String> tempList = new LinkedHashSet<String>();
		try {
			if (addresses == null || addresses.length == 0) {
				return null;
			}
			for (Address address : addresses) {

				tempList.add(address.toString());
			}
			return tempList;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String getHedaers(Message message, String key) throws Exception {

		String values[] = message.getHeader(key);

		if (values != null) {

			return values[0];

		}
		return null;
	}

}
