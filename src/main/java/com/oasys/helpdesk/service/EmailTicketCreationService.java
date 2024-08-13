package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.service.EmailRequestServiceSupport.getUniqueApplicationCode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.dto.ContentPojo;
import com.oasys.helpdesk.entity.EmailRequest;
import com.oasys.helpdesk.repository.EmailRequestRepository;
import com.oasys.helpdesk.utility.EmailUtility;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class EmailTicketCreationService implements MessageHandler {

	@Autowired
	private EmailRequestRepository emailReqRepo;


	@Getter
	@Setter
	private byte[] buffer;

	@Value("${pop3.email.service.action}")
	boolean emailServiceAction;


	@Override
	
	public void handleMessage(Message<?> message) throws MessagingException {

		if (!emailServiceAction) {
			log.info("----------Email service is switched off--------");
			return;
		}
		log.info(">>>>>>>> HandleMessage method is Started >>>>>>>>>");
		log.debug("Message received -" + message);

		if(! (message.getPayload() instanceof javax.mail.Message))
		{
			log.info("Message received not a type of - javax.mail.Message");
			return;
		}
		javax.mail.Message msg = (javax.mail.Message) message.getPayload();

		try (Folder folder = msg.getFolder();) {

			EmailRequest eTCRequest = new EmailRequest();
			folder.open(Folder.READ_WRITE);
			javax.mail.Message[] messages = folder.getMessages();
			for (javax.mail.Message msg1 : messages) {
				eTCRequest.setSubject(msg1.getSubject());
				Address[] froms = msg1.getFrom();
				String email = ((InternetAddress) froms[0]).getAddress();

				Address[] replyTo = msg1.getReplyTo();
				String replyToStr = ((InternetAddress) replyTo[0]).getAddress();
				Address[] bcc = msg1.getRecipients(javax.mail.Message.RecipientType.BCC);
				String bccStr = ((InternetAddress) replyTo[0]).getAddress();
				Address[] cc = msg1.getRecipients(javax.mail.Message.RecipientType.CC);
				String ccStr = ((InternetAddress) replyTo[0]).getAddress();

				eTCRequest.setCcEmailList(ccStr);
				eTCRequest.setBccEmailList(bccStr);
				eTCRequest.setToEmailidList(replyToStr);
				eTCRequest.setFromEmailId(email);
				eTCRequest.setActive(true);
				eTCRequest.setValidEmail(true);
				eTCRequest.setPriority("High");
				eTCRequest.setApplicationNo(getUniqueApplicationCode());
				String contentType = msg1.getContentType();
				log.debug("contenttype>>>>>>>>>> is " + contentType);
				ContentPojo contentPojo = EmailUtility.getContent(msg1);
				byte[] bytes = contentPojo.getContent().getBytes(StandardCharsets.UTF_8);
				String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8);
				eTCRequest.setEmailBody(utf8EncodedString);
				eTCRequest = emailReqRepo.save(eTCRequest);
				log.info("Email request information saved sucessfully ");
				msg1.setFlag(Flags.Flag.DELETED, true);
			}

		} catch (javax.mail.MessagingException e) {
			log.error("Error in Saving Email Request ", ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
	
			log.error("Error in Saving Email Request ", ExceptionUtils.getStackTrace(e));
		}
		log.debug(" <<<<<<< HandleMessage method is End <<<<<<<");
	}

	/*
	 * This method checks for content-type based on which, it processes and
	 * fetches the content of the message
	 */

	public String readEmailBody(Part part) throws javax.mail.MessagingException,
			IOException {
		String bodyContent = null;
		log.debug("CONTENT-TYPE: " + part.getContentType());
		log.debug("CONTENT: " + part.getContent());
		if (part.isMimeType("text/plain")) {
			log.debug("This is plain text");
			String s = (String) part.getContent();
			// byte[] byteArray = s.getBytes();
			// Byte[] b=(Byte[]) part.getContent();
			log.debug("Email Body Content is >>>>>>>>>>>>>>>>>>>> " + s);
			bodyContent = s.replaceAll("\\[.*?\\]", "");
			log.debug("Email Body Content is >>>>>>>>>>>>>>>>>>>> "
					+ bodyContent);

		}
		// check if the content has attachment
		else if (part.isMimeType("multipart/*")) {
			log.debug("This is a Multipart");
			log.debug("---------------------------");
			Multipart mp = (Multipart) part.getContent();
			int count = mp.getCount();
			log.debug("count is >>>>>>>>>>>>>> " + count);
			for (int i = 0; i < count; i++) {
				log.debug(" part cout " + i);
				bodyContent = readEmailBody(mp.getBodyPart(i));
			}
		}
		// check if the content is an inline image
		else if (part.isMimeType("image/jpeg")) {
			log.debug("--------> image/jpeg >>>>>>>>>>>>>>>>>>>>");
			Object o = part.getContent();

			InputStream x = (InputStream) o;
			// Construct the required byte array

		} else if (part.getContentType().contains("image/")) {
			log.debug("<<<<<<<<<<< content type >>>>>>>>>>>>>>>>"
					+ part.getContentType());

		} else if (part.isMimeType("text/html")) {
			log.debug("This is html text");
			String s = (String) part.getContent();
			log.debug("Email Body Content is >>>>>>>>>>>>>>>>>>>> " + s);
			bodyContent = s;
			log.debug("Email Body Content is >>>>>>>>>>>>>>>>>>>> "
					+ bodyContent);
		} else {
			Object o = part.getContent();
			if (o instanceof String) {
				log.debug("This is a string >>>>>>>>>>>>>> ");
				log.debug("---------------------------");
				log.debug((String) o);
			} else if (o instanceof InputStream) {
				log.debug("This is just an input stream >>>>>>>>>>>>>>>> ");
				log.debug("---------------------------");
				InputStream is = (InputStream) o;
				is = (InputStream) o;
				int c;
				while ((c = is.read()) != -1)
					log.debug(c);
			} else {
				log.info("This is an unknown type");
				log.info("---------------------------");
				log.info(o.toString());
			}

		}
		return bodyContent; 
	}

	private String getText(Part p) throws MessagingException, IOException,
			javax.mail.MessagingException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			log.info("getText() >>>>>>>>>>>>>>>>>>>>>>>>");
			boolean textIsHtml = p.isMimeType("text/html");
			return s;
		}
		return null;
	}

	/*public EmailTicketRequestListDto getAllEmailRequests(
			EmaiTicketRequestSerachDto emailTicketSearchDto)
			throws ParseException {
		EmailTicketRequestListDto emailTicketRequestListDto = new EmailTicketRequestListDto();
		log.info("<---Get the Eamil Ticket Request Details Service started---->");

		log.info(emailTicketSearchDto);
		try {
			int page = emailTicketSearchDto.getPage();
			int size = emailTicketSearchDto.getSize();
			Pageable pageable = new PageRequest(page, size);
			Page<EmailTicketCreationRequest> resultPage = null;
			List<EmailTicketCreationRequest> result = null;
			Date toDate = emailTicketSearchDto.getToDate();

			log.info("From Date is >>>>>>>>>>>>> "
					+ emailTicketSearchDto.getFromDate());
			log.info("To Date is >>>>>>>>>>>>> "
					+ emailTicketSearchDto.getToDate() + "TODAY DATE IS "
					+ new Date() + toDate);

			if (emailTicketSearchDto.getFromDate() == null) {
				resultPage = emailReqRepo.findAllRequest(pageable);
				result = resultPage.getContent();
				log.info("Total records are -:" + resultPage.getTotalElements());
			} else {
				if (emailTicketSearchDto.getFromDate() != null
						&& emailTicketSearchDto.getToDate() == null) {
					resultPage = emailReqRepo.search(
							emailTicketSearchDto.getFromDate(), new Date(),
							pageable);
					result = resultPage.getContent();
					log.info("Total records are -:"
							+ resultPage.getTotalElements());

				} else {
					toDate.setHours(23);
					toDate.setMinutes(59);
					toDate.setSeconds(59);
					resultPage = emailReqRepo.search(
							emailTicketSearchDto.getFromDate(), toDate,
							pageable);
					result = resultPage.getContent();
					log.info("Total records are -:"
							+ resultPage.getTotalElements());
				}

			}

			*//** Convert the Entity to Dto *//*
			emailTicketRequestListDto = ObjectConverter
					.emailTicketRequestListToRequestEmailsListDto(result);
			emailTicketRequestListDto.setCount(resultPage.getTotalElements());
		} catch (Exception e) {
			log.error("Exception -:", e);
			e.printStackTrace();
			emailTicketRequestListDto
					.setStatusCode(ErrorCodeDescription.ERROR_GENERIC
							.getErrorCode());
		}
		return emailTicketRequestListDto;
	}

	public EmailTicketRequestListDto getAllJunkEmailRequests(
			EmaiTicketRequestSerachDto emailTicketSearchDto)
			throws ParseException {
		EmailTicketRequestListDto emailTicketRequestListDto = new EmailTicketRequestListDto();
		log.info("<--Get the Junk Eamil Ticket Request Details Service started-->"
				+ emailTicketSearchDto);

		try {
			int page = emailTicketSearchDto.getPage();
			int size = emailTicketSearchDto.getSize();
			Pageable pageable = new PageRequest(page, size);
			Page<EmailTicketCreationRequest> resultPage = null;
			List<EmailTicketCreationRequest> result = null;
			Date toDate = emailTicketSearchDto.getToDate();

			log.info("From Date is >>>>>>>>>>>>> "
					+ emailTicketSearchDto.getFromDate());
			log.info("To Date is >>>>>>>>>>>>> "
					+ emailTicketSearchDto.getToDate() + "TODAY DATE IS "
					+ new Date() + toDate);

			if (emailTicketSearchDto.getFromDate() == null) {
				resultPage = emailReqRepo.findAllJunkRequest(pageable);
				result = resultPage.getContent();
				log.info("Total Junk Email records are -:"
						+ resultPage.getTotalElements());
			} else {
				if (emailTicketSearchDto.getFromDate() != null
						&& emailTicketSearchDto.getToDate() == null) {
					Date currentDate = new Date();
					currentDate.setHours(23);
					currentDate.setMinutes(59);
					currentDate.setSeconds(59);
					resultPage = emailReqRepo.searchJunkMails(
							emailTicketSearchDto.getFromDate(), currentDate,
							pageable);
					result = resultPage.getContent();
					log.info("Total Junk Email records are -:"
							+ resultPage.getTotalElements());

				} else {
					toDate.setHours(23);
					toDate.setMinutes(59);
					toDate.setSeconds(59);
					resultPage = emailReqRepo.searchJunkMails(
							emailTicketSearchDto.getFromDate(), toDate,
							pageable);
					result = resultPage.getContent();
					log.info("Total Junk Email records are -:"
							+ resultPage.getTotalElements());
				}

			}

			*//** Convert the Entity to Dto *//*
			emailTicketRequestListDto = ObjectConverter
					.emailTicketRequestListToRequestEmailsListDto(result);
			emailTicketRequestListDto.setCount(resultPage.getTotalElements());
		} catch (Exception e) {
			log.error("Exception -:", e);
			e.printStackTrace();
			emailTicketRequestListDto
					.setStatusCode(ErrorCodeDescription.ERROR_GENERIC
							.getErrorCode());
		}
		return emailTicketRequestListDto;
	}

	public TicketDto emailtRequestTicketCreation(TicketDto hdTicketDto) {

		log.info("<-----Email Request Ticket creation Service  started---->");
		TicketDto ticketDto = null;
		try {
			if (hdTicketDto != null) {
				long source = TicketSource.EMail.getEntityCode();
				hdTicketDto.setTicketSource(source);
				ticketDto = ticketService.creatTicket(hdTicketDto);
				if (ticketDto.getStatusCode() == 0) {
					log.info("EmailRequest Ticket creation Ticket Number :"
							+ ticketDto.getTicketNo());
					log.info(">>>>>>>>>>>>>>>>EmailRequset is<<<<<<<<<<<<<<<<<<<<<<< "
							+ hdTicketDto.getEmailRequestId());
					if (hdTicketDto.getEmailRequestId() != null) {
						EmailTicketCreationRequest emailRequest = emailReqRepo
								.findOne(hdTicketDto.getEmailRequestId());
						if (emailRequest != null) {
							emailRequest.setStatus(true);
							emailRequest.setTicketId(ticketDto.getId());
							emailReqRepo.saveAndFlush(emailRequest);
							log.info(">>>>>>>>>>>>>>>EmailTicketCreateRequest is updated successfully>>>>>>>>>>>>>>>>>");
						}
					}
				} else {
					log.info("EmailRequest ticket creation ticket is not created for the Request : "
							+ ticketDto);
					return ticketDto;
				}
			}
		} catch (Exception e) {
			log.error("Ticket Creation Service Error", e);
		}
		log.info("<-----Email Ticket creation Service  completed---->");
		return ticketDto;
	}

	public BaseDto moveJunkEmailToInbox(
			EmailTicketRequestListDto emailTicketRequestListDto) {
		log.info("Inside  EmailTicketCreationService  ---->    moveJunkEmailToInbox");
		BaseDto baseDto = new BaseDto();
		try {
			List<RequestEmailsDto> requestEmailsDtoList = emailTicketRequestListDto
					.getRequestEmailDtoList();
			for (RequestEmailsDto requestEmailsDto : requestEmailsDtoList) {
				EmailTicketCreationRequest emailTicketCreationRequest = emailReqRepo
						.findOne(requestEmailsDto.getId());
				emailTicketCreationRequest.setIsValid(true);
				emailTicketCreationRequest.setModifiedBy(requestEmailsDto
						.getLastModifiedby());
				emailTicketCreationRequest.setLastModifiedDate(new Date());
				emailReqRepo.save(emailTicketCreationRequest);
			}
			baseDto.setStatusCode(0);
		} catch (Exception e) {
			e.printStackTrace();
			baseDto.setStatusCode(ErrorCodeDescription.ERROR_GENERIC
					.getErrorCode());
		}

		return baseDto;
	}*/
}