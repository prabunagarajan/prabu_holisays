package com.oasys.helpdesk.service;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.EntityMasterTypeRequestDTO;
import com.oasys.helpdesk.dto.EntityMasterTypeResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.EntityMasterType;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.EntityMasterTypeRepository;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.oasys.helpdesk.constant.Constant.*;
import static com.oasys.helpdesk.service.EntityMasterTypeServiceSupport.getUniqueEntityTypeCode;
import static com.oasys.helpdesk.service.EntityMasterTypeServiceSupport.toEntityMasterType;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Log4j2
@Service
public class EntityMasterTypeService {
    @Autowired
    private EntityMasterTypeRepository entityMasterTypeRepository;
    @Autowired
    private PaginationMapper paginationMapper;
    @Autowired
    private EntityMasterTypeServiceSupport entityMasterTypeServiceSupport;

    public GenericResponse getAllEntityMasterTypes() {
        List<EntityMasterTypeResponseDTO> entityTypeDTOList = entityMasterTypeRepository
                .findAll()
                .stream()
                .map(entityMasterTypeServiceSupport::convertEntityToResponseDTO)
                .collect(toList());
        if (CollectionUtils.isEmpty(entityTypeDTOList)) {
            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
                    ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
        }

        return Library.getSuccessfulResponse(entityTypeDTOList,
                ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
    }

    public GenericResponse getAllEntityNames() {
        List<String> entityTypeDTOList = entityMasterTypeRepository
                .findAll()
                .stream()
                .map(entityMasterTypeServiceSupport::convertEntityToResponseDTO)
                .map(EntityMasterTypeResponseDTO::getEntityName)
                .collect(toList());
        if (CollectionUtils.isEmpty(entityTypeDTOList)) {
            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
                    ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
        }

        return Library.getSuccessfulResponse(entityTypeDTOList,
                ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
    }

    public GenericResponse getAllStatuses() {
        List<String> entityTypeDTOList = Arrays.asList("Active", "InActive");
        if (CollectionUtils.isEmpty(entityTypeDTOList)) {
            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
                    ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
        }

        return Library.getSuccessfulResponse(entityTypeDTOList,
                ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
    }

    public GenericResponse getEntityTypeById(Long id) {
        Optional<EntityMasterType> entityMasterTypeOptional = entityMasterTypeRepository.findById(id);
        if (entityMasterTypeOptional.isPresent())
            return Library.getSuccessfulResponse(entityMasterTypeOptional.get(),
                    ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
        else
            throw new RecordNotFoundException("Record with id " + id + "not found");
    }

    public GenericResponse getAllEntityMasterTypeByNameAndStatus(PaginationRequestDTO paginationDto) {

        Pageable pageable;
        Page<EntityMasterType> list;
        String entityTypeName = null;
        Boolean entityTypeStatus = null;

        if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
            pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
                    Sort.by(Sort.Direction.ASC, paginationDto.getSortField()));
        } else {
            pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
                    Sort.by(Sort.Direction.DESC, paginationDto.getSortField()));
        }
        if (Objects.nonNull(paginationDto.getFilters())) {
            if (Objects.nonNull(paginationDto.getFilters().get(ENTITY_TYPE_NAME))
                    && !paginationDto.getFilters().get(ENTITY_TYPE_NAME).toString().trim().isEmpty()) {
                entityTypeName = String.valueOf(paginationDto.getFilters().get(ENTITY_TYPE_NAME));
            }
            if (Objects.nonNull(paginationDto.getFilters().get(ENTITY_TYPE_STATUS))
                    && !paginationDto.getFilters().get(ENTITY_TYPE_STATUS).toString().trim().isEmpty()) {
                entityTypeStatus = Boolean.valueOf(paginationDto.getFilters().get(ENTITY_TYPE_STATUS).toString());
            }
        }
        list = getByFilter(entityTypeName, entityTypeStatus, pageable);
        if (isNull(list)) {
            list = entityMasterTypeRepository.getAll(pageable);
        }
        if (isNull(list) || list.isEmpty()) {
            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
                    ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
        }
        Page<EntityMasterTypeResponseDTO> finalResponse = list.map(entityMasterTypeServiceSupport::convertEntityToResponseDTO);
        return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
                ErrorMessages.RECORED_FOUND);
    }

    private Page<EntityMasterType> getByFilter(String entityTypeName, Boolean status, Pageable pageable) {
        Page<EntityMasterType> list = null;
        if (Objects.nonNull(entityTypeName) && Objects.nonNull(status)) {
            list = entityMasterTypeRepository.getByEntityNameAndStatus(entityTypeName, status, pageable);
        }

        if (isNull(entityTypeName) && Objects.nonNull(status)) {
            list = entityMasterTypeRepository.getByStatus(status, pageable);
        }

        if (Objects.nonNull(entityTypeName) && isNull(status)) {
            list = entityMasterTypeRepository.getByEntityName(entityTypeName, pageable);
        }
        return list;
    }

    public GenericResponse getEntityCode() {
        String code = getUniqueEntityTypeCode();
        while (true) {
            if (!isUniqueEntityTypeCode(code)) {
                code = getUniqueEntityTypeCode();
            } else
                break;
        }
        return Library.getSuccessfulResponse(code, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
                ErrorMessages.RECORED_FOUND);
    }

    protected boolean isUniqueEntityTypeCode(String uniqueCode) {
        Optional<EntityMasterType> byEntityCode = entityMasterTypeRepository.findByEntityCode(uniqueCode);
        return byEntityCode.isPresent() ? Boolean.FALSE : Boolean.TRUE;
    }

    public GenericResponse createEntityType(EntityMasterTypeRequestDTO entityMasterTypeDTO) {

        if(StringUtils.isEmpty(entityMasterTypeDTO.getEntityCode())) {
            return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
                    ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[]{CODE}));
        }

        Optional<EntityMasterType> entityMasterTypeEntity = entityMasterTypeRepository
                .findByEntityCode(entityMasterTypeDTO.getEntityCode());
        if (entityMasterTypeEntity.isPresent()) {
            return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
                    ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[]{CODE}));
        }

        EntityMasterType entity = entityMasterTypeRepository.save(toEntityMasterType(entityMasterTypeDTO));
        return Library.getSuccessfulResponse(entityMasterTypeServiceSupport.toEntityMasterTypeDTO(entity), ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);

    }

    public GenericResponse updateEntityType(EntityMasterTypeRequestDTO entityMasterTypeDTO) {

        if (isEmpty(entityMasterTypeDTO.getEntityCode())) {
            return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
                    ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[]{ID}));
        }

        Optional<EntityMasterType> entityMasterType = entityMasterTypeRepository.findByEntityCode(entityMasterTypeDTO.getEntityCode());
        if (!entityMasterType.isPresent()) {
            return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
                    ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[]{ID}));
        }
        EntityMasterType masterTypeEntity = entityMasterType.get();
        masterTypeEntity.setStatus(entityMasterTypeDTO.isStatus());
        masterTypeEntity.setEntityName(entityMasterTypeDTO.getEntityName());
        masterTypeEntity.setIsApplicable(entityMasterTypeDTO.getIsParentUnitApplicable());
        entityMasterTypeRepository.save(masterTypeEntity);
        return Library.getSuccessfulResponse(entityMasterTypeServiceSupport.toEntityMasterTypeDTO(masterTypeEntity), ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);

    }
}
