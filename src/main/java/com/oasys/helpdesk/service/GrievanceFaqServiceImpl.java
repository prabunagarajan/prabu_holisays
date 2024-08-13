package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.GRIEVANCE_CATEGORY_FAQ;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.QUESTION;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
import com.oasys.helpdesk.entity.GrievanceCategoryEntity;
import com.oasys.helpdesk.entity.GrievanceFaq;
import com.oasys.helpdesk.entity.GrievanceIssueDetails;
import com.oasys.helpdesk.mapper.GrievanceFaqMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.GrievanceFaqRepository;
import com.oasys.helpdesk.repository.GrievanceIssueDetailsRepository;
import com.oasys.helpdesk.request.GrievanceFaqRequestDTO;
import com.oasys.helpdesk.response.GrievanceFaqResponseDTO;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GrievanceFaqServiceImpl implements GrievanceFaqService{

	@Autowired
	private GrievanceFaqRepository grievanceFaqRepository;


	@Autowired
	private PaginationMapper paginationMapper;


	@Autowired
	private GrievanceFaqMapper grievanceFaqMapper;

	@Autowired
	private GrievanceIssueDetailsRepository issueDetailsRepository;

	

	@Override
	public GenericResponse createFaq(GrievanceFaqRequestDTO gfaqRequestDto)
	{
		Optional<GrievanceFaq> faqOptional=null;
		
		try {
		faqOptional = grievanceFaqRepository.findByQuestionIgnoreCase(gfaqRequestDto.getQuestion());
		}
		catch(Exception e) {
			e.printStackTrace();
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { QUESTION }));
		}
		
		if (faqOptional.isPresent() && Objects.nonNull(faqOptional.get().getIssueDetails())
				&& gfaqRequestDto.getIssueDetailsId().equals(faqOptional.get().getIssueDetails().getId())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { QUESTION }));
		}
		faqOptional = grievanceFaqRepository.findByCodeIgnoreCase(gfaqRequestDto.getCode());
		if (faqOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}
		gfaqRequestDto.setId(null);
		//		GrievanceFaq faq = grievanceFaqMapper.convertRequestDTOToEntity(gfaqRequestDto, null);
		//		grievanceFaqRepository.save(faq);
		//		if (gfaqRequestDto != null ) {
		Optional<GrievanceIssueDetails> issueDetailsOptional = issueDetailsRepository.findById(gfaqRequestDto.getIssueDetailsId());
		if(!issueDetailsOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.ISSUE_DETAILS }));
		}
		GrievanceFaq grievanceFaq = new GrievanceFaq();
		grievanceFaq.setIssueDetails(issueDetailsOptional.get());
		
		//			grievanceFaq.setCategoryName(gfaqRequestDto.getCategoryName());
		grievanceFaq.setQuestion(gfaqRequestDto.getQuestion());
		grievanceFaq.setAnswer(gfaqRequestDto.getAnswer());
		grievanceFaq.setCode(gfaqRequestDto.getCode());
		grievanceFaq.setStatus(gfaqRequestDto.getStatus());
		grievanceFaq.setTypeofUser(gfaqRequestDto.getTypeofUser());
		grievanceFaqRepository.save(grievanceFaq);
		return Library.getSuccessfulResponse(grievanceFaq, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_CREATED);

	} 


	@Override
	public GenericResponse updateTypeOfUser(GrievanceFaqRequestDTO requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<GrievanceFaq> grievanceFaqOptional = grievanceFaqRepository
				.findById(requestDTO.getId());
		if (!grievanceFaqOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}
		Optional<GrievanceFaq> faqOptional = grievanceFaqRepository.findByQuestionIgnoreCase(requestDTO.getQuestion());
		if (faqOptional.isPresent() && (requestDTO.getId() != faqOptional.get().getId())
				&& Objects.nonNull(faqOptional.get().getIssueDetails())
				&& requestDTO.getIssueDetailsId().equals(faqOptional.get().getIssueDetails().getId())) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { QUESTION }));
		}

		GrievanceFaq grievanceFaq = grievanceFaqOptional.get();
		grievanceFaq.setAnswer(requestDTO.getAnswer());
		grievanceFaq.setQuestion(requestDTO.getQuestion());
		Optional<GrievanceIssueDetails> issueDetailsOptional = issueDetailsRepository.findById(requestDTO.getIssueDetailsId());
		if(!issueDetailsOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { Constant.ISSUE_DETAILS }));
		}
		grievanceFaq.setIssueDetails(issueDetailsOptional.get());
		
		grievanceFaq.setStatus(requestDTO.getStatus());
		grievanceFaq.setTypeofUser(requestDTO.getTypeofUser());
		grievanceFaqRepository.save(grievanceFaq);
		return Library.getSuccessfulResponse(grievanceFaq, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getById(Long Id) {
		Optional<GrievanceFaq> GrievanceFaqEntity = grievanceFaqRepository.findById(Id);
		if (!GrievanceFaqEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(grievanceFaqMapper.convertEntityToResponseDTO(GrievanceFaqEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}



	@Override
	public GenericResponse getAll() {
		List<GrievanceFaq> grievanceFaq = grievanceFaqRepository.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(grievanceFaq)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceFaqResponseDTO> ResponseData = grievanceFaq.stream()
				.map(grievanceFaqMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(GRIEVANCE_CATEGORY_FAQ);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<GrievanceFaq> grievanceFaq = grievanceFaqRepository.findByCodeIgnoreCase(code);
			if (grievanceFaq.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}


	@Override
	public GenericResponse getAllActive() {
		List<GrievanceFaq> configList = grievanceFaqRepository.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(configList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceFaqResponseDTO> ResponseData = configList.stream()
				.map(grievanceFaqMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(ResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}




	@Override
	public GenericResponse searchByTypeOfUser(PaginationRequestDTO paginationDto){
		Pageable pageable = null;
		Page<GrievanceFaq> list = null;
		Long categoryName = null;
		String question = null;
		String typeofUser = null;
		if (StringUtils.isBlank(paginationDto.getSortField())) {
			paginationDto.setSortField(MODIFIED_DATE);
		}
		if (StringUtils.isBlank(paginationDto.getSortOrder())) {
			paginationDto.setSortOrder(DESC);
		}
		if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.ASC, paginationDto.getSortField()));
		} else {
			pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
					Sort.by(Direction.DESC, paginationDto.getSortField()));
		}
		if (Objects.nonNull(paginationDto.getFilters())) {
			if (Objects.nonNull(paginationDto.getFilters().get("categoryId"))
					&& !paginationDto.getFilters().get("categoryId").toString().trim().isEmpty()) {
				try {
					// id = Long.valueOf(paginationDto.getFilters().get(ID).toString());
					categoryName =Long.valueOf(paginationDto.getFilters().get("categoryId").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing categoryName :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			if (Objects.nonNull(paginationDto.getFilters().get("question"))
					&& !paginationDto.getFilters().get("question").toString().trim().isEmpty()) {
				try {
					question = String.valueOf(paginationDto.getFilters().get("question").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing question :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
			
			if (Objects.nonNull(paginationDto.getFilters().get("typeofUser"))
		            && !paginationDto.getFilters().get("typeofUser").toString().trim().isEmpty()) {
		        try {
		        	typeofUser = String.valueOf(paginationDto.getFilters().get("typeofUser").toString());
		        } catch (Exception e) {
		            log.error("error occurred while parsing typeofUser :: {}", e);
		            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		        }
		    }
		}

		GrievanceCategoryEntity categoryNameO = new GrievanceCategoryEntity();
		categoryNameO.setId(categoryName);

		if (Objects.nonNull(categoryName) && Objects.nonNull(question)) {
			list = grievanceFaqRepository.getBySubStringAndStatus(categoryNameO.getId(), question, pageable);
		}
		else if (Objects.nonNull(categoryName) && Objects.isNull(question)) {
			list = grievanceFaqRepository.getByCategoryNameO(categoryNameO.getId(), pageable);
		}
		else if (Objects.isNull(categoryName) && Objects.nonNull(question)) {
			list = grievanceFaqRepository.getBySubString(question, pageable);
		}
		 else if (Objects.nonNull(typeofUser)) {
			    list = grievanceFaqRepository.getByTypeOfUser(typeofUser, pageable);
			}


		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<GrievanceFaqResponseDTO> finalResponse = list.map(grievanceFaqMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}
	
	@Override
	public GenericResponse getByIssueDetailsId(Long issueDetailsId) {
		List<GrievanceFaq> list = grievanceFaqRepository.findByIssueDetailsId(issueDetailsId);
		if (CollectionUtils.isEmpty(list)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<GrievanceFaqResponseDTO> responseList = list.stream().map(grievanceFaqMapper::convertEntityToResponseDTO)
				.collect(Collectors.toList());
		return Library.getSuccessfulResponse(responseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	
	
	
	
	
	
}
