package com.oasys.helpdesk.service;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.EmailTemplate;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TicketCountResponseDTO;
import com.oasys.helpdesk.entity.EmailRequest;
import com.oasys.helpdesk.entity.IssueFromEntity;
import com.oasys.helpdesk.entity.SentEmailRequest;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.CreateTicketRepository;
import com.oasys.helpdesk.repository.EmailRequestRepository;
import com.oasys.helpdesk.repository.IncomingSmsRepository;
import com.oasys.helpdesk.repository.IssueFromRepository;
import com.oasys.helpdesk.repository.SentEmailRequestRepository;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.request.AcknowledgeAndOthersEmailDTO;
import com.oasys.helpdesk.request.BaseTemplateMailDTO;
import com.oasys.helpdesk.request.EmailRequestCreationDto;
import com.oasys.helpdesk.request.EmailRequestDto;
import com.oasys.helpdesk.request.ReplyMailRequest;
import com.oasys.helpdesk.response.EmailRequestResponseDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.oasys.helpdesk.constant.Constant.*;
import static com.oasys.helpdesk.service.EmailRequestServiceSupport.performMandatoryCheck;
import static java.util.Objects.isNull;


@Service
@Log4j2
public class EmailRequestService {

    @Autowired
    private EmailRequestRepository emailRequestRepository;

    @Autowired
    private EmailRequestServiceSupport emailRequestServiceSupport;

	@Autowired
	private PaginationMapper paginationMapper;
	
	@Autowired
	private TicketStatusrepository ticketStatusrepository;
	
	@Autowired
	private CreateTicketRepository createTicketRepository;
	
	@Autowired
	private IssueFromRepository  issueFromRepository;

	@Autowired
    private JavaMailSender javaMailSender;
	
	
	@Autowired
	private IncomingSmsRepository incomingsmsrepository;
	
	
	@Autowired
	private SentEmailRequestRepository sentEmailRequestRepository;
	
	@Value("${sent.from.mail.username}")
  	private String sentfromEmailId;

    public GenericResponse getAllEmailRequest() {
       // List<EmailRequest> emailRequestList = emailRequestRepository.findAll();
        List<EmailRequest> emailRequestList = emailRequestRepository.findAllByOrderByModifiedDateDesc();
        if (!emailRequestList.isEmpty()) {
            List<EmailRequestResponseDto> EmailRequestResponseDtoList = new ArrayList<>();
            emailRequestList.forEach(pt -> EmailRequestResponseDtoList.add(emailRequestServiceSupport.toEmailResponseDTO(pt)));

            return Library.getSuccessfulResponse(EmailRequestResponseDtoList,
                    ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
        } else {
            throw new RecordNotFoundException();
        }
    }

    public GenericResponse getEmailRequestById(Long id) throws RecordNotFoundException {
        EmailRequest emailRequest = emailRequestRepository.getById(id);
        if (emailRequest != null && emailRequest.getId() != null) {
            return Library.getSuccessfulResponse(emailRequestServiceSupport.toEmailResponseDTO(emailRequest), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
                    ErrorMessages.RECORED_FOUND);
        } else {
            throw new RecordNotFoundException();
        }
    }

    public GenericResponse getEmailBySenderId(String emailId) throws RecordNotFoundException {
        Optional<EmailRequest> emailRequestOptional = emailRequestRepository.findByFromEmailId(emailId);
        if (emailRequestOptional.isPresent()) {
            return Library.getSuccessfulResponse(emailRequestServiceSupport.toEmailResponseDTO(emailRequestOptional.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
                    ErrorMessages.RECORED_FOUND);
        } else {
            throw new RecordNotFoundException();
        }
    }

    public GenericResponse searchEmailRequest(EmailRequestDto emailRequestDto) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String from = dateFormat.format(emailRequestDto.getFromDate()) + " 00:00:00";
        String to = dateFormat.format(emailRequestDto.getToDate()) + " 23:59:59";
        List<EmailRequest> emailRequestList = emailRequestRepository.getEmailbyBetweenDates(from, to, emailRequestDto.getFromEmailId());

        if (!emailRequestList.isEmpty()) {
			List<EmailRequestResponseDto> EmailRequestResponseDtoList = new ArrayList<>();
			emailRequestList.forEach(pt ->
					EmailRequestResponseDtoList.add(emailRequestServiceSupport.toEmailResponseDTO(pt))
			);

			return Library.getSuccessfulResponse(EmailRequestResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
            throw new RecordNotFoundException();
        }
    }

    public GenericResponse createEmailRequest(EmailRequestCreationDto emailRequestDto) {
        Optional<EmailRequest> applicationNo = emailRequestRepository
                .findByApplicationNo(emailRequestDto.getApplicationNo());
        if (applicationNo.isPresent()) {
            return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
                    ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[]{APPLICATION_ID}));
        }

        GenericResponse mandatoryCheck = performMandatoryCheck(emailRequestDto);
        if (mandatoryCheck != null)
            return mandatoryCheck;

        EmailRequest entity = emailRequestRepository.save(emailRequestServiceSupport.toEmailRequestEntity(emailRequestDto));
        return Library.getSuccessfulResponse(emailRequestServiceSupport.toEmailResponseDTO(entity), ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
    }

	public GenericResponse getByEmailAndDate(PaginationRequestDTO paginationDto) {

		Pageable pageable;
		Page<EmailRequest> list;
		String emailId = null;
		Date fromDate = null;
		Date toDate = null;

		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Sort.Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Sort.Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get(FROM_EMAIL_ID))
					&& !paginationDto.getFilters().get(FROM_EMAIL_ID).toString().trim().isEmpty()) {
				emailId = String.valueOf(paginationDto.getFilters().get(FROM_EMAIL_ID));
			}
			try {
				if (Objects.nonNull(paginationDto.getFilters().get(FROM_DATE))
						&& !paginationDto.getFilters().get(FROM_DATE).toString().trim().isEmpty()) {
					fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(paginationDto.getFilters().get(FROM_DATE)));
				}

				if (Objects.nonNull(paginationDto.getFilters().get(TO_DATE))
						&& !paginationDto.getFilters().get(TO_DATE).toString().trim().isEmpty()) {

					toDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(paginationDto.getFilters().get(TO_DATE)));

				}
			} catch (ParseException e) {
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[]{FROM_DATE, TO_DATE}));
			}

		}
		list = getByFilter(emailId, fromDate, toDate, pageable);
		if (isNull(list)) {
			list = emailRequestRepository.getAll(pageable);
		}
		if (isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<EmailRequestResponseDto> finalResponse = list.map(k -> emailRequestServiceSupport.toEmailResponseDTO(k));
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	private Page<EmailRequest> getByFilter(String emailId, Date fromDate, Date toDate, Pageable pageable) {
		Page<EmailRequest> list = null;
		if (Objects.nonNull(emailId) && Objects.nonNull(fromDate) && Objects.nonNull(toDate)) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String from = dateFormat.format(fromDate) + " 00:00:00";
			String to = dateFormat.format(toDate) + " 23:59:59";

			list = emailRequestRepository.getByEmailIdAndDateRange(from, to, emailId, pageable);
		}
		
		if (Objects.isNull(emailId) && Objects.nonNull(fromDate)) {
			list = emailRequestRepository.getByDateRange(fromDate, pageable);
		}

		if (Objects.nonNull(emailId) && Objects.isNull(fromDate) && Objects.isNull(toDate)) {
			list = emailRequestRepository.getByFromEmailId(emailId, pageable);
		}
		
	

		return list;
	}

	public GenericResponse getCount(String date,String todate,String issueFrom, AuthenticationDTO authenticationDTO) {
	
		final Date fromDate;
		final Date toDate;
		try {
			fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		
		try {
			toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(todate + " " + "00:00:00");
		} catch (ParseException e) {
			log.error("error occurred while parsing date : {}", e.getMessage());
			throw new InvalidDataValidation("Invalid date parameter passed");
		}
		
		
		List<TicketCountResponseDTO> ticketCountResponseDTOList = new ArrayList<>();
		Integer emailRequest = emailRequestRepository.getNewEmail(date);
		Integer smscount=incomingsmsrepository.getCountByCreatedDateBetween(fromDate, toDate);
		List<TicketStatusEntity> ticketStatusList = ticketStatusrepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(ticketStatusList)) {
			throw new RecordNotFoundException("No Record Found");
		}
		ticketStatusList.forEach(ticketStatus -> {
			if (Objects.isNull(ticketStatus.getId()) || StringUtils.isBlank(ticketStatus.getTicketstatusname())) {
				return;
			}
			TicketCountResponseDTO ticketCountResponseDTO = new TicketCountResponseDTO();
			ticketCountResponseDTO.setStatus(ticketStatus.getTicketstatusname());
			if(Objects.isNull(authenticationDTO) || Objects.isNull(authenticationDTO.getUserId())) {
				log.error("invalid authentication details : {}", authenticationDTO);
				throw new RecordNotFoundException("No Record Found");
			}
			
			Optional<IssueFromEntity> issueFromMaster = issueFromRepository.findByIssueFromIgnoreCase(issueFrom);
		
			if(!issueFromMaster.isPresent())
			{
				log.error("invalid issue from details : {}", issueFrom);
				throw new RecordNotFoundException("No Record Found");
			}
			IssueFromEntity entity = issueFromMaster.get();
			ticketCountResponseDTO
			.setCount(createTicketRepository.getCountByStatusAndCreatedByAndCreatedDateAndIssueFrom(ticketStatus.getId(), authenticationDTO.getUserId(),entity.getId(),fromDate,toDate));
			ticketCountResponseDTO.setNewEmailCount(emailRequest);
			ticketCountResponseDTO.setSmsCount(smscount);
			
			ticketCountResponseDTOList.add(ticketCountResponseDTO);
		});
		return Library.getSuccessfulResponse(ticketCountResponseDTOList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	
	public GenericResponse replyMail(ReplyMailRequest sentMailRequest) {
		Optional<EmailRequest> emOptional = emailRequestRepository.findById(sentMailRequest.getEmailId());

		if (!emOptional.isPresent()) {
			throw new RecordNotFoundException();
		}
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(sentfromEmailId);
		message.setTo(emOptional.get().getFromEmailId());
		message.setSubject(emOptional.get().getSubject());
		message.setText(sentMailRequest.getEmailBody());
		javaMailSender.send(message);
		SentEmailRequest sentEmailRequest = new SentEmailRequest(
				CommonUtil.utf8EncodeString(sentMailRequest.getEmailBody()), emOptional.get().getFromEmailId(),
				emOptional.get().getId());
		sentEmailRequest = sentEmailRequestRepository.save(sentEmailRequest);
		
		return Library.getSuccessfulResponse(emailRequestServiceSupport.toSentEmailResponseDTO(sentEmailRequest),
				ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
		
		
		

	}
	
	public GenericResponse replyEmailWithTemplate(BaseTemplateMailDTO baseTemplateMailDTO , EmailTemplate emailTemplate) {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try 
		{
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setSubject(baseTemplateMailDTO.getSubject());
			mimeMessageHelper.setFrom(sentfromEmailId);
			mimeMessageHelper.setTo(baseTemplateMailDTO.getToId());
			mimeMessageHelper.setText(emailRequestServiceSupport.geContentFromTemplate(baseTemplateMailDTO,emailTemplate), true);
			javaMailSender.send(mimeMessage);
			return Library.getSuccessfulResponse(null,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

}
