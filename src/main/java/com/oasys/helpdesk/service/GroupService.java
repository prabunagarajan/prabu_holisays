package com.oasys.helpdesk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.entity.FieldAgent;
import com.oasys.helpdesk.entity.Group;
import com.oasys.helpdesk.repository.FieldAgentRepository;
import com.oasys.helpdesk.repository.GroupRepository;
import com.oasys.helpdesk.request.GroupRequestDto;
import com.oasys.helpdesk.response.GroupResponseDto;
import com.oasys.helpdesk.response.UserResponseDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;



@Log4j2
@Service
public class GroupService {

	@Autowired
	GroupRepository helpDeskGroupRepository;
	
	@Autowired
	CommonDataController commonDataController ;
	
	@Autowired
	FieldAgentRepository fieldAgentRepository ;

	public GenericResponse getAllgroup() {
		List<Group> HelpDeskGroupList = helpDeskGroupRepository.findAll();
		if(HelpDeskGroupList==null ||HelpDeskGroupList.size()==0 ) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
	    }
		if (HelpDeskGroupList.size() > 0) {
			List<GroupResponseDto> HelpDeskGroupResponseDtoList = new ArrayList<GroupResponseDto>();
			HelpDeskGroupList.forEach(pt -> {
				HelpDeskGroupResponseDtoList.add(convertHelpDeskGroupToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskGroupResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse getgroupByName(String groupName) {
		List<Group> HelpDeskGroupList = helpDeskGroupRepository.getgroupByName(groupName);
		if(HelpDeskGroupList==null ||HelpDeskGroupList.size()==0 ) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
	    }
		if (HelpDeskGroupList.size() > 0) {
			List<GroupResponseDto> HelpDeskGroupResponseDtoList = new ArrayList<GroupResponseDto>();
			HelpDeskGroupList.forEach(pt -> {
				HelpDeskGroupResponseDtoList.add(convertHelpDeskGroupToDto(pt));
			});

			return Library.getSuccessfulResponse(HelpDeskGroupResponseDtoList,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	public GenericResponse getGroupById(Long id) throws RecordNotFoundException {
		Group helpDeskGroup = helpDeskGroupRepository.getById(id);
		if (helpDeskGroup== null ) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
		if (helpDeskGroup!= null && helpDeskGroup.getId() != null) {
			return Library.getSuccessfulResponse(convertHelpDeskGroupToDto(helpDeskGroup), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	private GroupResponseDto convertHelpDeskGroupToDto(Group helpDeskGroup) {

		GroupResponseDto helpDeskGroupResponseDto = new GroupResponseDto();
		helpDeskGroupResponseDto.setId(helpDeskGroup.getId());
		helpDeskGroupResponseDto.setRev(helpDeskGroup.getRev());
		helpDeskGroupResponseDto.setName(helpDeskGroup.getName());
		helpDeskGroupResponseDto.setType(helpDeskGroup.getType());
		helpDeskGroupResponseDto.setActive(helpDeskGroup.isActive());
		String createduser=commonDataController.getUserNameById(helpDeskGroup.getCreatedBy());
		String modifieduser=commonDataController.getUserNameById(helpDeskGroup.getModifiedBy());
		helpDeskGroupResponseDto.setCreatedBy(createduser);
		helpDeskGroupResponseDto.setCreatedDate(helpDeskGroup.getCreatedDate());
		helpDeskGroupResponseDto.setModifiedBy(modifieduser);
		helpDeskGroupResponseDto.setModifiedDate(helpDeskGroup.getModifiedDate());
		
		return helpDeskGroupResponseDto;

	}
	
	public GenericResponse createGroup(GroupRequestDto helpDeskGroupRequestDto)  throws RecordNotFoundException , Exception
	{
		
		if (helpDeskGroupRequestDto != null ) {
			Group helpDeskGroup = new Group();
			helpDeskGroup.setRev(helpDeskGroupRequestDto.getRev());
			Group group = helpDeskGroupRepository.getByName(helpDeskGroupRequestDto.getName());
			if(group ==null) {
			helpDeskGroup.setName(helpDeskGroupRequestDto.getName());
			}
			else {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ErrorMessages.DUPLICATE_GROUP_NAME_FOUND + group.getName());
			}
			helpDeskGroup.setType(helpDeskGroupRequestDto.getType());
			helpDeskGroup.setActive(helpDeskGroupRequestDto.isActive());
			helpDeskGroupRepository.save(helpDeskGroup);
			
			
			
			if (helpDeskGroup != null && helpDeskGroup.getId() != null) {

					for(Long memberid:helpDeskGroupRequestDto.getMemberIdList()) {
				    FieldAgent helpDeskFieldAgent= new FieldAgent();
					helpDeskFieldAgent.setGroup(helpDeskGroup);
					helpDeskFieldAgent.setMemberId(memberid);
					helpDeskFieldAgent.setActive(helpDeskGroupRequestDto.isActive());
					
				    fieldAgentRepository.save(helpDeskFieldAgent);
			        }
			}
			
			
			
			return Library.getSuccessfulResponse(helpDeskGroup, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
			
		} else {
			throw new RecordNotFoundException();
		}
	} 
	
	public GenericResponse getUserByDesignationCode(AuthenticationDTO authenticationDTO,Locale locale, String code) throws RecordNotFoundException {
		//List<User> userMaster = userRepository.getUserByDesignationCode(code);
		

		List<UserResponseDto> userMaster= commonDataController.getUserByDesignationCode(authenticationDTO,locale,code);
		if (userMaster != null) {
			return Library.getSuccessfulResponse(userMaster,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
	}

	public GenericResponse getUserById(AuthenticationDTO authenticationDTO,Locale locale,Long id) throws RecordNotFoundException {
		UserResponseDto userMasterResponseDto =commonDataController.getUserById(id);
		if (userMasterResponseDto== null ) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
		if (userMasterResponseDto!= null && userMasterResponseDto.getId() != null) {
			return Library.getSuccessfulResponse(userMasterResponseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}

	
}
