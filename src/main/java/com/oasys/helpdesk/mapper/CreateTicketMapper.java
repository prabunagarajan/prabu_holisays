package com.oasys.helpdesk.mapper;

import static com.oasys.helpdesk.constant.Constant.ACTION_TAKEN_ID;
import static com.oasys.helpdesk.constant.Constant.ACTUAL_PROBLEM_ID;
import static com.oasys.helpdesk.constant.Constant.CATEGORYID;
import static com.oasys.helpdesk.constant.Constant.ISSUE_DETAILS;
import static com.oasys.helpdesk.constant.Constant.ISSUE_FROM_ID;
import static com.oasys.helpdesk.constant.Constant.KB_ID;
import static com.oasys.helpdesk.constant.Constant.KNOWLEDGE_SOLUTION;
import static com.oasys.helpdesk.constant.Constant.PRIORITYID;
import static com.oasys.helpdesk.constant.Constant.PROBLEM_REPORTED_ID;
import static com.oasys.helpdesk.constant.Constant.ROLE_ID;
import static com.oasys.helpdesk.constant.Constant.SLA_ID;
import static com.oasys.helpdesk.constant.Constant.SUB_CATEGORY_ID;
import static com.oasys.helpdesk.constant.Constant.TICKET_STATUS_ID;
import static com.oasys.helpdesk.constant.Constant.USER_ID;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.DurationDTO;
import com.oasys.helpdesk.entity.ActionTaken;
import com.oasys.helpdesk.entity.ActualProblem;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.CreateTicketEntitypayment;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.IssueFromEntity;
import com.oasys.helpdesk.entity.KnowledgeBase;
import com.oasys.helpdesk.entity.KnowledgeSolution;
import com.oasys.helpdesk.entity.PriorityMaster;
import com.oasys.helpdesk.entity.ProblemReported;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.entity.SlaMasterEntity;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.ActionTakenRepository;
import com.oasys.helpdesk.repository.AcutalProblemRepository;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.CreateTicketRepository;
import com.oasys.helpdesk.repository.IssueDetailsRepository;
import com.oasys.helpdesk.repository.IssueFromRepository;
import com.oasys.helpdesk.repository.KnowledgeRepository;
import com.oasys.helpdesk.repository.KnowledgeSolutionRepository;
import com.oasys.helpdesk.repository.PriorityMasterRepository;
import com.oasys.helpdesk.repository.ProblemReportedRepository;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.repository.SlaMasterRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.repository.UsergroupRepository;
import com.oasys.helpdesk.request.CreateTicketDashboardDTO;
import com.oasys.helpdesk.request.CreateTicketRequestDto;
import com.oasys.helpdesk.response.CreateTicketResponseDto;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class CreateTicketMapper {
	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private TicketStatusrepository ticketStatusrepository;

	@Autowired
	private IssueDetailsRepository issueDetailsRepository;

	@Autowired
	private IssueFromRepository issueFromRepository;

	@Autowired
	private SlaMasterRepository slaMasterRepository;

	@Autowired
	private KnowledgeRepository knowledgeRepository;

	@Autowired
	private UsergroupRepository usergroupRepository;

	@Autowired
	private PriorityMasterRepository priorityMasterRepository;

	@Autowired
	private AcutalProblemRepository actualProblemRepository;

	@Autowired
	private ActionTakenRepository actionTakenRepository;

	@Autowired
	private ProblemReportedRepository problemReportedRepository;

	@Autowired
	private RoleMasterRepository roleMasterRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private KnowledgeSolutionRepository knowledgeSolutionRepository;

	@Autowired
	CreateTicketRepository createTicketRepository;

	public CreateTicketResponseDto convertEntityToResponseDTO(CreateTicketEntity entity) {
		CreateTicketResponseDto responseDTO = commonUtil.modalMap(entity, CreateTicketResponseDto.class);
		String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());
		// String
		// firstname=commonDataController.getFirstNameById(entity.getCreatedBy());
		// responseDTO.setFirstName(firstname);
		// String mobileno=commonDataController.getmobileNoById(entity.getCreatedBy());
		// responseDTO.setFieldmobileNo(mobileno);
		/*
		 * String entityTypeMasterResponse = commonDataController
		 * .getMasterDropDownValueByKey(Constant.ENTITY_TYPE_DROPDOWN_KEY,
		 * entity.getEntityTypeId()); if (StringUtils.isBlank(entityTypeMasterResponse))
		 * { throw new
		 * InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
		 * .getMessage(new Object[] { Constant.ENTITY_TYPE_CODE })); }
		 */

		///////////////////////// existing one///////////////////////

//		LocalDateTime modifiedDate = entity.getModifiedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//		LocalDateTime createdDate = entity.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//				
//		//Duration duration = Duration.between(createdDate, modifiedDate);
//		
//		LocalDateTime tempDateTime = LocalDateTime.from( createdDate );
//		
//		long hours = tempDateTime.until( modifiedDate, ChronoUnit.HOURS );
//		tempDateTime = tempDateTime.plusHours( hours );
//
//		long minutes = tempDateTime.until( modifiedDate, ChronoUnit.MINUTES );
//		tempDateTime = tempDateTime.plusMinutes( minutes );
//		
//		 responseDTO.setDurationInHours(hours+"."+ minutes+" hours");

		////////////////////////////////////////////////////////////////////////////

		////////////// Newly add////////////////////////////////
		if (Objects.nonNull(entity.getTicketStatus())) {
			responseDTO.setTicketStatus(entity.getTicketStatus().getTicketstatusname());
			responseDTO.setTicketStatusId(entity.getTicketStatus().getId());
		}
//			
//			try {
//				String duration=entity.getDuration();
//				if ( entity.getTicketStatus().getTicketstatusname().equalsIgnoreCase("Closed")
//						|| entity.getTicketStatus().getTicketstatusname().equalsIgnoreCase("Resolved")) {
//					//if (duration !=null) {
//					if (duration !=null && !duration.isEmpty()) {
//						Long timestampduration = Long.valueOf(entity.getDuration());
//						  long hours = TimeUnit.SECONDS.toHours(timestampduration);
//					        long minutes = TimeUnit.SECONDS.toMinutes(timestampduration) % 60;
//					        String durationhrs=hours+"."+minutes+" hours";
//					        responseDTO.setDurationInHours(durationhrs.toString().trim());
//					        
//						//responseDTO.setDurationInHours(hours+"."+minutes+"hours".trim());
//					}

//				} else {
//					LocalDateTime currentDateTime = LocalDateTime.now();
//					// Format the date and time
//					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//					String formattedDateTime = currentDateTime.format(formatter);
//					// Extract hours and minutes
//					int hours = currentDateTime.getHour();
//					int minutes = currentDateTime.getMinute();
//					Long id=entity.getId();
//					Optional<DurationDTO> time = createTicketRepository.getByTimeHRMins(id);
//					String dhrs=time.get().getHoursMins().trim() +" hours";
//					//responseDTO.setDurationInHours(time.get().getHoursMins().trim() +"hours".trim());
//					responseDTO.setDurationInHours(dhrs.toString().trim());
//				}
		if (Objects.nonNull(entity.getSlaMaster())) {
			responseDTO.setSla(entity.getSlaMaster().getSla());
			responseDTO.setSlaId(entity.getSlaMaster().getId());
		}

		try {
			String duration = entity.getDuration();
			if (entity.getTicketStatus().getTicketstatusname().equalsIgnoreCase("Closed")
					|| entity.getTicketStatus().getTicketstatusname().equalsIgnoreCase("Resolved")) {
				if (Objects.nonNull(duration)) {
					Long timestampduration = Long.valueOf(entity.getDuration());
					long hours = TimeUnit.SECONDS.toHours(timestampduration);
					long minutes = TimeUnit.SECONDS.toMinutes(timestampduration) % 60;
					String durationhrs = hours + "." + minutes;
					responseDTO.setDurationInHours(durationhrs.toString().trim());
					// SLA Breach Hours
					double durationh = Double.parseDouble(responseDTO.getDurationInHours());
					double slahrs = Double.parseDouble(responseDTO.getSla().toString());
					double Slabreachhrs = durationh - slahrs;
					double slabreachHrsString = Slabreachhrs;
					double slabreachHrs = (slabreachHrsString);
					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					String formattedSlabreachHrs = decimalFormat.format(slabreachHrs);
					if (formattedSlabreachHrs.contains("-")) {
						responseDTO.setSLABreachHrs("0.0");

					} else {
						responseDTO.setSLABreachHrs(String.valueOf(formattedSlabreachHrs));

					}

				}

				else if (Objects.isNull(duration)) {
					LocalDateTime modifiedDate = entity.getModifiedDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDateTime();
					LocalDateTime createdDate = entity.getCreatedDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDateTime();

					// Duration duration = Duration.between(createdDate, modifiedDate);

					LocalDateTime tempDateTime = LocalDateTime.from(createdDate);

					long hours = tempDateTime.until(modifiedDate, ChronoUnit.HOURS);
					tempDateTime = tempDateTime.plusHours(hours);

					long minutes = tempDateTime.until(modifiedDate, ChronoUnit.MINUTES);
					tempDateTime = tempDateTime.plusMinutes(minutes);
					responseDTO.setDurationInHours(hours + "." + minutes);
					// SLA Breach Hours
					double durationh = Double.parseDouble(responseDTO.getDurationInHours());
					double slahrs = Double.parseDouble(responseDTO.getSla().toString());
					double Slabreachhrs = durationh - slahrs;
					double slabreachHrsString = Slabreachhrs;
					double slabreachHrs = (slabreachHrsString);
					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					String formattedSlabreachHrs = decimalFormat.format(slabreachHrs);
					if (formattedSlabreachHrs.contains("-")) {
						responseDTO.setSLABreachHrs("0.0");

					} else {
						responseDTO.setSLABreachHrs(String.valueOf(formattedSlabreachHrs));

					}
				}
			} else {
				LocalDateTime currentDateTime = LocalDateTime.now();
				// Format the date and time
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formattedDateTime = currentDateTime.format(formatter);
				// Extract hours and minutes
				int hours = currentDateTime.getHour();
				int minutes = currentDateTime.getMinute();
				Long id = entity.getId();
				Optional<DurationDTO> time = createTicketRepository.getByTimeHRMins(id);
				String dhrs = time.get().getHoursMins().trim();
				// responseDTO.setDurationInHours(time.get().getHoursMins().trim()
				// +"hours".trim());
				responseDTO.setDurationInHours(dhrs.toString().trim());
				// SLA Breach Hours
				double durationh = Double.parseDouble(responseDTO.getDurationInHours());
				double slahrs = Double.parseDouble(responseDTO.getSla().toString());
				double Slabreachhrs = durationh - slahrs;
				double slabreachHrsString = Slabreachhrs;
				double slabreachHrs = (slabreachHrsString);
				DecimalFormat decimalFormat = new DecimalFormat("#.##");
				String formattedSlabreachHrs = decimalFormat.format(slabreachHrs);
				if (formattedSlabreachHrs.contains("-")) {
					responseDTO.setSLABreachHrs("0.0");

				} else {
					responseDTO.setSLABreachHrs(String.valueOf(formattedSlabreachHrs));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (entity.getCreatedBy() == 0) {
			responseDTO.setCreatedBy(entity.getCreatedbyName());
		} else {
			responseDTO.setCreatedBy(createdByUserName);
		}
		// responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		responseDTO.setActive(entity.isActive());
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		if (Objects.nonNull(entity.getCategory())) {
			responseDTO.setCategoryName(entity.getCategory().getCategoryName());
			responseDTO.setCategoryId(entity.getCategory().getId());
		}
		if (Objects.nonNull(entity.getSubCategory())) {
			responseDTO.setSubCategoryName(entity.getSubCategory().getSubCategoryName());
			responseDTO.setSubCategoryId(entity.getSubCategory().getId());
		}

		if (Objects.nonNull(entity.getAssignGroup())) {
			responseDTO.setAssignGroupId(entity.getAssignGroup().getId());
			responseDTO.setAssignGroupName(entity.getAssignGroup().getRoleName());
		}

		if (Objects.nonNull(entity.getIssueFrom())) {
			responseDTO.setIssueFrom(entity.getIssueFrom().getIssueFrom());
			responseDTO.setIssueFromId(entity.getIssueFrom().getId());
		}

		if (Objects.nonNull(entity.getIssueMaster())) {
			responseDTO.setIssueDetails(entity.getIssueMaster().getIssueName());
			responseDTO.setIssueDetailsId(entity.getIssueMaster().getId());
			responseDTO.setIssueType(entity.getIssueMaster().isIssuetype());
			Boolean software = responseDTO.getIssueType();
			Boolean ver = true;
			if (software.equals(ver)) {
				responseDTO.setIssuetypeName("Software");
			}

			else {
				responseDTO.setIssuetypeName("Hardware");
			}
		}
		if (Objects.nonNull(entity.getKnowledgeBase())) {
			responseDTO.setKnowledgeBase(entity.getKnowledgeBase().getKnowledgeSolution());
			responseDTO.setKnowledgeBaseId(entity.getKnowledgeBase().getId());
			responseDTO.setKnowledgeBaseKBID(entity.getKnowledgeBase().getKbId());
		}

		if (Objects.nonNull(entity.getActionTakenMaster())) {
			responseDTO.setActionTakenName(entity.getActionTakenMaster().getActionTaken());
			responseDTO.setActionTakenId(entity.getActionTakenMaster().getId());
		}

		if (Objects.nonNull(entity.getActualProblemMaster())) {
			responseDTO.setActualProplemName(entity.getActualProblemMaster().getActualProblem());
			responseDTO.setActualProblemId(entity.getActualProblemMaster().getId());
		}

		if (Objects.nonNull(entity.getProblemReportedMaster())) {
			responseDTO.setProblemReportedName(entity.getProblemReportedMaster().getProblem());
			responseDTO.setProblemReportedId(entity.getProblemReportedMaster().getId());
		}
		if (Objects.nonNull(entity.getAssignTo())) {
			responseDTO.setAssignToName(entity.getAssignTo().getMiddleName());
			responseDTO.setAssignToId(entity.getAssignTo().getId());
			responseDTO.setFirstName(entity.getAssignTo().getFirstName());
			responseDTO.setFieldmobileNo(entity.getAssignTo().getPhoneNumber());
		}

		if (Objects.nonNull(entity.getPriority())) {
			responseDTO.setPriorityName(entity.getPriority().getPriority());
			responseDTO.setPriorityId(entity.getPriority().getId());
		} else {
			responseDTO.setPriorityName("");
			// responseDTO.setPriorityId(Long.valueOf(null));
		}

		if (Objects.nonNull(entity.getKnowledgeSolution())) {
			responseDTO.setSolutionId(entity.getKnowledgeSolution().getSolutionId());
			responseDTO.setSolutionCategoryId(entity.getKnowledgeSolution().getCategoryId().getId());
			responseDTO.setSolutionCategoryName(entity.getKnowledgeSolution().getCategoryId().getCategoryName());
			responseDTO.setSolutionSubcategoryId(entity.getKnowledgeSolution().getSubcategoryId().getId());
			responseDTO
					.setSolutionSubcategoryName(entity.getKnowledgeSolution().getSubcategoryId().getSubCategoryName());
			responseDTO.setSolutionIssueDetails(entity.getKnowledgeSolution().getIssueDetails());
			responseDTO.setSolutionNotes(entity.getKnowledgeSolution().getNotes());
		}

		if (Objects.nonNull(entity.getTicketStatus())) {
			responseDTO.setTicketStatus(entity.getTicketStatus().getTicketstatusname());
			responseDTO.setTicketStatusId(entity.getTicketStatus().getId());
		}
//		if(Objects.nonNull(entity.getTicketStatus())) {
//		responseDTO.setTicketStatus(entity.getTicketStatus().getTicketstatusname());
//		responseDTO.setTicketStatusId(entity.getTicketStatus().getId());
//	    }
		responseDTO.setTicketNumber(entity.getTicketNumber());
		responseDTO.setLicenceNumber(entity.getLicenceNumber());
		responseDTO.setLicenseTypeId(entity.getLicenceTypeId());
		responseDTO.setEntityTypeName(entity.getEntityTypeId());
		responseDTO.setRemarks(entity.getRemarks());
		responseDTO.setCallDisconnect(entity.getCallDisconnect());
		responseDTO.setRequiredField(entity.getRequiredField());
		responseDTO.setEmail(entity.getEmail());
		responseDTO.setMobile(entity.getMobile());
		responseDTO.setUnitName(entity.getUnitName());
		responseDTO.setLicenseStatus(entity.getLicenseStatus());
		responseDTO.setNotes(entity.getNotes());
		responseDTO.setAlternativemobileNumber(entity.getAlternativemobileNumber());
		responseDTO.setAddress(entity.getAddress());
		// responseDTO.setCreatedbyName(entity.getCreatedbyName());
		responseDTO.setDistrict(entity.getDistrict());
		responseDTO.setShopCode(entity.getShopCode());
		responseDTO.setShopName(entity.getShopName());
		responseDTO.setDistrictCode(entity.getDistrictCode());
		responseDTO.setViewStatus(entity.isViewStatus());
		responseDTO.setTehsilCode(entity.getTehsilCode());
		responseDTO.setTehsilName(entity.getTehsilName());
		responseDTO.setUploadApplication(entity.getUploadApplication());
		responseDTO.setApplicationUuid(entity.getApplicationUuid());
		responseDTO.setImageUrl(entity.getImageUrl());
		responseDTO.setImageUuid(entity.getImageUuid());
		responseDTO.setSearchOption(entity.getSearchOption());
		responseDTO.setRaisedBy(entity.getRaisedBy());
		return responseDTO;
	}

	// newone
	public CreateTicketResponseDto convertEntityToResponseDTOPaymentApp(CreateTicketEntity entity) {
		CreateTicketResponseDto responseDTO = commonUtil.modalMap(entity, CreateTicketResponseDto.class);
		String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());
		// String
		// firstname=commonDataController.getFirstNameById(entity.getCreatedBy());
		// responseDTO.setFirstName(firstname);
		// String mobileno=commonDataController.getmobileNoById(entity.getCreatedBy());
		// responseDTO.setFieldmobileNo(mobileno);
		/*
		 * String entityTypeMasterResponse = commonDataController
		 * .getMasterDropDownValueByKey(Constant.ENTITY_TYPE_DROPDOWN_KEY,
		 * entity.getEntityTypeId()); if (StringUtils.isBlank(entityTypeMasterResponse))
		 * { throw new
		 * InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
		 * .getMessage(new Object[] { Constant.ENTITY_TYPE_CODE })); }
		 */

		///////////////////////// existing one///////////////////////

//		LocalDateTime modifiedDate = entity.getModifiedDate().toInstant().atZone(ZoneId.systemDefault())
//				.toLocalDateTime();
//		LocalDateTime createdDate = entity.getCreatedDate().toInstant().atZone(ZoneId.systemDefault())
//				.toLocalDateTime();

		// Duration duration = Duration.between(createdDate, modifiedDate);

//		LocalDateTime tempDateTime = LocalDateTime.from(createdDate);
//
//		long hours = tempDateTime.until(modifiedDate, ChronoUnit.HOURS);
//		tempDateTime = tempDateTime.plusHours(hours);
//
//		long minutes = tempDateTime.until(modifiedDate, ChronoUnit.MINUTES);
//		tempDateTime = tempDateTime.plusMinutes(minutes);
//
//		responseDTO.setDurationInHours(hours + "." + minutes + " hours");

		if (Objects.nonNull(entity.getTicketStatus())) {
			responseDTO.setTicketStatus(entity.getTicketStatus().getTicketstatusname());
			responseDTO.setTicketStatusId(entity.getTicketStatus().getId());
		}

		////////////////////////////////////////////////////////////////////////////
		if (Objects.nonNull(entity.getSlaMaster())) {
			responseDTO.setSla(entity.getSlaMaster().getSla());
			responseDTO.setSlaId(entity.getSlaMaster().getId());
		}

		try {
			String duration = entity.getDuration();
			if (entity.getTicketStatus().getTicketstatusname().equalsIgnoreCase("Closed")
					|| entity.getTicketStatus().getTicketstatusname().equalsIgnoreCase("Resolved")) {
				if (Objects.nonNull(duration)) {
					Long timestampduration = Long.valueOf(entity.getDuration());
					long hours = TimeUnit.SECONDS.toHours(timestampduration);
					long minutes = TimeUnit.SECONDS.toMinutes(timestampduration) % 60;
					String durationhrs = hours + "." + minutes;
					responseDTO.setDurationInHours(durationhrs.toString().trim());
					// SLA Breach Hours
					double durationh = Double.parseDouble(responseDTO.getDurationInHours());
					double slahrs = Double.parseDouble(responseDTO.getSla().toString());
					double Slabreachhrs = durationh - slahrs;
					double slabreachHrsString = Slabreachhrs;
					double slabreachHrs = (slabreachHrsString);
					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					String formattedSlabreachHrs = decimalFormat.format(slabreachHrs);
					if (formattedSlabreachHrs.contains("-")) {
						responseDTO.setSLABreachHrs("0.0");

					} else {
						responseDTO.setSLABreachHrs(String.valueOf(formattedSlabreachHrs));

					}

				}

				else if (Objects.isNull(duration)) {
					LocalDateTime modifiedDate = entity.getModifiedDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDateTime();
					LocalDateTime createdDate = entity.getCreatedDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDateTime();

					// Duration duration = Duration.between(createdDate, modifiedDate);

					LocalDateTime tempDateTime = LocalDateTime.from(createdDate);

					long hours = tempDateTime.until(modifiedDate, ChronoUnit.HOURS);
					tempDateTime = tempDateTime.plusHours(hours);

					long minutes = tempDateTime.until(modifiedDate, ChronoUnit.MINUTES);
					tempDateTime = tempDateTime.plusMinutes(minutes);
					responseDTO.setDurationInHours(hours + "." + minutes);
					// SLA Breach Hours
					double durationh = Double.parseDouble(responseDTO.getDurationInHours());
					double slahrs = Double.parseDouble(responseDTO.getSla().toString());
					double Slabreachhrs = durationh - slahrs;
					double slabreachHrsString = Slabreachhrs;
					double slabreachHrs = (slabreachHrsString);
					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					String formattedSlabreachHrs = decimalFormat.format(slabreachHrs);
					if (formattedSlabreachHrs.contains("-")) {
						responseDTO.setSLABreachHrs("0.0");

					} else {
						responseDTO.setSLABreachHrs(String.valueOf(formattedSlabreachHrs));

					}
				}
			} else {
				LocalDateTime currentDateTime = LocalDateTime.now();
				// Format the date and time
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formattedDateTime = currentDateTime.format(formatter);
				// Extract hours and minutes
				int hours = currentDateTime.getHour();
				int minutes = currentDateTime.getMinute();
				Long id = entity.getId();
				Optional<DurationDTO> time = createTicketRepository.getByTimeHRMins(id);
				String dhrs = time.get().getHoursMins().trim();
				// responseDTO.setDurationInHours(time.get().getHoursMins().trim()
				// +"hours".trim());
				responseDTO.setDurationInHours(dhrs.toString().trim());
				// SLA Breach Hours
				double durationh = Double.parseDouble(responseDTO.getDurationInHours());
				double slahrs = Double.parseDouble(responseDTO.getSla().toString());
				double Slabreachhrs = durationh - slahrs;
				double slabreachHrsString = Slabreachhrs;
				double slabreachHrs = (slabreachHrsString);
				DecimalFormat decimalFormat = new DecimalFormat("#.##");
				String formattedSlabreachHrs = decimalFormat.format(slabreachHrs);
				if (formattedSlabreachHrs.contains("-")) {
					responseDTO.setSLABreachHrs("0.0");

				} else {
					responseDTO.setSLABreachHrs(String.valueOf(formattedSlabreachHrs));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (entity.getCreatedBy() == 0) {
			responseDTO.setCreatedBy(entity.getCreatedbyName());
		} else {
			responseDTO.setCreatedBy(createdByUserName);
		}
		// responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		responseDTO.setActive(entity.isActive());
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		if (Objects.nonNull(entity.getCategory())) {
			responseDTO.setCategoryName(entity.getCategory().getCategoryName());
			responseDTO.setCategoryId(entity.getCategory().getId());
		}
		if (Objects.nonNull(entity.getSubCategory())) {
			responseDTO.setSubCategoryName(entity.getSubCategory().getSubCategoryName());
			responseDTO.setSubCategoryId(entity.getSubCategory().getId());
		}

		if (Objects.nonNull(entity.getAssignGroup())) {
			responseDTO.setAssignGroupId(entity.getAssignGroup().getId());
			responseDTO.setAssignGroupName(entity.getAssignGroup().getRoleName());
		}

		if (Objects.nonNull(entity.getIssueFrom())) {
			responseDTO.setIssueFrom(entity.getIssueFrom().getIssueFrom());
			responseDTO.setIssueFromId(entity.getIssueFrom().getId());
		}

		if (Objects.nonNull(entity.getIssueMaster())) {
			responseDTO.setIssueDetails(entity.getIssueMaster().getIssueName());
			responseDTO.setIssueDetailsId(entity.getIssueMaster().getId());
			responseDTO.setIssueType(entity.getIssueMaster().isIssuetype());
			Boolean software = responseDTO.getIssueType();
			Boolean ver = true;
			if (software.equals(ver)) {
				responseDTO.setIssuetypeName("Software");
			}

			else {
				responseDTO.setIssuetypeName("Hardware");
			}
		}
		if (Objects.nonNull(entity.getKnowledgeBase())) {
			responseDTO.setKnowledgeBase(entity.getKnowledgeBase().getKnowledgeSolution());
			responseDTO.setKnowledgeBaseId(entity.getKnowledgeBase().getId());
			responseDTO.setKnowledgeBaseKBID(entity.getKnowledgeBase().getKbId());
		}

		if (Objects.nonNull(entity.getSlaMaster())) {
			responseDTO.setSla(entity.getSlaMaster().getSla());
			responseDTO.setSlaId(entity.getSlaMaster().getId());
		}

		if (Objects.nonNull(entity.getActionTakenMaster())) {
			responseDTO.setActionTakenName(entity.getActionTakenMaster().getActionTaken());
			responseDTO.setActionTakenId(entity.getActionTakenMaster().getId());
		}

		if (Objects.nonNull(entity.getActualProblemMaster())) {
			responseDTO.setActualProplemName(entity.getActualProblemMaster().getActualProblem());
			responseDTO.setActualProblemId(entity.getActualProblemMaster().getId());
		}

		if (Objects.nonNull(entity.getProblemReportedMaster())) {
			responseDTO.setProblemReportedName(entity.getProblemReportedMaster().getProblem());
			responseDTO.setProblemReportedId(entity.getProblemReportedMaster().getId());
		}
		if (Objects.nonNull(entity.getAssignTo())) {
			responseDTO.setAssignToName(entity.getAssignTo().getMiddleName());
			responseDTO.setAssignToId(entity.getAssignTo().getId());
			responseDTO.setFirstName(entity.getAssignTo().getFirstName());
			responseDTO.setFieldmobileNo(entity.getAssignTo().getPhoneNumber());
		}

		if (Objects.nonNull(entity.getPriority())) {
			responseDTO.setPriorityName(entity.getPriority().getPriority());
			responseDTO.setPriorityId(entity.getPriority().getId());
		} else {
			responseDTO.setPriorityName("null");
			// responseDTO.setPriorityId(Long.valueOf(null));
		}

		if (Objects.nonNull(entity.getKnowledgeSolution())) {
			responseDTO.setSolutionId(entity.getKnowledgeSolution().getSolutionId());
			responseDTO.setSolutionCategoryId(entity.getKnowledgeSolution().getCategoryId().getId());
			responseDTO.setSolutionCategoryName(entity.getKnowledgeSolution().getCategoryId().getCategoryName());
			responseDTO.setSolutionSubcategoryId(entity.getKnowledgeSolution().getSubcategoryId().getId());
			responseDTO
					.setSolutionSubcategoryName(entity.getKnowledgeSolution().getSubcategoryId().getSubCategoryName());
			responseDTO.setSolutionIssueDetails(entity.getKnowledgeSolution().getIssueDetails());
			responseDTO.setSolutionNotes(entity.getKnowledgeSolution().getNotes());
		}
		responseDTO.setTicketNumber(entity.getTicketNumber());
		responseDTO.setLicenceNumber(entity.getLicenceNumber());
		responseDTO.setLicenseTypeId(entity.getLicenceTypeId());
		responseDTO.setEntityTypeName(entity.getEntityTypeId());
		responseDTO.setRemarks(entity.getRemarks());
		responseDTO.setCallDisconnect(entity.getCallDisconnect());
		responseDTO.setRequiredField(entity.getRequiredField());
		responseDTO.setEmail(entity.getEmail());
		responseDTO.setMobile(entity.getMobile());
		responseDTO.setUnitName(entity.getUnitName());
		responseDTO.setLicenseStatus(entity.getLicenseStatus());
		responseDTO.setNotes(entity.getNotes());
		responseDTO.setAlternativemobileNumber(entity.getAlternativemobileNumber());
		responseDTO.setAddress(entity.getAddress());
		// responseDTO.setCreatedbyName(entity.getCreatedbyName());
		responseDTO.setDistrict(entity.getDistrict());
		responseDTO.setShopCode(entity.getShopCode());
		responseDTO.setShopName(entity.getShopName());
		responseDTO.setDistrictCode(entity.getDistrictCode());
		responseDTO.setViewStatus(entity.isViewStatus());
		responseDTO.setTehsilCode(entity.getTehsilCode());
		responseDTO.setTehsilName(entity.getTehsilName());
		responseDTO.setUploadApplication(entity.getUploadApplication());
		responseDTO.setApplicationUuid(entity.getApplicationUuid());
		responseDTO.setImageUrl(entity.getImageUrl());
		responseDTO.setImageUuid(entity.getImageUuid());
		responseDTO.setSearchOption(entity.getSearchOption());
		responseDTO.setRaisedBy(entity.getRaisedBy());
		return responseDTO;
	}

	public CreateTicketResponseDto convertEntityToResponseDTOReports(CreateTicketEntity entity) {
		CreateTicketResponseDto responseDTO = commonUtil.modalMap(entity, CreateTicketResponseDto.class);
		String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		String modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());
//		String firstname=commonDataController.getFirstNameById(entity.getCreatedBy());
//		responseDTO.setFirstName(firstname);
//		String mobileno=commonDataController.getmobileNoById(entity.getCreatedBy());
//		responseDTO.setFieldmobileNo(mobileno);
		/*
		 * String entityTypeMasterResponse = commonDataController
		 * .getMasterDropDownValueByKey(Constant.ENTITY_TYPE_DROPDOWN_KEY,
		 * entity.getEntityTypeId()); if (StringUtils.isBlank(entityTypeMasterResponse))
		 * { throw new
		 * InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
		 * .getMessage(new Object[] { Constant.ENTITY_TYPE_CODE })); }
		 */
		////////////////////// Existing One/////////////////////////

//		LocalDateTime modifiedDate = entity.getModifiedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//		LocalDateTime createdDate = entity.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//				
//		//Duration duration = Duration.between(createdDate, modifiedDate);
//		
//		LocalDateTime tempDateTime = LocalDateTime.from( createdDate );
//		
//		long hours = tempDateTime.until( modifiedDate, ChronoUnit.HOURS );
//		tempDateTime = tempDateTime.plusHours( hours );
//
//		long minutes = tempDateTime.until( modifiedDate, ChronoUnit.MINUTES );
//		tempDateTime = tempDateTime.plusMinutes( minutes );
//		
//		 responseDTO.setDurationInHours(hours+"."+ minutes+" hours");

//////////////Newly add////////////////////////////////
		if (Objects.nonNull(entity.getTicketStatus())) {
			responseDTO.setTicketStatus(entity.getTicketStatus().getTicketstatusname());
			responseDTO.setTicketStatusId(entity.getTicketStatus().getId());
		}

//		try {
//			if (responseDTO.getTicketStatus().equalsIgnoreCase("Closed")
//					|| responseDTO.getTicketStatus().equalsIgnoreCase("Resolved")) {
//				String duration=entity.getDuration();
//				if (duration !=null && !duration.isEmpty()) {
//					Long timestampduration = Long.valueOf(entity.getDuration());
//					  long hours = TimeUnit.SECONDS.toHours(timestampduration);
//				        long minutes = TimeUnit.SECONDS.toMinutes(timestampduration) % 60;
//					responseDTO.setDurationInHours(hours + "." + minutes + " hours");
//				}
//
//			} else {
//				LocalDateTime currentDateTime = LocalDateTime.now();
//				// Format the date and time
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//				String formattedDateTime = currentDateTime.format(formatter);
//				// Extract hours and minutes
//				int hours = currentDateTime.getHour();
//				int minutes = currentDateTime.getMinute();
//				Long id=entity.getId();
//				Optional<DurationDTO> time = createTicketRepository.getByTimeHRMins(id);
//				responseDTO.setDurationInHours(time.get().getHoursMins() + " hours");
//				//responseDTO.setDurationInHours(hours + "." + minutes + " hours");
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		if (Objects.nonNull(entity.getSlaMaster())) {
			responseDTO.setSla(entity.getSlaMaster().getSla());
			responseDTO.setSlaId(entity.getSlaMaster().getId());
		}

		try {
			String duration = entity.getDuration();
			if (entity.getTicketStatus().getTicketstatusname().equalsIgnoreCase("Closed")
					|| entity.getTicketStatus().getTicketstatusname().equalsIgnoreCase("Resolved")) {
				if (Objects.nonNull(duration)) {
					Long timestampduration = Long.valueOf(entity.getDuration());
					long hours = TimeUnit.SECONDS.toHours(timestampduration);
					long minutes = TimeUnit.SECONDS.toMinutes(timestampduration) % 60;
					String durationhrs = hours + "." + minutes;
					responseDTO.setDurationInHours(durationhrs.toString().trim());
					// SLA Breach Hours
					double durationh = Double.parseDouble(responseDTO.getDurationInHours());
					double slahrs = Double.parseDouble(responseDTO.getSla().toString());
					double Slabreachhrs = durationh - slahrs;
					double slabreachHrsString = Slabreachhrs;
					double slabreachHrs = (slabreachHrsString);
					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					String formattedSlabreachHrs = decimalFormat.format(slabreachHrs);
					if (formattedSlabreachHrs.contains("-")) {
						responseDTO.setSLABreachHrs("0.0");

					} else {
						responseDTO.setSLABreachHrs(String.valueOf(formattedSlabreachHrs));

					}
				}

				else if (Objects.isNull(duration)) {
					LocalDateTime modifiedDate = entity.getModifiedDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDateTime();
					LocalDateTime createdDate = entity.getCreatedDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDateTime();

					// Duration duration = Duration.between(createdDate, modifiedDate);

					LocalDateTime tempDateTime = LocalDateTime.from(createdDate);

					long hours = tempDateTime.until(modifiedDate, ChronoUnit.HOURS);
					tempDateTime = tempDateTime.plusHours(hours);

					long minutes = tempDateTime.until(modifiedDate, ChronoUnit.MINUTES);
					tempDateTime = tempDateTime.plusMinutes(minutes);
					responseDTO.setDurationInHours(hours + "." + minutes);

					// SLA Breach Hours
					double durationh = Double.parseDouble(responseDTO.getDurationInHours());
					double slahrs = Double.parseDouble(responseDTO.getSla().toString());
					double Slabreachhrs = durationh - slahrs;
					double slabreachHrsString = Slabreachhrs;
					double slabreachHrs = (slabreachHrsString);
					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					String formattedSlabreachHrs = decimalFormat.format(slabreachHrs);
					if (formattedSlabreachHrs.contains("-")) {
						responseDTO.setSLABreachHrs("0.0");

					} else {
						responseDTO.setSLABreachHrs(String.valueOf(formattedSlabreachHrs));

					}
				}
			} else {
				LocalDateTime currentDateTime = LocalDateTime.now();
				// Format the date and time
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formattedDateTime = currentDateTime.format(formatter);
				// Extract hours and minutes
				int hours = currentDateTime.getHour();
				int minutes = currentDateTime.getMinute();
				Long id = entity.getId();
				Optional<DurationDTO> time = createTicketRepository.getByTimeHRMins(id);
				String dhrs = time.get().getHoursMins().trim();
				// responseDTO.setDurationInHours(time.get().getHoursMins().trim()
				// +"hours".trim());
				responseDTO.setDurationInHours(dhrs.toString().trim());
				// SLA Breach Hours
				double durationh = Double.parseDouble(responseDTO.getDurationInHours());
				double slahrs = Double.parseDouble(responseDTO.getSla().toString());
				double Slabreachhrs = durationh - slahrs;
				double slabreachHrsString = Slabreachhrs;
				double slabreachHrs = (slabreachHrsString);
				DecimalFormat decimalFormat = new DecimalFormat("#.##");
				String formattedSlabreachHrs = decimalFormat.format(slabreachHrs);
				if (formattedSlabreachHrs.contains("-")) {
					responseDTO.setSLABreachHrs("0.0");

				} else {
					responseDTO.setSLABreachHrs(String.valueOf(formattedSlabreachHrs));

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (entity.getCreatedBy() == 0) {
			responseDTO.setCreatedBy(entity.getCreatedbyName());
		} else {
			responseDTO.setCreatedBy(createdByUserName);
		}
		// responseDTO.setCreatedBy(createdByUserName);
		responseDTO.setModifiedBy(modifiedByUserName);
		responseDTO.setActive(entity.isActive());
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		if (Objects.nonNull(entity.getCategory())) {
			responseDTO.setCategoryName(entity.getCategory().getCategoryName());
			responseDTO.setCategoryId(entity.getCategory().getId());
		}
		if (Objects.nonNull(entity.getSubCategory())) {
			responseDTO.setSubCategoryName(entity.getSubCategory().getSubCategoryName());
			responseDTO.setSubCategoryId(entity.getSubCategory().getId());
		}

		if (Objects.nonNull(entity.getAssignGroup())) {
			responseDTO.setAssignGroupId(entity.getAssignGroup().getId());
			responseDTO.setAssignGroupName(entity.getAssignGroup().getRoleName());
		}

		if (Objects.nonNull(entity.getIssueFrom())) {
			responseDTO.setIssueFrom(entity.getIssueFrom().getIssueFrom());
			responseDTO.setIssueFromId(entity.getIssueFrom().getId());
		}
		if (Objects.nonNull(entity.getIssueMaster())) {
			responseDTO.setIssueDetails(entity.getIssueMaster().getIssueName());
			responseDTO.setIssueDetailsId(entity.getIssueMaster().getId());
			responseDTO.setIssueType(entity.getIssueMaster().isIssuetype());
			Boolean software = responseDTO.getIssueType();
			Boolean ver = true;
			if (software.equals(ver)) {
				responseDTO.setIssuetypeName("Software");
			}

			else {
				responseDTO.setIssuetypeName("Hardware");
			}
		}
		if (Objects.nonNull(entity.getKnowledgeBase())) {
			responseDTO.setKnowledgeBase(entity.getKnowledgeBase().getKnowledgeSolution());
			responseDTO.setKnowledgeBaseId(entity.getKnowledgeBase().getId());
			responseDTO.setKnowledgeBaseKBID(entity.getKnowledgeBase().getKbId());
		}

//		  if(Objects.nonNull(entity.getSlaMaster())) {
//		  responseDTO.setSla(entity.getSlaMaster().getSla());
//		  responseDTO.setSlaId(entity.getSlaMaster().getId());
//		  }

		if (Objects.nonNull(entity.getActionTakenMaster())) {
			responseDTO.setActionTakenName(entity.getActionTakenMaster().getActionTaken());
			responseDTO.setActionTakenId(entity.getActionTakenMaster().getId());
		}

		if (Objects.nonNull(entity.getActualProblemMaster())) {
			responseDTO.setActualProplemName(entity.getActualProblemMaster().getActualProblem());
			responseDTO.setActualProblemId(entity.getActualProblemMaster().getId());
		}

		if (Objects.nonNull(entity.getProblemReportedMaster())) {
			responseDTO.setProblemReportedName(entity.getProblemReportedMaster().getProblem());
			responseDTO.setProblemReportedId(entity.getProblemReportedMaster().getId());
		}
		if (Objects.nonNull(entity.getAssignTo())) {
			responseDTO.setAssignToName(entity.getAssignTo().getMiddleName());
			responseDTO.setAssignToId(entity.getAssignTo().getId());
			responseDTO.setFirstName(entity.getAssignTo().getFirstName());
			responseDTO.setFieldmobileNo(entity.getAssignTo().getPhoneNumber());
		}

		if (Objects.nonNull(entity.getPriority())) {
			responseDTO.setPriorityName(entity.getPriority().getPriority());
			responseDTO.setPriorityId(entity.getPriority().getId());
		}

		if (Objects.nonNull(entity.getKnowledgeSolution())) {
			responseDTO.setSolutionId(entity.getKnowledgeSolution().getSolutionId());
			responseDTO.setSolutionCategoryId(entity.getKnowledgeSolution().getCategoryId().getId());
			responseDTO.setSolutionCategoryName(entity.getKnowledgeSolution().getCategoryId().getCategoryName());
			responseDTO.setSolutionSubcategoryId(entity.getKnowledgeSolution().getSubcategoryId().getId());
			responseDTO
					.setSolutionSubcategoryName(entity.getKnowledgeSolution().getSubcategoryId().getSubCategoryName());
			responseDTO.setSolutionIssueDetails(entity.getKnowledgeSolution().getIssueDetails());
			responseDTO.setSolutionNotes(entity.getKnowledgeSolution().getNotes());
		}
		responseDTO.setTicketNumber(entity.getTicketNumber());
		responseDTO.setLicenceNumber(entity.getLicenceNumber());
		responseDTO.setLicenseTypeId(entity.getLicenceTypeId());
		responseDTO.setEntityTypeName(entity.getEntityTypeId());
		responseDTO.setRemarks(entity.getRemarks());
		responseDTO.setCallDisconnect(entity.getCallDisconnect());
		responseDTO.setRequiredField(entity.getRequiredField());
		responseDTO.setEmail(entity.getEmail());
		responseDTO.setMobile(entity.getMobile());
		responseDTO.setUnitName(entity.getUnitName());
		responseDTO.setLicenseStatus(entity.getLicenseStatus());
		responseDTO.setNotes(entity.getNotes());
		responseDTO.setAlternativemobileNumber(entity.getAlternativemobileNumber());
		responseDTO.setAddress(entity.getAddress());
		// responseDTO.setCreatedbyName(entity.getCreatedbyName());
		responseDTO.setDistrict(entity.getDistrict());
		responseDTO.setShopCode(entity.getShopCode());
		responseDTO.setShopName(entity.getShopName());
		responseDTO.setDistrictCode(entity.getDistrictCode());
		responseDTO.setViewStatus(entity.isViewStatus());
		responseDTO.setTehsilCode(entity.getTehsilCode());
		responseDTO.setTehsilName(entity.getTehsilName());
		responseDTO.setUploadApplication(entity.getUploadApplication());
		responseDTO.setApplicationUuid(entity.getApplicationUuid());
		responseDTO.setImageUrl(entity.getImageUrl());
		responseDTO.setImageUuid(entity.getImageUuid());
		responseDTO.setSearchOption(entity.getSearchOption());
		responseDTO.setRaisedBy(entity.getRaisedBy());
		return responseDTO;
	}

	public CreateTicketEntity convertRequestDTOToEntity(CreateTicketRequestDto requestDTO, CreateTicketEntity entity) {
		if (Objects.isNull(entity)) {

			entity = commonUtil.modalMap(requestDTO, CreateTicketEntity.class);
		}
		Optional<Category> categoryEntity = categoryRepository.findById(requestDTO.getCategoryId());
		if (!categoryEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { CATEGORYID }));
		}

		entity.setCategory(categoryEntity.get());
		Optional<SubCategory> subCategoryEntity = subCategoryRepository.findById(requestDTO.getSubCategoryId());
		if (!subCategoryEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { SUB_CATEGORY_ID }));
		}

		entity.setSubCategory(subCategoryEntity.get());

		Optional<TicketStatusEntity> ticketOptional = ticketStatusrepository.findById(requestDTO.getTicketStatusId());
		if (!ticketOptional.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { TICKET_STATUS_ID }));
		}

		entity.setTicketStatus(ticketOptional.get());

		Optional<IssueDetails> issueDetails = issueDetailsRepository.findById(requestDTO.getIssueDetailsId());
		if (!issueDetails.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ISSUE_DETAILS }));
		}

		entity.setIssueMaster(issueDetails.get());

		Optional<IssueFromEntity> issueFrom = issueFromRepository.findById(requestDTO.getIssueFromId());
		if (!ticketOptional.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ISSUE_FROM_ID }));
		}

		entity.setIssueFrom(issueFrom.get());
		if (Objects.nonNull(requestDTO.getSlaId())) {
			Optional<SlaMasterEntity> slaMaster = slaMasterRepository.findById(requestDTO.getSlaId());
			if (!slaMaster.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { SLA_ID }));
			}

			entity.setSlaMaster(slaMaster.get());
		}

		if (requestDTO.getKnowledgeBaseId() != null) {
			Optional<KnowledgeBase> knOptional = knowledgeRepository.findById(requestDTO.getKnowledgeBaseId());
			if (!knOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { KB_ID }));
			}

			entity.setKnowledgeBase(knOptional.get());
		}
		/*
		 * Optional<UserGroupEntity> uOptional =
		 * usergroupRepository.findById(requestDTO.getAssignGroupId()); if
		 * (!uOptional.isPresent()) {
		 * ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] {
		 * USER_GROUP_ID })); }
		 */
		if (Objects.nonNull(requestDTO.getPriorityId())) {
			Optional<PriorityMaster> pOptional = priorityMasterRepository.findById(requestDTO.getPriorityId());
			if (!pOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { PRIORITYID }));
			}

			entity.setPriority(pOptional.get());
		}
		if (requestDTO.getProblemReportedId() != null) {
			Optional<ProblemReported> prOptional = problemReportedRepository
					.findById(requestDTO.getProblemReportedId());
			if (!prOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { PROBLEM_REPORTED_ID }));
			}
			entity.setProblemReportedMaster(prOptional.get());
		}

		if (requestDTO.getActionTakenId() != null) {
			Optional<ActionTaken> atOptional = actionTakenRepository.findById(requestDTO.getActionTakenId());
			if (!atOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ACTION_TAKEN_ID }));
			}
			entity.setActionTakenMaster(atOptional.get());
		}

		if (requestDTO.getActualProblemId() != null) {
			Optional<ActualProblem> apOptional = actualProblemRepository.findById(requestDTO.getActualProblemId());
			if (!apOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ACTUAL_PROBLEM_ID }));
			}
			entity.setActualProblemMaster(apOptional.get());
		}

		if (requestDTO.getAssignGroupId() != null) {
			Optional<RoleMaster> rOptional = roleMasterRepository.findById(requestDTO.getAssignGroupId());
			if (!rOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ROLE_ID }));
			}
			entity.setAssignGroup(rOptional.get());
		}

		if (requestDTO.getAssignToId() != null) {
			Optional<UserEntity> uOptional = userRepository.findById(requestDTO.getAssignToId());
			if (!uOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { USER_ID }));
			}
			entity.setAssignTo(uOptional.get());
		}

		if (requestDTO.getSolutionId() != null) {
			Optional<KnowledgeSolution> kOptional = knowledgeSolutionRepository.findById(requestDTO.getSolutionId());
			if (!kOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { KNOWLEDGE_SOLUTION }));
			}
			entity.setKnowledgeSolution(kOptional.get());
		}

		entity.setSearchOption(requestDTO.getSearchOption());

		entity.setCallDisconnect(requestDTO.getCallDisconnect());

		entity.setRequiredField(requestDTO.getRequiredField());

		entity.setRemarks(requestDTO.getRemarks());

		entity.setActive(requestDTO.isActive());

		entity.setTicketNumber(requestDTO.getTicketNumber());

		entity.setEntityTypeId(requestDTO.getEntityTypeId());

		entity.setLicenceNumber(requestDTO.getLicenseNumber());

		entity.setLicenceTypeId(requestDTO.getLicenseTypeId());

		entity.setEmail(requestDTO.getEmail());

		entity.setMobile(requestDTO.getMobile());

		entity.setUnitName(requestDTO.getUnitName());

		entity.setLicenseStatus(requestDTO.getLicenseStatus());

		entity.setNotes(requestDTO.getNotes());

		entity.setAlternativemobileNumber(requestDTO.getAlternativemobileNumber());

		entity.setAddress(requestDTO.getAddress());

		entity.setCreatedbyName(requestDTO.getCreatedbyName());

		entity.setDistrict(requestDTO.getDistrict());

		entity.setShopCode(requestDTO.getShopCode());

		entity.setShopName(requestDTO.getShopName());

		entity.setDistrictCode(requestDTO.getDistrictCode());

		entity.setViewStatus(requestDTO.isViewStatus());

		entity.setTehsilCode(requestDTO.getTehsilCode());

		entity.setTehsilName(requestDTO.getTehsilName());

		entity.setApplicationUuid(requestDTO.getApplicationUuid());

		entity.setUploadApplication(requestDTO.getUploadApplication());

		entity.setImageUrl(requestDTO.getImageUrl());

		entity.setImageUuid(requestDTO.getImageUuid());
		// entity.setUnitCode(requestDTO.getUnitCode());

		entity.setRaisedBy(requestDTO.getRaisedBy());

		entity.setDuration(requestDTO.getDuration());

		entity.setIssueTypeSH(requestDTO.getIssueTypeSH());

		return entity;
	}

	public CreateTicketEntity convertRequestToEntityForUpdate(CreateTicketDashboardDTO requestDTO,
			CreateTicketEntity entity) {
		if (Objects.isNull(entity)) {

			entity = commonUtil.modalMap(requestDTO, CreateTicketEntity.class);
		}
		if (requestDTO.getProblemReportedId() != null) {
			Optional<ProblemReported> prOptional = problemReportedRepository
					.findById(requestDTO.getProblemReportedId());
			if (!prOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { PROBLEM_REPORTED_ID }));
			}
			entity.setProblemReportedMaster(prOptional.get());
		}

		if (requestDTO.getActionTakenId() != null) {
			Optional<ActionTaken> atOptional = actionTakenRepository.findById(requestDTO.getActionTakenId());
			if (!atOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ACTION_TAKEN_ID }));
			}
			entity.setActionTakenMaster(atOptional.get());
		}

		if (requestDTO.getActualProblemId() != null) {
			Optional<ActualProblem> apOptional = actualProblemRepository.findById(requestDTO.getActualProblemId());
			if (!apOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ACTUAL_PROBLEM_ID }));
			}
			entity.setActualProblemMaster(apOptional.get());
		}

		// entity.setNotes(requestDTO.getNotes());
		entity.setRemarks(requestDTO.getRemarks());
		Optional<TicketStatusEntity> ticketOptional = ticketStatusrepository.findById(requestDTO.getTicketStatusId());
		if (!ticketOptional.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { TICKET_STATUS_ID }));
		}

		entity.setTicketStatus(ticketOptional.get());
		
		if (requestDTO.getAssignToId() != null) {
			Optional<UserEntity> assignOptional = userRepository.findById(requestDTO.getAssignToId());
			if (!assignOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { "ASSIGN_TO_ID" }));
			}
			entity.setAssignTo(assignOptional.get());
		}

		return entity;
	}

	public CreateTicketEntitypayment convertRequestDTOToEntityapp(CreateTicketDashboardDTO requestDTO,
			CreateTicketEntitypayment entity) {
		if (Objects.isNull(entity)) {

			entity = commonUtil.modalMap(requestDTO, CreateTicketEntitypayment.class);
		}
		Optional<Category> categoryEntity = categoryRepository.findById(requestDTO.getCategoryId());
		if (!categoryEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { CATEGORYID }));
		}

		entity.setCategory(categoryEntity.get());
		Optional<SubCategory> subCategoryEntity = subCategoryRepository.findById(requestDTO.getSubCategoryId());
		if (!subCategoryEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { SUB_CATEGORY_ID }));
		}

		entity.setSubCategory(subCategoryEntity.get());

		Optional<TicketStatusEntity> ticketOptional = ticketStatusrepository.findById(requestDTO.getTicketStatusId());
		if (!ticketOptional.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { TICKET_STATUS_ID }));
		}

		entity.setTicketStatus(ticketOptional.get());

		Optional<IssueDetails> issueDetails = issueDetailsRepository.findById(requestDTO.getIssueDetailsId());
		if (!issueDetails.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ISSUE_DETAILS }));
		}

		entity.setIssueMaster(issueDetails.get());

		Optional<IssueFromEntity> issueFrom = issueFromRepository.findById(requestDTO.getIssueFromId());
		if (!ticketOptional.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ISSUE_FROM_ID }));
		}

		entity.setIssueFrom(issueFrom.get());
		if (Objects.nonNull(requestDTO.getSlaId())) {
			Optional<SlaMasterEntity> slaMaster = slaMasterRepository.findById(requestDTO.getSlaId());
			if (!slaMaster.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { SLA_ID }));
			}

			entity.setSlaMaster(slaMaster.get());
		}

		if (requestDTO.getKnowledgeBaseId() != null) {
			Optional<KnowledgeBase> knOptional = knowledgeRepository.findById(requestDTO.getKnowledgeBaseId());
			if (!knOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { KB_ID }));
			}

			entity.setKnowledgeBase(knOptional.get());
		}
		/*
		 * Optional<UserGroupEntity> uOptional =
		 * usergroupRepository.findById(requestDTO.getAssignGroupId()); if
		 * (!uOptional.isPresent()) {
		 * ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] {
		 * USER_GROUP_ID })); }
		 */
		if (Objects.nonNull(requestDTO.getPriorityId())) {
			Optional<PriorityMaster> pOptional = priorityMasterRepository.findById(requestDTO.getPriorityId());
			if (!pOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { PRIORITYID }));
			}

			entity.setPriority(pOptional.get());
		}
		if (requestDTO.getProblemReportedId() != null) {
			Optional<ProblemReported> prOptional = problemReportedRepository
					.findById(requestDTO.getProblemReportedId());
			if (!prOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { PROBLEM_REPORTED_ID }));
			}
			entity.setProblemReportedMaster(prOptional.get());
		}

		if (requestDTO.getActionTakenId() != null) {
			Optional<ActionTaken> atOptional = actionTakenRepository.findById(requestDTO.getActionTakenId());
			if (!atOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ACTION_TAKEN_ID }));
			}
			entity.setActionTakenMaster(atOptional.get());
		}

		if (requestDTO.getActualProblemId() != null) {
			Optional<ActualProblem> apOptional = actualProblemRepository.findById(requestDTO.getActualProblemId());
			if (!apOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ACTUAL_PROBLEM_ID }));
			}
			entity.setActualProblemMaster(apOptional.get());
		}

		if (requestDTO.getAssignGroupId() != null) {
			Optional<RoleMaster> rOptional = roleMasterRepository.findById(requestDTO.getAssignGroupId());
			if (!rOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ROLE_ID }));
			}
			entity.setAssignGroup(rOptional.get());
		}

		if (requestDTO.getAssignToId() != null) {
			Optional<UserEntity> uOptional = userRepository.findById(requestDTO.getAssignToId());
			if (!uOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { USER_ID }));
			}
			entity.setAssignTo(uOptional.get());
		}

		if (requestDTO.getSolutionId() != null) {
			Optional<KnowledgeSolution> kOptional = knowledgeSolutionRepository.findById(requestDTO.getSolutionId());
			if (!kOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { KNOWLEDGE_SOLUTION }));
			}
			entity.setKnowledgeSolution(kOptional.get());
		}

		entity.setCallDisconnect(requestDTO.getCallDisconnect());

		entity.setRequiredField(requestDTO.getRequiredField());

		entity.setRemarks(requestDTO.getRemarks());

		entity.setActive(requestDTO.isActive());

		entity.setTicketNumber(requestDTO.getTicketNumber());

		entity.setEntityTypeId(requestDTO.getEntityTypeId());

		entity.setLicenceNumber(requestDTO.getLicenseNumber());

		entity.setLicenceTypeId(requestDTO.getLicenseTypeId());

		entity.setEmail(requestDTO.getEmail());

		entity.setMobile(requestDTO.getMobile());

		entity.setUnitName(requestDTO.getUnitName());

		entity.setLicenseStatus(requestDTO.getLicenseStatus());

		entity.setNotes(requestDTO.getNotes());

		entity.setAlternativemobileNumber(requestDTO.getAlternativemobileNumber());

		entity.setAddress(requestDTO.getAddress());

		entity.setCreatedbyName(requestDTO.getCreatedbyName());
//		Long l= new Long(0);  
//		int i=l.intValue();  
//		entity.setCreatedBy(l);

		entity.setDistrict(requestDTO.getDistrict());

		entity.setShopCode(requestDTO.getShopCode());

		entity.setShopName(requestDTO.getShopName());

		entity.setDistrictCode(requestDTO.getDistrictCode());

		entity.setViewStatus(requestDTO.isViewStatus());

		entity.setTehsilCode(requestDTO.getTehsilCode());

		entity.setTehsilName(requestDTO.getTehsilName());

		entity.setApplicationUuid(requestDTO.getApplicationUuid());

		entity.setUploadApplication(requestDTO.getUploadApplication());

		entity.setImageUrl(requestDTO.getImageUrl());

		entity.setImageUuid(requestDTO.getImageUuid());

		// entity.setUnitCode(requestDTO.getUnitCode());

		entity.setSearchOption(requestDTO.getSearchOption());

		entity.setRaisedBy(requestDTO.getRaisedBy());

		entity.setIssueTypeSH(requestDTO.getIssueTypeSH());

		return entity;
	}
	
	
	
	public CreateTicketEntitypayment convertRequestDTOToEntityappupdate(CreateTicketDashboardDTO requestDTO,
			CreateTicketEntitypayment entity) {
		if (Objects.isNull(entity)) {

			entity = commonUtil.modalMap(requestDTO, CreateTicketEntitypayment.class);
		}
		Optional<Category> categoryEntity = categoryRepository.findById(requestDTO.getCategoryId());
		if (!categoryEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { CATEGORYID }));
		}

		entity.setCategory(categoryEntity.get());
		Optional<SubCategory> subCategoryEntity = subCategoryRepository.findById(requestDTO.getSubCategoryId());
		if (!subCategoryEntity.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { SUB_CATEGORY_ID }));
		}

		entity.setSubCategory(subCategoryEntity.get());

		Optional<TicketStatusEntity> ticketOptional = ticketStatusrepository.findById(requestDTO.getTicketStatusId());
		if (!ticketOptional.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { TICKET_STATUS_ID }));
		}

		entity.setTicketStatus(ticketOptional.get());

		Optional<IssueDetails> issueDetails = issueDetailsRepository.findById(requestDTO.getIssueDetailsId());
		if (!issueDetails.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ISSUE_DETAILS }));
		}

		entity.setIssueMaster(issueDetails.get());

		Optional<IssueFromEntity> issueFrom = issueFromRepository.findById(requestDTO.getIssueFromId());
		if (!ticketOptional.isPresent()) {
			throw new InvalidDataValidation(
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ISSUE_FROM_ID }));
		}

		entity.setIssueFrom(issueFrom.get());
		if (Objects.nonNull(requestDTO.getSlaId())) {
			Optional<SlaMasterEntity> slaMaster = slaMasterRepository.findById(requestDTO.getSlaId());
			if (!slaMaster.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { SLA_ID }));
			}

			entity.setSlaMaster(slaMaster.get());
		}

		if (requestDTO.getKnowledgeBaseId() != null) {
			Optional<KnowledgeBase> knOptional = knowledgeRepository.findById(requestDTO.getKnowledgeBaseId());
			if (!knOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { KB_ID }));
			}

			entity.setKnowledgeBase(knOptional.get());
		}
		/*
		 * Optional<UserGroupEntity> uOptional =
		 * usergroupRepository.findById(requestDTO.getAssignGroupId()); if
		 * (!uOptional.isPresent()) {
		 * ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] {
		 * USER_GROUP_ID })); }
		 */
		if (Objects.nonNull(requestDTO.getPriorityId())) {
			Optional<PriorityMaster> pOptional = priorityMasterRepository.findById(requestDTO.getPriorityId());
			if (!pOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { PRIORITYID }));
			}

			entity.setPriority(pOptional.get());
		}
		if (requestDTO.getProblemReportedId() != null) {
			Optional<ProblemReported> prOptional = problemReportedRepository
					.findById(requestDTO.getProblemReportedId());
			if (!prOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { PROBLEM_REPORTED_ID }));
			}
			entity.setProblemReportedMaster(prOptional.get());
		}

		if (requestDTO.getActionTakenId() != null) {
			Optional<ActionTaken> atOptional = actionTakenRepository.findById(requestDTO.getActionTakenId());
			if (!atOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ACTION_TAKEN_ID }));
			}
			entity.setActionTakenMaster(atOptional.get());
		}

		if (requestDTO.getActualProblemId() != null) {
			Optional<ActualProblem> apOptional = actualProblemRepository.findById(requestDTO.getActualProblemId());
			if (!apOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ACTUAL_PROBLEM_ID }));
			}
			entity.setActualProblemMaster(apOptional.get());
		}

		if (requestDTO.getAssignGroupId() != null) {
			Optional<RoleMaster> rOptional = roleMasterRepository.findById(requestDTO.getAssignGroupId());
			if (!rOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ROLE_ID }));
			}
			entity.setAssignGroup(rOptional.get());
		}

		if (requestDTO.getAssignToId() != null) {
			Optional<UserEntity> uOptional = userRepository.findById(requestDTO.getAssignToId());
			if (!uOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { USER_ID }));
			}
			entity.setAssignTo(uOptional.get());
		}

		if (requestDTO.getSolutionId() != null) {
			Optional<KnowledgeSolution> kOptional = knowledgeSolutionRepository.findById(requestDTO.getSolutionId());
			if (!kOptional.isPresent()) {
				throw new InvalidDataValidation(
						ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { KNOWLEDGE_SOLUTION }));
			}
			entity.setKnowledgeSolution(kOptional.get());
		}

		entity.setCallDisconnect(requestDTO.getCallDisconnect());

		entity.setRequiredField(requestDTO.getRequiredField());

		entity.setRemarks(requestDTO.getRemarks());

		entity.setActive(requestDTO.isActive());

		entity.setTicketNumber(requestDTO.getTicketNumber());

		entity.setEntityTypeId(requestDTO.getEntityTypeId());

		entity.setLicenceNumber(requestDTO.getLicenseNumber());

		entity.setLicenceTypeId(requestDTO.getLicenseTypeId());

		entity.setEmail(requestDTO.getEmail());

		entity.setMobile(requestDTO.getMobile());

		entity.setUnitName(requestDTO.getUnitName());

		entity.setLicenseStatus(requestDTO.getLicenseStatus());

		entity.setNotes(requestDTO.getNotes());

		entity.setAlternativemobileNumber(requestDTO.getAlternativemobileNumber());

		entity.setAddress(requestDTO.getAddress());

		entity.setCreatedbyName(requestDTO.getCreatedbyName());
//		Long l= new Long(0);  
//		int i=l.intValue();  
//		entity.setCreatedBy(l);

		//entity.setDistrict(requestDTO.getDistrict());

		entity.setShopCode(requestDTO.getShopCode());

		entity.setShopName(requestDTO.getShopName());

		//entity.setDistrictCode(requestDTO.getDistrictCode());

		entity.setViewStatus(requestDTO.isViewStatus());

		entity.setTehsilCode(requestDTO.getTehsilCode());

		entity.setTehsilName(requestDTO.getTehsilName());

		entity.setApplicationUuid(requestDTO.getApplicationUuid());

		entity.setUploadApplication(requestDTO.getUploadApplication());

		entity.setImageUrl(requestDTO.getImageUrl());

		entity.setImageUuid(requestDTO.getImageUuid());

		// entity.setUnitCode(requestDTO.getUnitCode());

		entity.setSearchOption(requestDTO.getSearchOption());

		entity.setRaisedBy(requestDTO.getRaisedBy());

		entity.setIssueTypeSH(requestDTO.getIssueTypeSH());

		return entity;
	}

	public CreateTicketResponseDto convertEntityToResponseDTOapp(CreateTicketEntitypayment entity) {
		// CreateTicketResponseDto responseDTO = commonUtil.modalMap(entity,
		// CreateTicketResponseDto.class);
		CreateTicketResponseDto responseDTO = new CreateTicketResponseDto();

		// String createdByUserName =
		// commonDataController.getUserNameById(entity.getCreatedBy());
		// String modifiedByUserName =
		// commonDataController.getUserNameById(entity.getModifiedBy());
		/*
		 * String entityTypeMasterResponse = commonDataController
		 * .getMasterDropDownValueByKey(Constant.ENTITY_TYPE_DROPDOWN_KEY,
		 * entity.getEntityTypeId()); if (StringUtils.isBlank(entityTypeMasterResponse))
		 * { throw new
		 * InvalidDataValidation(ResponseMessageConstant.INVALID_REQUEST_PARM
		 * .getMessage(new Object[] { Constant.ENTITY_TYPE_CODE })); }
		 */

		//////////////////// Existing one//////////

//		LocalDateTime modifiedDate = entity.getModifiedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//		LocalDateTime createdDate = entity.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//				
//		//Duration duration = Duration.between(createdDate, modifiedDate);
//		
//		LocalDateTime tempDateTime = LocalDateTime.from( createdDate );
//		
//		long hours = tempDateTime.until( modifiedDate, ChronoUnit.HOURS );
//		tempDateTime = tempDateTime.plusHours( hours );
//
//		long minutes = tempDateTime.until( modifiedDate, ChronoUnit.MINUTES );
//		tempDateTime = tempDateTime.plusMinutes( minutes );
//		
//		 responseDTO.setDurationInHours(hours+"."+ minutes+" hours");

//////////////Newly add////////////////////////////////
		if (Objects.nonNull(entity.getTicketStatus())) {
			responseDTO.setTicketStatus(entity.getTicketStatus().getTicketstatusname());
			responseDTO.setTicketStatusId(entity.getTicketStatus().getId());
		}

//		try {
//			if (responseDTO.getTicketStatus().equalsIgnoreCase("Closed")
//					|| responseDTO.getTicketStatus().equalsIgnoreCase("Resolved")) {
//				String duration=entity.getDuration();
//				if (!duration.isEmpty() && duration !=null ) {
//					Long timestampduration = Long.valueOf(entity.getDuration());
//					  long hours = TimeUnit.SECONDS.toHours(timestampduration);
//				        long minutes = TimeUnit.SECONDS.toMinutes(timestampduration) % 60;
//					responseDTO.setDurationInHours(hours + "." + minutes + " hours");
//				}
//
//			} else {
//				LocalDateTime currentDateTime = LocalDateTime.now();
//				// Format the date and time
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//				String formattedDateTime = currentDateTime.format(formatter);
//				// Extract hours and minutes
//				int hours = currentDateTime.getHour();
//				int minutes = currentDateTime.getMinute();
//				Long id=entity.getId();
//				Optional<DurationDTO> time = createTicketRepository.getByTimeHRMins(id);
//				responseDTO.setDurationInHours(time.get().getHoursMins() + " hours");
//				//responseDTO.setDurationInHours(hours + "." + minutes + " hours");
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		if (Objects.nonNull(entity.getSlaMaster())) {
			responseDTO.setSla(entity.getSlaMaster().getSla());
			responseDTO.setSlaId(entity.getSlaMaster().getId());
		}

		try {
			String duration = entity.getDuration();
			if (entity.getTicketStatus().getTicketstatusname().equalsIgnoreCase("Closed")
					|| entity.getTicketStatus().getTicketstatusname().equalsIgnoreCase("Resolved")) {
				if (Objects.nonNull(duration)) {
					Long timestampduration = Long.valueOf(entity.getDuration());
					long hours = TimeUnit.SECONDS.toHours(timestampduration);
					long minutes = TimeUnit.SECONDS.toMinutes(timestampduration) % 60;
					String durationhrs = hours + "." + minutes;
					responseDTO.setDurationInHours(durationhrs.toString().trim());
					// SLA Breach Hours
					double durationh = Double.parseDouble(responseDTO.getDurationInHours());
					double slahrs = Double.parseDouble(responseDTO.getSla().toString());
					double Slabreachhrs = durationh - slahrs;
					double slabreachHrsString = Slabreachhrs;
					double slabreachHrs = (slabreachHrsString);
					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					String formattedSlabreachHrs = decimalFormat.format(slabreachHrs);
					if (formattedSlabreachHrs.contains("-")) {
						responseDTO.setSLABreachHrs("0.0");

					} else {
						responseDTO.setSLABreachHrs(String.valueOf(formattedSlabreachHrs));

					}

				}

				else if (Objects.isNull(duration)) {
					LocalDateTime modifiedDate = entity.getModifiedDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDateTime();
					LocalDateTime createdDate = entity.getCreatedDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDateTime();

					// Duration duration = Duration.between(createdDate, modifiedDate);

					LocalDateTime tempDateTime = LocalDateTime.from(createdDate);

					long hours = tempDateTime.until(modifiedDate, ChronoUnit.HOURS);
					tempDateTime = tempDateTime.plusHours(hours);

					long minutes = tempDateTime.until(modifiedDate, ChronoUnit.MINUTES);
					tempDateTime = tempDateTime.plusMinutes(minutes);
					responseDTO.setDurationInHours(hours + "." + minutes);
					// SLA Breach Hours
					double durationh = Double.parseDouble(responseDTO.getDurationInHours());
					double slahrs = Double.parseDouble(responseDTO.getSla().toString());
					double Slabreachhrs = durationh - slahrs;
					double slabreachHrsString = Slabreachhrs;
					double slabreachHrs = (slabreachHrsString);
					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					String formattedSlabreachHrs = decimalFormat.format(slabreachHrs);
					if (formattedSlabreachHrs.contains("-")) {
						responseDTO.setSLABreachHrs("0.0");

					} else {
						responseDTO.setSLABreachHrs(String.valueOf(formattedSlabreachHrs));

					}
				}
			} else {
				LocalDateTime currentDateTime = LocalDateTime.now();
				// Format the date and time
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formattedDateTime = currentDateTime.format(formatter);
				// Extract hours and minutes
				int hours = currentDateTime.getHour();
				int minutes = currentDateTime.getMinute();
				Long id = entity.getId();
				Optional<DurationDTO> time = createTicketRepository.getByTimeHRMins(id);
				String dhrs = time.get().getHoursMins().trim();
				// responseDTO.setDurationInHours(time.get().getHoursMins().trim()
				// +"hours".trim());
				responseDTO.setDurationInHours(dhrs.toString().trim());

				// SLA Breach Hours
				double durationh = Double.parseDouble(responseDTO.getDurationInHours());
				double slahrs = Double.parseDouble(responseDTO.getSla().toString());
				double Slabreachhrs = durationh - slahrs;
				double slabreachHrsString = Slabreachhrs;
				double slabreachHrs = (slabreachHrsString);
				DecimalFormat decimalFormat = new DecimalFormat("#.##");
				String formattedSlabreachHrs = decimalFormat.format(slabreachHrs);
				if (formattedSlabreachHrs.contains("-")) {
					responseDTO.setSLABreachHrs("0.0");

				} else {
					responseDTO.setSLABreachHrs(String.valueOf(formattedSlabreachHrs));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// responseDTO.setCreatedBy(createdByUserName);
		// responseDTO.setModifiedBy(modifiedByUserName);
		responseDTO.setActive(entity.isActive());
		if (Objects.nonNull(entity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
		}
		if (Objects.nonNull(entity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
		}
		if (Objects.nonNull(entity.getCategory())) {
			responseDTO.setCategoryName(entity.getCategory().getCategoryName());
			responseDTO.setCategoryId(entity.getCategory().getId());
		}
		if (Objects.nonNull(entity.getSubCategory())) {
			responseDTO.setSubCategoryName(entity.getSubCategory().getSubCategoryName());
			responseDTO.setSubCategoryId(entity.getSubCategory().getId());
		}
		if (Objects.nonNull(entity.getTicketStatus())) {
			responseDTO.setTicketStatus(entity.getTicketStatus().getTicketstatusname());
			responseDTO.setTicketStatusId(entity.getTicketStatus().getId());
		}

		if (Objects.nonNull(entity.getAssignGroup())) {
			responseDTO.setAssignGroupId(entity.getAssignGroup().getId());
			responseDTO.setAssignGroupName(entity.getAssignGroup().getRoleName());
		}

		if (Objects.nonNull(entity.getIssueFrom())) {
			responseDTO.setIssueFrom(entity.getIssueFrom().getIssueFrom());
			responseDTO.setIssueFromId(entity.getIssueFrom().getId());
		}
		if (Objects.nonNull(entity.getIssueMaster())) {
			responseDTO.setIssueDetails(entity.getIssueMaster().getIssueName());
			responseDTO.setIssueDetailsId(entity.getIssueMaster().getId());
			responseDTO.setIssueType(entity.getIssueMaster().isIssuetype());
			Boolean software = responseDTO.getIssueType();
			Boolean ver = true;
			if (software.equals(ver)) {
				responseDTO.setIssuetypeName("Software");
			}

			else {
				responseDTO.setIssuetypeName("Hardware");
			}
		}
		if (Objects.nonNull(entity.getKnowledgeBase())) {
			responseDTO.setKnowledgeBase(entity.getKnowledgeBase().getKnowledgeSolution());
			responseDTO.setKnowledgeBaseId(entity.getKnowledgeBase().getId());
			responseDTO.setKnowledgeBaseKBID(entity.getKnowledgeBase().getKbId());
		}

//		  if(Objects.nonNull(entity.getSlaMaster())) {
//		  responseDTO.setSla(entity.getSlaMaster().getSla());
//		  responseDTO.setSlaId(entity.getSlaMaster().getId());
//		  }

		if (Objects.nonNull(entity.getActionTakenMaster())) {
			responseDTO.setActionTakenName(entity.getActionTakenMaster().getActionTaken());
			responseDTO.setActionTakenId(entity.getActionTakenMaster().getId());
		}

		if (Objects.nonNull(entity.getActualProblemMaster())) {
			responseDTO.setActualProplemName(entity.getActualProblemMaster().getActualProblem());
			responseDTO.setActualProblemId(entity.getActualProblemMaster().getId());
		}

		if (Objects.nonNull(entity.getProblemReportedMaster())) {
			responseDTO.setProblemReportedName(entity.getProblemReportedMaster().getProblem());
			responseDTO.setProblemReportedId(entity.getProblemReportedMaster().getId());
		}
		if (Objects.nonNull(entity.getAssignTo())) {
			responseDTO.setAssignToName(entity.getAssignTo().getUsername());
			responseDTO.setAssignToId(entity.getAssignTo().getId());
		}

		if (Objects.nonNull(entity.getPriority())) {
			responseDTO.setPriorityName(entity.getPriority().getPriority());
			responseDTO.setPriorityId(entity.getPriority().getId());
		}

		if (Objects.nonNull(entity.getKnowledgeSolution())) {
			responseDTO.setSolutionId(entity.getKnowledgeSolution().getSolutionId());
			responseDTO.setSolutionCategoryId(entity.getKnowledgeSolution().getCategoryId().getId());
			responseDTO.setSolutionCategoryName(entity.getKnowledgeSolution().getCategoryId().getCategoryName());
			responseDTO.setSolutionSubcategoryId(entity.getKnowledgeSolution().getSubcategoryId().getId());
			responseDTO
					.setSolutionSubcategoryName(entity.getKnowledgeSolution().getSubcategoryId().getSubCategoryName());
			responseDTO.setSolutionIssueDetails(entity.getKnowledgeSolution().getIssueDetails());
			responseDTO.setSolutionNotes(entity.getKnowledgeSolution().getNotes());
		}
		responseDTO.setTicketNumber(entity.getTicketNumber());
		responseDTO.setLicenceNumber(entity.getLicenceNumber());
		responseDTO.setLicenseTypeId(entity.getLicenceTypeId());
		responseDTO.setEntityTypeName(entity.getEntityTypeId());
		responseDTO.setRemarks(entity.getRemarks());
		responseDTO.setCallDisconnect(entity.getCallDisconnect());
		responseDTO.setRequiredField(entity.getRequiredField());
		responseDTO.setEmail(entity.getEmail());
		responseDTO.setMobile(entity.getMobile());
		responseDTO.setUnitName(entity.getUnitName());
		responseDTO.setLicenseStatus(entity.getLicenseStatus());
		responseDTO.setNotes(entity.getNotes());
		responseDTO.setAlternativemobileNumber(entity.getAlternativemobileNumber());
		responseDTO.setAddress(entity.getAddress());
		// responseDTO.setCreatedbyName(entity.getCreatedbyName());
		if (entity.getCreatedBy() == 0) {
			responseDTO.setCreatedBy(entity.getCreatedbyName());
		}
//			 else {
//				 responseDTO.setCreatedBy(createdByUserName);	 
//			 }

		responseDTO.setDistrict(entity.getDistrict());
		responseDTO.setShopCode(entity.getShopCode());
		responseDTO.setShopName(entity.getShopName());
		responseDTO.setDistrictCode(entity.getDistrictCode());
		responseDTO.setViewStatus(entity.isViewStatus());
		responseDTO.setTehsilCode(entity.getTehsilCode());
		responseDTO.setTehsilName(entity.getTehsilName());
		responseDTO.setApplicationUuid(entity.getApplicationUuid());
		responseDTO.setUploadApplication(entity.getUploadApplication());
		responseDTO.setImageUrl(entity.getImageUrl());
		responseDTO.setImageUuid(entity.getImageUuid());
		// responseDTO.setUnitCode(entity.getUnitCode());
		responseDTO.setRaisedBy(entity.getRaisedBy());
		responseDTO.setIssueTypesHS(entity.getIssueTypeSH());
		return responseDTO;
	}

}
