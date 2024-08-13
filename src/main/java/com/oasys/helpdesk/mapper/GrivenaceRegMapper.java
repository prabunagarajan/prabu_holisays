package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.GrievanceRegResponseDTO;
import com.oasys.helpdesk.entity.AssociatedUserEntity;
import com.oasys.helpdesk.entity.GrievanceregisterEntity;
import com.oasys.helpdesk.entity.UserEntity;
import com.oasys.helpdesk.repository.AssociatedUserRepository;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.repository.UserRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.Library;

@Component
public class GrivenaceRegMapper {
	
	

	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	@Autowired
	CategoryRepository helpDeskTicketCategoryRepository;
	
	@Autowired
	SubCategoryRepository helpDeskTicketSubCategoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AssociatedUserRepository associatedUserRepository;
	
	public GrievanceRegResponseDTO convertEntityToResponseDTO(GrievanceregisterEntity tsEntity) {
		GrievanceRegResponseDTO ticketstatusResponseDTO = commonUtil.modalMap(tsEntity, GrievanceRegResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(tsEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(tsEntity.getModifiedBy());
		
		//ticketstatusResponseDTO.setCategoryId((tsEntity.getCategoryId().getId().toString()));
		ticketstatusResponseDTO.setCreated_by(createdByUserName);
		ticketstatusResponseDTO.setModified_by(modifiedByUserName);
		if (Objects.nonNull(tsEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			ticketstatusResponseDTO.setCreated_date(dateFormat.format(tsEntity.getCreatedDate()));
		}
		if (Objects.nonNull(tsEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			ticketstatusResponseDTO.setModified_date(dateFormat.format(tsEntity.getModifiedDate()));;
		}
		//ticketstatusResponseDTO.setGrieCategory(String.valueOf(tsEntity.getCategoryId().getId()));
		if(Objects.nonNull(tsEntity.getCategoryId())) {
		ticketstatusResponseDTO.setCategoryId(String.valueOf(tsEntity.getCategoryId().getId()));
		ticketstatusResponseDTO.setCategoryname(String.valueOf(tsEntity.getCategoryId().getCategoryName()));
		}
		
		if (Objects.nonNull(tsEntity.getAssigntoid())) {
			Long asigngroup = Long.parseLong(tsEntity.getAssigntoid());
			List<AssociatedUserEntity> assigngroupfrom = associatedUserRepository.findByAssociatedUserId(asigngroup);
			if (!assigngroupfrom.isEmpty()) {
				assigngroupfrom.stream().forEach(action -> {
					ticketstatusResponseDTO.setAssigntoName(action.getAssociateduserName());
					ticketstatusResponseDTO.setUserid(tsEntity.getUserid());
					ticketstatusResponseDTO.setAssignToId(action.getAssociatedUserId().toString());
					ticketstatusResponseDTO.setAssigngroup(action.getUserName());
					ticketstatusResponseDTO.setAssigngroupId(String.valueOf(action.getUser()));
					
					
			    });
			}
		}
		
//		if (Objects.nonNull(tsEntity.getUserid())) {
//
//			Long asigntoid = Long.parseLong(tsEntity.getUserid());
//			List<AssociatedUserEntity> assignfrom = associatedUserRepository.findByUser(asigntoid);
//			if (!assignfrom.isEmpty()) {
//				assignfrom.stream().forEach(action -> {
//					ticketstatusResponseDTO.setAssigntoName(action.getAssociateduserName());
//					ticketstatusResponseDTO.setAssignToId(tsEntity.getAssigntoid());
//				});
//			}
//		}
//		
		
		if (Objects.nonNull(tsEntity.getAssignto())) {

			//ticketstatusResponseDTO.setAssigngroup(tsEntity.getAssignto().getAssignGroup().getRoleName());
			//ticketstatusResponseDTO.setAssigngroupId(tsEntity.getAssignto().getAssignGroup().getId());
			if (Objects.nonNull(tsEntity.getAssignto().getSla())
					&& Objects.nonNull(tsEntity.getAssignto().getSla().getGIssueDetails())) {
				ticketstatusResponseDTO.setIssuedetails(
						String.valueOf(tsEntity.getAssignto().getSla().getGIssueDetails().getIssueName()));
			}
			if (Objects.nonNull(tsEntity.getAssignto().getSla())
					&& Objects.nonNull(tsEntity.getAssignto().getSla().getPriority())) {
				ticketstatusResponseDTO.setPriority(tsEntity.getAssignto().getSla().getPriority().getPriority());
			}
			 //ticketstatusResponseDTO.setAssignToId(String.valueOf(tsEntity.getAssignto().getAssignTo().getId()));
			  // ticketstatusResponseDTO.setAssignToId(String.valueOf(tsEntity.getAssigntoid()));
			//ticketstatusResponseDTO.setPriorityId(String.valueOf(tsEntity.getAssignto().getPriority()));
		}
		ticketstatusResponseDTO.setApplnDate(tsEntity.getAppln_date());
		ticketstatusResponseDTO.setAddressofComplaint(tsEntity.getAddressof_complaint());
		ticketstatusResponseDTO.setDepartment(tsEntity.getDepartment());
		ticketstatusResponseDTO.setDistrict(tsEntity.getDistrict());
		ticketstatusResponseDTO.setEmailId(tsEntity.getEmailid());
		ticketstatusResponseDTO.setGrieDesc(tsEntity.getGrie_desc());
		ticketstatusResponseDTO.setGrievanceId(tsEntity.getGrievanceid());
		ticketstatusResponseDTO.setIssueFrom(tsEntity.getIssuefrom());
		ticketstatusResponseDTO.setLicenceStatus(tsEntity.getLicence_status());
		ticketstatusResponseDTO.setLicenceType(tsEntity.getLicencetype());
		ticketstatusResponseDTO.setLiceneceNumber(tsEntity.getLiceneceNumber());
		ticketstatusResponseDTO.setNameofComplaint(tsEntity.getNameof_complaint());
		ticketstatusResponseDTO.setPhoneNumber(tsEntity.getPhone_number());
		ticketstatusResponseDTO.setReferticNumber(tsEntity.getReferticnumber());
		ticketstatusResponseDTO.setStatus(tsEntity.getStatus());
		ticketstatusResponseDTO.setTypeofUser(tsEntity.getTypeofuser());
		ticketstatusResponseDTO.setUnitName(tsEntity.getUnitname());
		ticketstatusResponseDTO.setUploadDoc(tsEntity.getUploaddoc());
		ticketstatusResponseDTO.setUserName(tsEntity.getUsername());
		ticketstatusResponseDTO.setGrievancetcStatus(tsEntity.getGrievancetcstatus());
		ticketstatusResponseDTO.setResolvegrievance(tsEntity.getResolvegrievance());
		ticketstatusResponseDTO.setQualityresponse(tsEntity.getQuality_response());
		ticketstatusResponseDTO.setGrievanceresolved(tsEntity.getGrievanceresolved());
		ticketstatusResponseDTO.setValuableinput(tsEntity.getValuableinput());
		ticketstatusResponseDTO.setGrieDesc(tsEntity.getGrie_desc());
		ticketstatusResponseDTO.setGrievancetcStatus(tsEntity.getGrievancetcstatus());
		ticketstatusResponseDTO.setResolutiondetails(tsEntity.getResolutiondetails());
		//ticketstatusResponseDTO.setIssuedetails(tsEntity.getIssuedetails());
		ticketstatusResponseDTO.setNotes(tsEntity.getNotes());
		ticketstatusResponseDTO.setUserremarks(tsEntity.getUserremarks());
		ticketstatusResponseDTO.setOfficerremarks(tsEntity.getOfficerremarks());
		ticketstatusResponseDTO.setIssueFrom(tsEntity.getIssuefrom());
		ticketstatusResponseDTO.setPriorityId(tsEntity.getPriority());
		ticketstatusResponseDTO.setSla(tsEntity.getSlaEntity().getSla().toString());
		ticketstatusResponseDTO.setSlaId(tsEntity.getSlaEntity().getId());
		ticketstatusResponseDTO.setUuid(tsEntity.getUuid());
		ticketstatusResponseDTO.setFilename(tsEntity.getFilename());
		ticketstatusResponseDTO.setIssuedetails(tsEntity.getIssuedetails().getIssueName());
		ticketstatusResponseDTO.setIssuedetailsId(tsEntity.getIssuedetails().getId());
		ticketstatusResponseDTO.setDocfileName(tsEntity.getDocfilename());
		ticketstatusResponseDTO.setDocuuid(tsEntity.getDocuuid());
		ticketstatusResponseDTO.setFaqId(tsEntity.getFaqId());
		ticketstatusResponseDTO.setCreatedbyName(tsEntity.getCreatedbyName());
		ticketstatusResponseDTO.setHofficerId(tsEntity.getHofficerId());
		ticketstatusResponseDTO.setUpdatedBy(tsEntity.getUpdatedBy());
		return ticketstatusResponseDTO;
	}


	public GrievanceRegResponseDTO convertEntityToResponseDTOView(GrievanceregisterEntity tsEntity,String updated_By) {
		GrievanceRegResponseDTO ticketstatusResponseDTO = commonUtil.modalMap(tsEntity, GrievanceRegResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(tsEntity.getCreatedBy());
		//String modifiedByUserName=commonDataController.getUserNameById(tsEntity.getModifiedBy());
		
		//ticketstatusResponseDTO.setCategoryId((tsEntity.getCategoryId().getId().toString()));
		ticketstatusResponseDTO.setCreated_by(createdByUserName);
		ticketstatusResponseDTO.setModified_by(updated_By);
		if (Objects.nonNull(tsEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			ticketstatusResponseDTO.setCreated_date(dateFormat.format(tsEntity.getCreatedDate()));
		}
		if (Objects.nonNull(tsEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			ticketstatusResponseDTO.setModified_date(dateFormat.format(tsEntity.getModifiedDate()));;
		}
		//ticketstatusResponseDTO.setGrieCategory(String.valueOf(tsEntity.getCategoryId().getId()));
		if(Objects.nonNull(tsEntity.getCategoryId())) {
		ticketstatusResponseDTO.setCategoryId(String.valueOf(tsEntity.getCategoryId().getId()));
		ticketstatusResponseDTO.setCategoryname(String.valueOf(tsEntity.getCategoryId().getCategoryName()));
		}
		
		
		if (Objects.nonNull(tsEntity.getAssignto())) {
			//ticketstatusResponseDTO.setAssignto(String.valueOf(tsEntity.getAssignto().getId()));
//			ticketstatusResponseDTO.setAssignto(String.valueOf(tsEntity.getAssignto().getAssignTo().getId()));
		    //ticketstatusResponseDTO.setAssigntoName(tsEntity.getAssignto().getAssignTo().getUsername());
			//ticketstatusResponseDTO.setAssigngroup(tsEntity.getAssignto().getAssignGroup().getRoleName());
			//ticketstatusResponseDTO.setAssigngroupId(tsEntity.getAssignto().getAssignGroup().getId());
			if (Objects.nonNull(tsEntity.getAssignto().getSla())
					&& Objects.nonNull(tsEntity.getAssignto().getSla().getGIssueDetails())) {
				ticketstatusResponseDTO.setIssuedetails(
						String.valueOf(tsEntity.getAssignto().getSla().getGIssueDetails().getIssueName()));
			}
			if (Objects.nonNull(tsEntity.getAssignto().getSla())
					&& Objects.nonNull(tsEntity.getAssignto().getSla().getPriority())) {
				ticketstatusResponseDTO.setPriority(tsEntity.getAssignto().getSla().getPriority().getPriority());
			}
			 //ticketstatusResponseDTO.setAssignToId(String.valueOf(tsEntity.getAssignto().getAssignTo().getId()));
			   ticketstatusResponseDTO.setAssignToId(String.valueOf(tsEntity.getAssigntoid()));
			//ticketstatusResponseDTO.setPriorityId(String.valueOf(tsEntity.getAssignto().getPriority()));
		}
	   
		ticketstatusResponseDTO.setApplnDate(tsEntity.getAppln_date());
		ticketstatusResponseDTO.setAddressofComplaint(tsEntity.getAddressof_complaint());
		ticketstatusResponseDTO.setDepartment(tsEntity.getDepartment());
		ticketstatusResponseDTO.setDistrict(tsEntity.getDistrict());
		ticketstatusResponseDTO.setEmailId(tsEntity.getEmailid());
		ticketstatusResponseDTO.setGrieDesc(tsEntity.getGrie_desc());
		ticketstatusResponseDTO.setGrievanceId(tsEntity.getGrievanceid());
		ticketstatusResponseDTO.setIssueFrom(tsEntity.getIssuefrom());
		ticketstatusResponseDTO.setLicenceStatus(tsEntity.getLicence_status());
		ticketstatusResponseDTO.setLicenceType(tsEntity.getLicencetype());
		ticketstatusResponseDTO.setLiceneceNumber(tsEntity.getLiceneceNumber());
		ticketstatusResponseDTO.setNameofComplaint(tsEntity.getNameof_complaint());
		ticketstatusResponseDTO.setPhoneNumber(tsEntity.getPhone_number());
		ticketstatusResponseDTO.setReferticNumber(tsEntity.getReferticnumber());
		ticketstatusResponseDTO.setStatus(tsEntity.getStatus());
		ticketstatusResponseDTO.setTypeofUser(tsEntity.getTypeofuser());
		ticketstatusResponseDTO.setUnitName(tsEntity.getUnitname());
		ticketstatusResponseDTO.setUploadDoc(tsEntity.getUploaddoc());
		ticketstatusResponseDTO.setUserName(tsEntity.getUsername());
		ticketstatusResponseDTO.setGrievancetcStatus(tsEntity.getGrievancetcstatus());
		ticketstatusResponseDTO.setResolvegrievance(tsEntity.getResolvegrievance());
		ticketstatusResponseDTO.setQualityresponse(tsEntity.getQuality_response());
		ticketstatusResponseDTO.setGrievanceresolved(tsEntity.getGrievanceresolved());
		ticketstatusResponseDTO.setValuableinput(tsEntity.getValuableinput());
		ticketstatusResponseDTO.setGrieDesc(tsEntity.getGrie_desc());
		ticketstatusResponseDTO.setGrievancetcStatus(tsEntity.getGrievancetcstatus());
		ticketstatusResponseDTO.setResolutiondetails(tsEntity.getResolutiondetails());
		//ticketstatusResponseDTO.setIssuedetails(tsEntity.getIssuedetails());
		ticketstatusResponseDTO.setNotes(tsEntity.getNotes());
		ticketstatusResponseDTO.setUserremarks(tsEntity.getUserremarks());
		ticketstatusResponseDTO.setOfficerremarks(tsEntity.getOfficerremarks());
		ticketstatusResponseDTO.setUserid(tsEntity.getUserid());
		ticketstatusResponseDTO.setIssueFrom(tsEntity.getIssuefrom());
		ticketstatusResponseDTO.setPriorityId(tsEntity.getPriority());
		ticketstatusResponseDTO.setSla(tsEntity.getSlaEntity().getSla().toString());
		ticketstatusResponseDTO.setSlaId(tsEntity.getSlaEntity().getId());
		ticketstatusResponseDTO.setUuid(tsEntity.getUuid());
		ticketstatusResponseDTO.setFilename(tsEntity.getFilename());
		ticketstatusResponseDTO.setIssuedetails(tsEntity.getIssuedetails().getIssueName());
		ticketstatusResponseDTO.setIssuedetailsId(tsEntity.getIssuedetails().getId());
		ticketstatusResponseDTO.setDocfileName(tsEntity.getDocfilename());
		ticketstatusResponseDTO.setDocuuid(tsEntity.getDocuuid());
		ticketstatusResponseDTO.setFaqId(tsEntity.getFaqId());
		ticketstatusResponseDTO.setCreatedbyName(tsEntity.getCreatedbyName());
		ticketstatusResponseDTO.setHofficerId(tsEntity.getHofficerId());
		ticketstatusResponseDTO.setUpdatedBy(tsEntity.getUpdatedBy());
		return ticketstatusResponseDTO;
	}
	
	
	
	
	
	
	public GrievanceRegResponseDTO convertEntityToResponseDTOList(GrievanceregisterEntity tsEntity) {
		GrievanceRegResponseDTO ticketstatusResponseDTO = commonUtil.modalMap(tsEntity, GrievanceRegResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(tsEntity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(tsEntity.getModifiedBy());
		//ticketstatusResponseDTO.setCategoryId((tsEntity.getCategoryId().getId().toString()));
		ticketstatusResponseDTO.setCreated_by(createdByUserName);
		ticketstatusResponseDTO.setModified_by(modifiedByUserName);
		if (Objects.nonNull(tsEntity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			ticketstatusResponseDTO.setCreated_date(dateFormat.format(tsEntity.getCreatedDate()));
		}
		if (Objects.nonNull(tsEntity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			ticketstatusResponseDTO.setModified_date(dateFormat.format(tsEntity.getModifiedDate()));;
		}
		//ticketstatusResponseDTO.setGrieCategory(String.valueOf(tsEntity.getCategoryId().getId()));
		if(Objects.nonNull(tsEntity.getCategoryId())) {
		ticketstatusResponseDTO.setCategoryId(String.valueOf(tsEntity.getCategoryId().getId()));
		ticketstatusResponseDTO.setCategoryname(String.valueOf(tsEntity.getCategoryId().getCategoryName()));
		}
		
		
		if (Objects.nonNull(tsEntity.getAssignto())) {
			//ticketstatusResponseDTO.setAssignto(String.valueOf(tsEntity.getAssignto().getId()));
//			ticketstatusResponseDTO.setAssignto(String.valueOf(tsEntity.getAssignto().getAssignTo().getId()));
		//	UserEntity Entity = userRepository.getById(Long.valueOf(tsEntity.getAssigntoid()));
		 //   ticketstatusResponseDTO.setAssigntoName(Entity.getUsername());
		//	ticketstatusResponseDTO.setAssigngroup(tsEntity.getAssignto().getAssignGroup().getRoleName());
		//	ticketstatusResponseDTO.setAssigngroupId(tsEntity.getAssignto().getAssignGroup().getId());
			if (Objects.nonNull(tsEntity.getAssignto().getSla())
					&& Objects.nonNull(tsEntity.getAssignto().getSla().getGIssueDetails())) {
				ticketstatusResponseDTO.setIssuedetails(
						String.valueOf(tsEntity.getAssignto().getSla().getGIssueDetails().getIssueName()));
			}
			if (Objects.nonNull(tsEntity.getAssignto().getSla())
					&& Objects.nonNull(tsEntity.getAssignto().getSla().getPriority())) {
				ticketstatusResponseDTO.setPriority(tsEntity.getAssignto().getSla().getPriority().getPriority());
			}
			 //ticketstatusResponseDTO.setAssignToId(String.valueOf(tsEntity.getAssignto().getAssignTo().getId()));
			 //  ticketstatusResponseDTO.setAssignToId(String.valueOf(tsEntity.getAssigntoid()));
			//ticketstatusResponseDTO.setPriorityId(String.valueOf(tsEntity.getAssignto().getPriority()));
		}
		
		if (Objects.nonNull(tsEntity.getAssigntoid())) {
			Long asigngroup = Long.parseLong(tsEntity.getAssigntoid());
			List<AssociatedUserEntity> assigngroupfrom = associatedUserRepository.findByAssociatedUserId(asigngroup);
			if (!assigngroupfrom.isEmpty()) {
				assigngroupfrom.stream().forEach(action -> {
					ticketstatusResponseDTO.setAssigntoName(action.getAssociateduserName());
					ticketstatusResponseDTO.setUserid(tsEntity.getUserid());
					ticketstatusResponseDTO.setAssignToId(tsEntity.getUserid());
					ticketstatusResponseDTO.setAssigngroup(action.getUserName());
					ticketstatusResponseDTO.setAssigngroupId(String.valueOf(action.getUser()));
					
					
			    });
			}
		}
		
	   
		ticketstatusResponseDTO.setApplnDate(tsEntity.getAppln_date());
		ticketstatusResponseDTO.setAddressofComplaint(tsEntity.getAddressof_complaint());
		ticketstatusResponseDTO.setDepartment(tsEntity.getDepartment());
		ticketstatusResponseDTO.setDistrict(tsEntity.getDistrict());
		ticketstatusResponseDTO.setEmailId(tsEntity.getEmailid());
		ticketstatusResponseDTO.setGrieDesc(tsEntity.getGrie_desc());
		ticketstatusResponseDTO.setGrievanceId(tsEntity.getGrievanceid());
		ticketstatusResponseDTO.setIssueFrom(tsEntity.getIssuefrom());
		ticketstatusResponseDTO.setLicenceStatus(tsEntity.getLicence_status());
		ticketstatusResponseDTO.setLicenceType(tsEntity.getLicencetype());
		ticketstatusResponseDTO.setLiceneceNumber(tsEntity.getLiceneceNumber());
		ticketstatusResponseDTO.setNameofComplaint(tsEntity.getNameof_complaint());
		ticketstatusResponseDTO.setPhoneNumber(tsEntity.getPhone_number());
		ticketstatusResponseDTO.setReferticNumber(tsEntity.getReferticnumber());
		ticketstatusResponseDTO.setStatus(tsEntity.getStatus());
		ticketstatusResponseDTO.setTypeofUser(tsEntity.getTypeofuser());
		ticketstatusResponseDTO.setUnitName(tsEntity.getUnitname());
		ticketstatusResponseDTO.setUploadDoc(tsEntity.getUploaddoc());
		ticketstatusResponseDTO.setUserName(tsEntity.getUsername());
		ticketstatusResponseDTO.setGrievancetcStatus(tsEntity.getGrievancetcstatus());
		ticketstatusResponseDTO.setResolvegrievance(tsEntity.getResolvegrievance());
		ticketstatusResponseDTO.setQualityresponse(tsEntity.getQuality_response());
		ticketstatusResponseDTO.setGrievanceresolved(tsEntity.getGrievanceresolved());
		ticketstatusResponseDTO.setValuableinput(tsEntity.getValuableinput());
		ticketstatusResponseDTO.setGrieDesc(tsEntity.getGrie_desc());
		ticketstatusResponseDTO.setGrievancetcStatus(tsEntity.getGrievancetcstatus());
		ticketstatusResponseDTO.setResolutiondetails(tsEntity.getResolutiondetails());
		//ticketstatusResponseDTO.setIssuedetails(tsEntity.getIssuedetails());
		ticketstatusResponseDTO.setNotes(tsEntity.getNotes());
		ticketstatusResponseDTO.setUserremarks(tsEntity.getUserremarks());
		ticketstatusResponseDTO.setOfficerremarks(tsEntity.getOfficerremarks());
		ticketstatusResponseDTO.setUserid(tsEntity.getUserid());
		ticketstatusResponseDTO.setIssueFrom(tsEntity.getIssuefrom());
		ticketstatusResponseDTO.setPriorityId(tsEntity.getPriority());
		ticketstatusResponseDTO.setSla(tsEntity.getSlaEntity().getSla().toString());
		ticketstatusResponseDTO.setSlaId(tsEntity.getSlaEntity().getId());
		ticketstatusResponseDTO.setUuid(tsEntity.getUuid());
		ticketstatusResponseDTO.setFilename(tsEntity.getFilename());
		ticketstatusResponseDTO.setIssuedetails(tsEntity.getIssuedetails().getIssueName());
		ticketstatusResponseDTO.setIssuedetailsId(tsEntity.getIssuedetails().getId());
		ticketstatusResponseDTO.setDocfileName(tsEntity.getDocfilename());
		ticketstatusResponseDTO.setDocuuid(tsEntity.getDocuuid());
		ticketstatusResponseDTO.setFaqId(tsEntity.getFaqId());
		ticketstatusResponseDTO.setCreatedbyName(tsEntity.getCreatedbyName());
		ticketstatusResponseDTO.setHofficerId(tsEntity.getHofficerId());
		ticketstatusResponseDTO.setUpdatedBy(tsEntity.getUpdatedBy());
		return ticketstatusResponseDTO;
	}
	
	
	
	
	
	
	
	

}
