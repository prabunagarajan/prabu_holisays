package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.TICKET_NUMBER;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.conf.exception.InvalidDataValidation;
import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationResponseDTO;
import com.oasys.helpdesk.dto.RoleMasterResponseDTO;
import com.oasys.helpdesk.entity.CreateTicketEntity;
import com.oasys.helpdesk.entity.EntityDetails;
import com.oasys.helpdesk.entity.RoleMaster;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.RoleMasterMapper;
import com.oasys.helpdesk.repository.RoleMasterRepository;
import com.oasys.helpdesk.response.CreateTicketResponseDto;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RoleMasterServiceImpl implements RoleMasterService {

	@Autowired
	private RoleMasterRepository roleMasterRepository;

	@Autowired
	private RoleMasterMapper roleMasterMapper;

	@Autowired
	private PaginationMapper paginationMapper;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;

	@Autowired
	private RoleMasterMapper rolemastermapper;

	@Autowired
	private EntityManager entityManager;

	@Override
	public GenericResponse getAll(Boolean helpDeskRoleRequired, Boolean defaultRoleRequired) {
		List<RoleMaster> roleMasterList = null;
		if (Objects.nonNull(helpDeskRoleRequired) && Boolean.FALSE.equals(helpDeskRoleRequired)) {
			if (Objects.nonNull(defaultRoleRequired) && Boolean.TRUE.equals(defaultRoleRequired)) {
				roleMasterList = roleMasterRepository.findByStatusAndHelpdeskRoleOrderByModifiedDateDesc(Boolean.TRUE,
						Boolean.FALSE);
			} else {
				roleMasterList = roleMasterRepository.findByStatusAndDefaultRoleAndHelpdeskRoleOrderByModifiedDateDesc(
						Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
			}
		} else {
			if (Objects.nonNull(defaultRoleRequired) && Boolean.TRUE.equals(defaultRoleRequired)) {
				roleMasterList = roleMasterRepository.findByStatusAndHelpdeskRoleOrderByModifiedDateDesc(Boolean.TRUE,
						Boolean.TRUE);
			} else {
				roleMasterList = roleMasterRepository.findByStatusAndDefaultRoleAndHelpdeskRoleOrderByModifiedDateDesc(
						Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
			}
		}
		if (CollectionUtils.isEmpty(roleMasterList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<RoleMasterResponseDTO> assetBrandResponseList = roleMasterList.stream()
				.map(roleMasterMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetBrandResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse search(PaginationRequestDTO requstdata, AuthenticationDTO authenticationDTO)
			throws ParseException {

		PaginationResponseDTO paginationResponseDTO = new PaginationResponseDTO();

		Long count = this.getCountBysearchfiledRoleMaster(requstdata, authenticationDTO);

		log.info("total count :: {}", count);
		if (count > 0) {
			List<RoleMaster> list = this.getRecordsByFilterRoleMasterDTO(requstdata, authenticationDTO);
			if (CollectionUtils.isEmpty(list)) {
				throw new RecordNotFoundException("No Record Found");
			}

			List<RoleMasterResponseDTO> dtoList = list.stream().map(rolemastermapper::convertEntityToResponseDTO)
					.collect(Collectors.toList());

			paginationResponseDTO.setContents(dtoList);
			paginationResponseDTO.setNumberOfElements(Objects.nonNull(count) ? count.intValue() : null);
			paginationResponseDTO.setTotalElements(count);
			return Library.getSuccessfulResponse(paginationResponseDTO, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException("No Record Found");
		}

	}

	public List<RoleMaster> getRecordsByFilterRoleMasterDTO(PaginationRequestDTO filterRequestDTO,
			AuthenticationDTO authenticationDTO) throws ParseException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<RoleMaster> cq = cb.createQuery(RoleMaster.class);

		Root<RoleMaster> from = cq.from(RoleMaster.class);

		List<Predicate> list = new ArrayList<>();

		TypedQuery<RoleMaster> typedQuery = null;
		addCriteriaRoleMaster(cb, list, filterRequestDTO, from, authenticationDTO);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		cq.distinct(true);
		if (StringUtils.isBlank(filterRequestDTO.getSortField())) {
			filterRequestDTO.setSortField(MODIFIED_DATE);
		}

		if (StringUtils.isNotBlank(filterRequestDTO.getSortOrder())
				&& filterRequestDTO.getSortOrder().equalsIgnoreCase(ASC)) {
			cq.orderBy(cb.asc(from.get(filterRequestDTO.getSortField())));

		} else {
			cq.orderBy(cb.desc(from.get(filterRequestDTO.getSortField())));
		}

		if (Objects.nonNull(filterRequestDTO.getPaginationSize())
				&& Objects.nonNull(filterRequestDTO.getPaginationSize())) {
			typedQuery = entityManager.createQuery(cq)
					.setFirstResult((filterRequestDTO.getPageNo() * filterRequestDTO.getPaginationSize()))
					.setMaxResults(filterRequestDTO.getPaginationSize());
		} else {
			typedQuery = entityManager.createQuery(cq);

		}

		List<RoleMaster> data = typedQuery.getResultList();
		if (CollectionUtils.isEmpty(data)) {
			throw new RecordNotFoundException("No Record Found");
		}

		return data;

	}

	private Long getCountBysearchfiledRoleMaster(PaginationRequestDTO filterRequestDTO,
			AuthenticationDTO authenticationDTO) throws ParseException {
		log.info("getting total count by search fields :: {}", filterRequestDTO.toString());
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<RoleMaster> from = cq.from(RoleMaster.class);
		cq.select(cb.count(from));
		List<Predicate> list = new ArrayList<>();
		addCriteriaRoleMaster(cb, list, filterRequestDTO, from, authenticationDTO);
		cq.where(cb.and(list.toArray(new Predicate[list.size()])));
		Long count = entityManager.createQuery(cq).getSingleResult();
		if (count > 0) {
			return count;
		} else {
			throw new RecordNotFoundException("No Record Found");
		}
	}

	private void addCriteriaRoleMaster(CriteriaBuilder cb, List<Predicate> list, PaginationRequestDTO filterRequstDTO,
			Root<RoleMaster> from, AuthenticationDTO authenticationDTO) throws ParseException {

		if (Objects.nonNull(filterRequstDTO.getFilters().get(ID))
				&& !filterRequstDTO.getFilters().get(ID).toString().trim().isEmpty()) {

			Long id = Long.valueOf(filterRequstDTO.getFilters().get(ID).toString());
			list.add(cb.equal(from.get(ID), id));
		}

		if (Objects.nonNull(filterRequstDTO.getFilters().get("roleName"))
				&& !filterRequstDTO.getFilters().get("roleName").toString().trim().isEmpty()) {

			String RoleName = String.valueOf(filterRequstDTO.getFilters().get("roleName").toString());
			list.add(cb.equal(from.get("roleName"), RoleName));

		}
	}

	@Override
	public GenericResponse get() {
		List<RoleMaster> roleMasterList = roleMasterRepository
				.findByStatusAndDefaultRoleOrderByModifiedDateDesc(Boolean.TRUE, Boolean.FALSE);

		if (CollectionUtils.isEmpty(roleMasterList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<RoleMasterResponseDTO> assetBrandResponseList = roleMasterList.stream()
				.map(roleMasterMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(assetBrandResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse addRoleCode(RoleMasterResponseDTO rolemasterresponsedto) {

		Optional<RoleMaster> roleMasterEntity = roleMasterRepository
				.findByRoleCodeIgnoreCase(rolemasterresponsedto.getRoleCode());

		if (roleMasterEntity.isPresent()) {

			return Library.getFailedfulResponse(roleMasterEntity, ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "ROLE CODE" }));
		}

		roleMasterEntity = roleMasterRepository.findByRoleNameIgnoreCase(rolemasterresponsedto.getRoleName());

		if (roleMasterEntity.isPresent()) {

			return Library.getFailedfulResponse(roleMasterEntity, ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "ROLE NAME" }));
		}

		RoleMaster rolemaster = new RoleMaster();
		rolemaster.setId(null);
		rolemaster.setRoleCode(rolemasterresponsedto.getRoleCode().toUpperCase());
		rolemaster.setRoleName(rolemasterresponsedto.getRoleName());
		rolemaster.setStatus(rolemasterresponsedto.getStatus());
		rolemaster.setDefaultRole(false);
		rolemaster.setHelpdeskRole(true);
		roleMasterRepository.save(rolemaster);
		return Library.getSuccessfulResponse(rolemaster, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse getById(Long id) {
		Optional<RoleMaster> roleMaster = roleMasterRepository.findById(id);

		List<RoleMasterResponseDTO> dto = new ArrayList<RoleMasterResponseDTO>();

		RoleMasterResponseDTO rolemasterresponcedto = new RoleMasterResponseDTO();
		try {

			rolemasterresponcedto.setRoleCode(roleMaster.get().getRoleCode());
			rolemasterresponcedto.setRoleName(roleMaster.get().getRoleName());
			rolemasterresponcedto.setStatus(roleMaster.get().getStatus());
			rolemasterresponcedto.setCreatedById(roleMaster.get().getCreatedBy());
			rolemasterresponcedto.setCreatedDate(roleMaster.get().getCreatedDate().toString());
			rolemasterresponcedto.setModifiedById(roleMaster.get().getModifiedBy());
			rolemasterresponcedto.setModifiedDate(roleMaster.get().getModifiedDate().toString());
			rolemasterresponcedto.setDefaultRole(false);
			rolemasterresponcedto.setIs_helpdesk_role(true);
			Long userid = (long) roleMaster.get().getCreatedBy();
			Long Id = (long) roleMaster.get().getId();
			rolemasterresponcedto.setId(Id);
			String createdByUserName = commonDataController.getUserNameById(userid);
			rolemasterresponcedto.setCreatedByUserName(createdByUserName);
			String modifiedByUserName = commonDataController.getUserNameById(userid);
			rolemasterresponcedto.setModifiedByUserName(modifiedByUserName);
			dto.add(rolemasterresponcedto);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!roleMaster.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(rolemasterresponcedto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse updateRoleMaster(RoleMasterResponseDTO rolemasterresponcedto) {
		if (Objects.isNull(rolemasterresponcedto.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { "ID" }));
		}
		Optional<RoleMaster> roleMasterEntityOptional = roleMasterRepository.findById(rolemasterresponcedto.getId());
		RoleMaster roleMaster = null;
		if (roleMasterEntityOptional.isPresent()) {
			roleMaster = roleMasterEntityOptional.get();
			roleMaster.setRoleCode(rolemasterresponcedto.getRoleCode().toUpperCase());
			roleMaster.setRoleName(rolemasterresponcedto.getRoleName());
			roleMaster.setStatus(rolemasterresponcedto.getStatus());
			roleMaster.setDefaultRole(roleMasterEntityOptional.get().getDefaultRole());
			roleMaster.setHelpdeskRole(roleMasterEntityOptional.get().getHelpdeskRole());
			roleMasterRepository.save(roleMaster);
		} else {
			throw new InvalidDataValidation("Invalid ID");
		}
		return Library.getSuccessfulResponse(roleMaster, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

}
