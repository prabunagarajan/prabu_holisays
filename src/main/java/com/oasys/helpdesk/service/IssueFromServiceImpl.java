package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.ISSUE_TYPE;
import static com.oasys.helpdesk.constant.Constant.TYPE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.AssetTypeResponseDTO;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.IssueFromEntity;
import com.oasys.helpdesk.mapper.IssueFromMapper;
import com.oasys.helpdesk.repository.IssueFromRepository;
import com.oasys.helpdesk.request.IssueFromRequestDto;
import com.oasys.helpdesk.response.IssueFromResponseDto;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class IssueFromServiceImpl implements IssueFromService{
	@Autowired
	IssueFromRepository issueFromRepository;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	IssueFromMapper issueFromMapper;
	
	public static final String ISSUE_FROM= "Issue From";
	
	@Override
	public GenericResponse getIssueFromList() {
	List<IssueFromEntity> IssueFromList = issueFromRepository.findAllByOrderByModifiedDateDesc();
	
	
	if (CollectionUtils.isEmpty(IssueFromList)) {
		return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
				ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
	}
	List<IssueFromResponseDto> IssueDetailsResponseDtoList = IssueFromList.stream()
			.map(issueFromMapper::convertEntityToResponseDTO).collect(Collectors.toList());
	return Library.getSuccessfulResponse(IssueDetailsResponseDtoList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
			ErrorMessages.RECORED_FOUND);
	}
	


	@Override
	public GenericResponse createIssueFrom(IssueFromRequestDto issueFromRequestDto) {
		Optional<IssueFromEntity> issueFromOptional = issueFromRepository.findByIssueFromIgnoreCase(issueFromRequestDto.getIssueFrom());
			
		if(issueFromOptional.isPresent())
		{
				return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
						ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { ISSUE_TYPE }));
			
		}
		
			issueFromOptional = issueFromRepository.findByIssueFromCodeIgnoreCase(issueFromRequestDto.getIssueFromCode());
			if(issueFromOptional.isPresent())
			{
					return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
							ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
				
			}
			
			issueFromRequestDto.setId(null);
			IssueFromEntity issueFromEntity = commonUtil.modalMap(issueFromRequestDto, IssueFromEntity.class);
			issueFromRepository.save(issueFromEntity);
			
			return Library.getSuccessfulResponse(issueFromEntity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
	}
	
	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(ISSUE_FROM);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<IssueFromEntity> issueFromEntity = issueFromRepository.findByIssueFromCodeIgnoreCase(code);
			if (issueFromEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}



	@Override
	public GenericResponse getById(Long id) {
		Optional<IssueFromEntity> issueFromEntity = issueFromRepository.findById(id);
		if (!issueFromEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(issueFromMapper.convertEntityToResponseDTO(issueFromEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}



	@Override
	public GenericResponse updateAssetType(IssueFromRequestDto requestDTO) {
		if(Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<IssueFromEntity> issueFromOptional = issueFromRepository
				.findByIssueFromIgnoreCaseNotInId(requestDTO.getIssueFrom(), requestDTO.getId());
		if (issueFromOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { ISSUE_TYPE }));
		}
		issueFromOptional = issueFromRepository.findById(requestDTO.getId());
		if (!issueFromOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		IssueFromEntity issueFromEntity = issueFromOptional.get();
		issueFromEntity.setIssueFrom(requestDTO.getIssueFrom());
		issueFromEntity.setActive(requestDTO.isActive());
		issueFromRepository.save(issueFromEntity);
		return Library.getSuccessfulResponse(issueFromEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_UPDATED);
	
	}



	@Override
	public GenericResponse getAllActive() {		
		
		List<IssueFromEntity> issueFromList = issueFromRepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(issueFromList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<IssueFromResponseDto> issueFromResponseList = issueFromList.stream()
				.map(issueFromMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(issueFromResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

}
