package com.oasys.helpdesk.service;

import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.SHIFT_WORKING_DAYS;

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
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.ShiftWorkingDaysRequestDTO;
import com.oasys.helpdesk.dto.ShiftWorkingDaysResponseDTO;
import com.oasys.helpdesk.entity.ShiftWorkingDaysEntity;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.ShiftWorkingDaysMapper;
import com.oasys.helpdesk.repository.ShiftWorkingDaysRepository;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ShiftWorkingDaysServiceImpl implements ShiftWorkingDaysService {

	@Autowired
	private ShiftWorkingDaysRepository workingDaysRepo;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private ShiftWorkingDaysMapper shiftWorkingDaysMapper;

	@Autowired
	private PaginationMapper paginationMapper;

	@Override
	public GenericResponse addShiftWorkingDays(ShiftWorkingDaysRequestDTO requestDTO) {
		Optional<ShiftWorkingDaysEntity> workingDaysOptional = workingDaysRepo
				.findByCodeIgnoreCase(requestDTO.getCode().toUpperCase());

		List<ShiftWorkingDaysEntity> workingDaysOptional1 = workingDaysRepo
				.findAllByWorkingdays(requestDTO.getWorkingdays());

		if (workingDaysOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { CODE }));
		}

		if (!workingDaysOptional1.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "Working Days" }));
		}
		requestDTO.setId(null);
		ShiftWorkingDaysEntity shiftWorkingEntity = commonUtil.modalMap(requestDTO, ShiftWorkingDaysEntity.class);
		workingDaysRepo.save(shiftWorkingEntity);
		return Library.getSuccessfulResponse(shiftWorkingEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_CREATED);
	}

	@Override
	public GenericResponse updateShiftworkingDays(ShiftWorkingDaysRequestDTO requestDTO) {
		if (Objects.isNull(requestDTO.getId())) {
			return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
					ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		Optional<ShiftWorkingDaysEntity> shiftWorkingDaysOptional = workingDaysRepo
				.findByWorkingdaysAndId(requestDTO.getWorkingdays(), requestDTO.getId());
		
		List<ShiftWorkingDaysEntity> workingDaysOptional1 = workingDaysRepo
				.findAllByWorkingdays(requestDTO.getWorkingdays());
		
		if (!workingDaysOptional1.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "Working Days" }));
		}

		if (shiftWorkingDaysOptional.isPresent()) {
			ShiftWorkingDaysEntity shiftWorkingDaysEntity = shiftWorkingDaysOptional.get();
			shiftWorkingDaysEntity.setWorkingdays(requestDTO.getWorkingdays());
			shiftWorkingDaysEntity.setStatus(requestDTO.getStatus());
			workingDaysRepo.save(shiftWorkingDaysEntity);
			return Library.getSuccessfulResponse(shiftWorkingDaysEntity, ErrorCode.CREATED.getErrorCode(),
					ErrorMessages.RECORED_UPDATED);
		}

		shiftWorkingDaysOptional = workingDaysRepo.findById(requestDTO.getId());
		if (!shiftWorkingDaysOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[] { ID }));
		}

		ShiftWorkingDaysEntity shiftWorkingDaysEntity = shiftWorkingDaysOptional.get();
		shiftWorkingDaysEntity.setWorkingdays(requestDTO.getWorkingdays());
		shiftWorkingDaysEntity.setStatus(requestDTO.getStatus());
		workingDaysRepo.save(shiftWorkingDaysEntity);

		return Library.getSuccessfulResponse(shiftWorkingDaysEntity, ErrorCode.CREATED.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

	@Override
	public GenericResponse getByworkingDays(Long workingdays) {
		List<ShiftWorkingDaysEntity> shiftdays = workingDaysRepo.findAllByWorkingdays(workingdays);
		if (shiftdays == null) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<ShiftWorkingDaysResponseDTO> shiftConfigEntitylist = shiftdays.stream()
				.map(shiftWorkingDaysMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(shiftConfigEntitylist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);

	}

	@Override
	public GenericResponse getById(Long Id) {
		Optional<ShiftWorkingDaysEntity> shiftWorkingDaysEntity = workingDaysRepo.findById(Id);
		if (!shiftWorkingDaysEntity.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		return Library.getSuccessfulResponse(
				shiftWorkingDaysMapper.convertEntityToResponseDTO(shiftWorkingDaysEntity.get()),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAll() {
		List<ShiftWorkingDaysEntity> shiftWorkingEntity = workingDaysRepo.findAllByOrderByModifiedDateDesc();
		if (CollectionUtils.isEmpty(shiftWorkingEntity)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<ShiftWorkingDaysResponseDTO> shiftworkingResponseData = shiftWorkingEntity.stream()
				.map(shiftWorkingDaysMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(shiftworkingResponseData, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllActive() {
		List<ShiftWorkingDaysEntity> workingList = workingDaysRepo.findAllByStatusOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(workingList)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		List<ShiftWorkingDaysResponseDTO> shiftworkResponseList = workingList.stream()
				.map(shiftWorkingDaysMapper::convertEntityToResponseDTO).collect(Collectors.toList());
		return Library.getSuccessfulResponse(shiftworkResponseList, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse searchByWorkingDays(PaginationRequestDTO paginationDto) {
		Pageable pageable = null;
		Page<ShiftWorkingDaysEntity> list = null;
		Long workingdays = null;
		Boolean status = null;
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
			if (Objects.nonNull(paginationDto.getFilters().get("workingdays"))
					&& !paginationDto.getFilters().get("workingdays").toString().trim().isEmpty()) {
				try {
					workingdays = Long.valueOf(paginationDto.getFilters().get("workingdays").toString());
				} catch (Exception e) {
					log.error("error occurred while parsing workingdays :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}

			if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
					&& !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
				try {
					status = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
				} catch (Exception e) {
					log.error("error occurred while parsing status :: {}", e);
					return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
							ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
				}
			}
		}
		if (Objects.nonNull(workingdays) && Objects.nonNull(status)) {
			list = workingDaysRepo.getByWorkingdaysAndStatus(workingdays, status, pageable);
		} else if (Objects.nonNull(workingdays) && Objects.isNull(status)) {
			list = workingDaysRepo.getByWorkingdays(workingdays, pageable);
		} else if (Objects.isNull(workingdays) && Objects.nonNull(status)) {
			list = workingDaysRepo.getByStatus(status, pageable);
		}
		if (Objects.isNull(list)) {
			list = workingDaysRepo.getAll(pageable);
		}
		if (Objects.isNull(list) || list.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}
		Page<ShiftWorkingDaysResponseDTO> dataResponse = list.map(shiftWorkingDaysMapper::convertEntityToResponseDTO);
		return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(dataResponse),
				ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getCode() {
		MenuPrefix prefix = MenuPrefix.getType(SHIFT_WORKING_DAYS);
		String code = prefix.toString() + RandomUtil.getRandomNumber();
		while (true) {
			Optional<ShiftWorkingDaysEntity> assetTypeEntity = workingDaysRepo.findByCodeIgnoreCase(code);
			if (assetTypeEntity.isPresent()) {
				code = prefix.toString() + RandomUtil.getRandomNumber();
			} else {
				break;
			}
		}
		return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

}
