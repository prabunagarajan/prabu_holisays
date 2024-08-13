package com.oasys.posasset.service.impl;


import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DEPARTMENT_NAME;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.SUBSOL_NAME;
import static com.oasys.helpdesk.constant.Constant.TICKETSTATUS_NAME;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.SubsolutionRequestDTO;
import com.oasys.helpdesk.dto.SubsolutionResponseDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.dto.TicketstausResponseDTO;
import com.oasys.helpdesk.entity.Category;
import com.oasys.helpdesk.entity.IssueDetails;
import com.oasys.helpdesk.entity.SubCategory;
import com.oasys.helpdesk.entity.SubsolutionEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.SubsolutionMapper;
import com.oasys.helpdesk.mapper.TicketstatusMapper;
import com.oasys.helpdesk.repository.CategoryRepository;
import com.oasys.helpdesk.repository.SubCategoryRepository;
import com.oasys.helpdesk.repository.SubsolutionRepository;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.response.IsssueDetresdto;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;
import com.oasys.posasset.entity.SIMEntity;
import com.oasys.posasset.entity.SIMProviderDetEntity;
import com.oasys.posasset.mapper.SIMProviderMapper;
import com.oasys.posasset.repository.SIMProviderRepository;
import com.oasys.posasset.request.SIMProviderRequestDTO;
import com.oasys.posasset.response.SIMProviderResponseDTO;
import com.oasys.posasset.response.SIMResponseDTO;
import com.oasys.posasset.service.SIMproviderService;

import lombok.extern.log4j.Log4j2;
@Service
@Log4j2
public class SIMProviderServiceImpl implements SIMproviderService {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private SIMProviderRepository simproviderRepository;
	
	
	@Autowired
	private PaginationMapper paginationMapper;
	
	
	@Autowired
	private SIMProviderMapper simprovidermapper;
	
	
	
	
	@Override
	public GenericResponse addsimprovider(SIMProviderRequestDTO requestDTO)	{

		Optional<SIMProviderDetEntity> ticketOptional=simproviderRepository.findByProvidernameIgnoreCase(requestDTO.getProviderName().toUpperCase());

		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "providerName" }));
		}
		
		requestDTO.setId(null);
		SIMProviderDetEntity tcEntity = commonUtil.modalMap(requestDTO, SIMProviderDetEntity.class);
		tcEntity.setProvidername(requestDTO.getIproviderName());
		tcEntity.setIprovidername(requestDTO.getIproviderName());
		tcEntity.setStatus(requestDTO.isStatus());
		simproviderRepository.save(tcEntity);
		return Library.getSuccessfulResponse(tcEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
	}
	
	
	@Override
	public GenericResponse getAllActive() {
		List<SIMProviderDetEntity> assetTypeList = simproviderRepository.findAllByStatusOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(assetTypeList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<SIMProviderResponseDTO> assetTypeResponseList = assetTypeList.stream()
				.map(simprovidermapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetTypeResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	
	@Override
	public GenericResponse getAll() {
		List<SIMProviderDetEntity> DepList = simproviderRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(DepList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		
		List<SIMProviderResponseDTO> depResponseList = DepList.stream()
				.map(simprovidermapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(depResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getById(Long id) {
		Optional<SIMProviderDetEntity> depTypeEntity = simproviderRepository.findById(id);
		if (!depTypeEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(simprovidermapper.convertEntityToResponseDTO(depTypeEntity.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}
}
