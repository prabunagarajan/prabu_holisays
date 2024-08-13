package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CATEGORY_ID;
import static com.oasys.helpdesk.constant.Constant.GRIEVANCE_KNOWLEDGE_BASE;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.KB_ID;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.GrievanceKbResponseDTO;
import com.oasys.helpdesk.dto.GrievanceKnowledgeBaseRequestDTO;
import com.oasys.helpdesk.dto.KnowledgeBaseResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.GrievanceFaq;
import com.oasys.helpdesk.entity.GrievanceKnowledgeBase;
import com.oasys.helpdesk.entity.GrievancePriorityEntity;
import com.oasys.helpdesk.entity.KnowledgeBase;
import com.oasys.helpdesk.mapper.GrievanceKnowlegdeBaseMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.GrievanceKnowledgeBaseRepository;
import com.oasys.helpdesk.response.GrievanceFaqResponseDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

@Service
public class GrievanceKnowledgeBaseServiceImpl implements GrievanceKnowledgeBaseService {

	@Autowired
	private GrievanceKnowledgeBaseRepository gkbRepository;

	@Autowired
	private GrievanceKnowlegdeBaseMapper gkbMapper;

	@Autowired
	private PaginationMapper paginationMapper;

	@Override
	public GenericResponse createKnowledge(GrievanceKnowledgeBaseRequestDTO knowledgeRequestDto) {
		Optional<GrievanceKnowledgeBase> entityMaster = gkbRepository.findByCode(knowledgeRequestDto.getCode());
		if (entityMaster.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { ID }));
		}

		if (isNull(knowledgeRequestDto.getCode()) || isNull(knowledgeRequestDto.getCategoryId())
				|| isNull(knowledgeRequestDto.getPriority()) || isNull(knowledgeRequestDto.getIssueDetails())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "MANDATORY_FIELDS" }));
		}
		GrievanceKnowledgeBase entity = gkbRepository.save(gkbMapper.toKnowledgeBaseEntity(knowledgeRequestDto));
		return Library.getSuccessfulResponse(gkbMapper.toKnowledgeBaseResponseDTO(entity),
				ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse updateKnowledge(GrievanceKnowledgeBaseRequestDTO knowledgeRequestDto) {

		if (isEmpty(knowledgeRequestDto.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { KB_ID }));
		}

		if (isNull(knowledgeRequestDto.getCategoryId()) || isNull(knowledgeRequestDto.getPriority())
				|| isNull(knowledgeRequestDto.getIssueDetails())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "MANDATORY_FIELDS" }));
		}

		Optional<GrievanceKnowledgeBase> knowledgeBaseOptional = gkbRepository.findById(knowledgeRequestDto.getId());
		if (!knowledgeBaseOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage(new Object[] { KB_ID }));
		}
		GrievanceKnowledgeBase knowledgeBase = gkbRepository
				.save(gkbMapper.toKnowledgeBaseUpdateEntity(knowledgeRequestDto, knowledgeBaseOptional.get()));
		return Library.getSuccessfulResponse(gkbMapper.toKnowledgeBaseResponseDTO(knowledgeBase),
				ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);

	}

	@Override
	public GenericResponse getByCategoryAndIssueDetailsAndStatus(PaginationRequestDTO paginationDto) {

		Pageable pageable;
		Page<GrievanceKnowledgeBase> list;
		Long category = null;
		Long issueDetails = null;
		Boolean status = null;
		String typeofUser= null;

		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Sort.Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Sort.Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get(CATEGORY_ID))
					&& !paginationDto.getFilters().get(CATEGORY_ID).toString().trim().isEmpty()) {
				category = Long.valueOf(paginationDto.getFilters().get(CATEGORY_ID).toString());
			}

			if (Objects.nonNull(paginationDto.getFilters().get("issueDetails"))
					&& !paginationDto.getFilters().get("issueDetails").toString().trim().isEmpty()) {
				issueDetails = Long.valueOf(paginationDto.getFilters().get("issueDetails").toString());
			}

			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
			}
			if (Objects.nonNull(paginationDto.getFilters().get("typeofUser"))
					&& !paginationDto.getFilters().get("typeofUser").toString().trim().isEmpty()) {
				typeofUser = String.valueOf(paginationDto.getFilters().get("typeofUser").toString());
			}
		}
		list = getByFilter(category, issueDetails, status, typeofUser,pageable);
		if (isNull(list)) {
			list = gkbRepository.getAll(pageable);
		}
		if (isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievanceKbResponseDTO> finalResponse = list.map(k -> gkbMapper.toKnowledgeBaseResponseDTO(k));
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	private Page<GrievanceKnowledgeBase> getByFilter(Long category, Long issueDetails, Boolean status,String typeofUser ,
			Pageable pageable) {
		Page<GrievanceKnowledgeBase> list = null;
		if (Objects.nonNull(category) && Objects.nonNull(issueDetails) && Objects.nonNull(status)) {
			list = gkbRepository.getByCategoryAndIssueDetailsAndStatus(category, issueDetails, status, pageable);
		}
		else if (Objects.nonNull(category) && Objects.nonNull(issueDetails) && Objects.nonNull(status) && Objects.nonNull(typeofUser)) {
			list = gkbRepository.getByCategoryAndIssueDetailsAndStatusType(category, issueDetails, status,typeofUser, pageable);
		}

		else if (Objects.nonNull(category) && Objects.nonNull(issueDetails) && Objects.isNull(status)) {
			list = gkbRepository.getByCategoryAndIssueDetails(category, issueDetails, pageable);
		}
		else if (Objects.nonNull(category) && Objects.nonNull(issueDetails) && Objects.isNull(status) && Objects.isNull(typeofUser)) {
			list = gkbRepository.getByCategoryAndIssueDetailsTypeNull(category, issueDetails, pageable);
		}

		else if (Objects.isNull(category) && Objects.nonNull(issueDetails) && Objects.nonNull(status)) {
			list = gkbRepository.getByIssueDetailsAndStatus(issueDetails, status, pageable);
		}
		else if (Objects.isNull(category) && Objects.nonNull(issueDetails) && Objects.nonNull(status)&& Objects.nonNull(typeofUser)) {
			list = gkbRepository.getByIssueDetailsAndStatusType(issueDetails, status,typeofUser, pageable);
		}

		else if (Objects.nonNull(category) && Objects.isNull(issueDetails) && Objects.isNull(status)) {
			list = gkbRepository.getByCategory(category, pageable);
		}
		
		else if (Objects.nonNull(category) && Objects.isNull(issueDetails) && Objects.isNull(status) && Objects.isNull(typeofUser)) {
			list = gkbRepository.getByCategoryType(category, pageable);
		}

		else if (Objects.nonNull(category) && Objects.isNull(issueDetails) && Objects.nonNull(status)) {
			list = gkbRepository.getByCategoryAndStatus(category, status, pageable);
		}
		
		else if (Objects.nonNull(category) && Objects.isNull(issueDetails) && Objects.nonNull(status)&& Objects.nonNull(typeofUser)) {
			list = gkbRepository.getByCategoryAndStatuss(category, status,typeofUser, pageable);
		}

		else if (Objects.isNull(category) && Objects.isNull(issueDetails) && Objects.nonNull(status)) {
			list = gkbRepository.getByStatus(status, pageable);
		}
		
		else if (Objects.isNull(category) && Objects.isNull(issueDetails) && Objects.nonNull(status)&& Objects.nonNull(typeofUser)) {
			list = gkbRepository.getByStatusType(status, typeofUser,pageable);
		}
		
		else if (Objects.isNull(category) && Objects.isNull(issueDetails) && Objects.isNull(status)&& Objects.isNull(typeofUser)) {
			list = gkbRepository.getByStatusAllNull(pageable);
		}
		
		else if (Objects.isNull(category) && Objects.isNull(issueDetails) && Objects.isNull(status)&& Objects.nonNull(typeofUser)) {
			list = gkbRepository.getByStatusNullType(typeofUser, pageable);
		}
		else if (Objects.isNull(category) && Objects.nonNull(issueDetails) && Objects.isNull(status)&& Objects.nonNull(typeofUser)) {
			list = gkbRepository.getByStatusNullTypes(issueDetails,typeofUser, pageable);
		}

		return list;
		
	}

	public GenericResponse getAllKnowledgeBase() {
//		List<GrievanceKbResponseDTO> grievanceTemplateDTOS = gkbRepository
//				.findAll()
//				.stream()
//				.map(k -> gkbMapper.toKnowledgeBaseResponseDTO(k))
//				.collect(toList());
//		if (CollectionUtils.isEmpty(grievanceTemplateDTOS)) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//
//		return Library.getSuccessfulResponse(grievanceTemplateDTOS,
//				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
//		

		List<GrievanceKnowledgeBase> grievanceknowledge = gkbRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(grievanceknowledge)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceKbResponseDTO> ResponseData = grievanceknowledge.stream()
				.map(gkbMapper::toKnowledgeBaseResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getUniqueCode() {
		MenuPrefix prefix = MenuPrefix.getType(GRIEVANCE_KNOWLEDGE_BASE);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<GrievanceKnowledgeBase> grievancekb = gkbRepository.findByCodeIgnoreCase(code);
			if (grievancekb.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getKnowledgeBaseById(Long id) {
		Optional<GrievanceKnowledgeBase> entityMasterTypeOptional = gkbRepository.findById(id);
		if (entityMasterTypeOptional.isPresent())
			return Library.getSuccessfulResponse(gkbMapper.toKnowledgeBaseResponseDTO(entityMasterTypeOptional.get()),
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		else
			throw new RecordNotFoundException("Record with id " + id + "not found");
	}

	@Override
	public GenericResponse getKnowledgeByCatAndIssueDId(Long categoryId, Long issueDetailsId) {
		// List<GrievanceKnowledgeBase> entityMasterTypeOptional =
		// gkbRepository.findByCategoryAndIssueDetailsId(categoryId,issueDetailsId);

		List<GrievanceKbResponseDTO> grievanceKnowledgeDTOS = gkbRepository
				.findByCategoryAndIssueDetailsId(categoryId, issueDetailsId).stream()
				.map(k -> gkbMapper.toKnowledgeBaseResponseDTO(k)).collect(toList());

		if (!CollectionUtils.isEmpty(grievanceKnowledgeDTOS))
			return Library.getSuccessfulResponse(grievanceKnowledgeDTOS, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		else
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
	}

	public GenericResponse updateResolvedCount(Long id) {
		Optional<GrievanceKnowledgeBase> KnowledgeBaseOptional = gkbRepository.findById(id);
		if (!KnowledgeBaseOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.ID }));
		}
		GrievanceKnowledgeBase knowledgeBase = KnowledgeBaseOptional.get();
		Integer count = knowledgeBase.getCount();
		if (Objects.isNull(count)) {
			count = 0;
		}
		knowledgeBase.setCount(count + 1);
		gkbRepository.save(knowledgeBase);
		return Library.getSuccessfulResponse(gkbMapper.toKnowledgeBaseResponseDTO(knowledgeBase),
				ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);

	}

	public GenericResponse getSolutionByIssueDetailsId(Long id) {
		// KnowledgeBaseResponseDTO response = new KnowledgeBaseResponseDTO();
		Integer solutionCount = gkbRepository.getByIssueDetailsId(id);
		if (Objects.nonNull(solutionCount)) {
			if (solutionCount != 0)
				return Library.getSuccessfulResponse(solutionCount, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
						ErrorMessages.RECORED_FOUND);
			else
				return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
						"No Solution Found For Id " + id);
		} else {
			return Library.getFailResponseCode(ErrorCode.FAILURE_RESPONSE.getErrorCode(),
					ErrorMessages.NO_RECORD_FOUND);
		}
	}

	@Override
	public GenericResponse getGrievanceKnowledgeBaseCountByStatus() {

		final Map<String, String> result = gkbRepository.getGrievanceKnowledgeCountByStatus().stream()
				.collect(Collectors.toMap(s -> String.valueOf(s.get("status")), s -> String.valueOf(s.get("COUNT"))));
		return Library.getSuccessfulResponse(result, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	
	}
}
