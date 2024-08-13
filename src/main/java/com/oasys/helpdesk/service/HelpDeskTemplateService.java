package com.oasys.helpdesk.service;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.HelpDeskTemplateRequestDTO;
import com.oasys.helpdesk.dto.HelpDeskTemplateResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.entity.HelpDeskTemplate;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.HelpdeskTemplateRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.oasys.helpdesk.constant.Constant.*;
import static com.oasys.helpdesk.service.HelpDeskTemplateServiceSupport.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.isEmpty;

@Log4j2
@Service
public class HelpDeskTemplateService {
    @Autowired
    private HelpdeskTemplateRepository helpdeskTemplateRepository;
    @Autowired
    private PaginationMapper paginationMapper;
    @Autowired
    private HelpDeskTemplateServiceSupport helpDeskTemplateServiceSupport;

    public GenericResponse getAllHelpDeskTemplates() {
        List<HelpDeskTemplateResponseDTO> helpDeskTemplateDTOS = helpdeskTemplateRepository
                .findAll()
                .stream()
                .map(helpDeskTemplateServiceSupport::toHelpDeskTemplateResponseDTO)
                .collect(toList());
        if (CollectionUtils.isEmpty(helpDeskTemplateDTOS)) {
            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
                    ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
        }

        return Library.getSuccessfulResponse(helpDeskTemplateDTOS,
                ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
    }

    public GenericResponse getHelpDeskTemplateById(Long id) {
        Optional<HelpDeskTemplate> entityMasterTypeOptional = helpdeskTemplateRepository.findById(id);
        if (entityMasterTypeOptional.isPresent())
            return Library.getSuccessfulResponse(helpDeskTemplateServiceSupport.toHelpDeskTemplateResponseDTO(entityMasterTypeOptional.get()),
                    ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
        else
            throw new RecordNotFoundException("Record with id " + id + "not found");
    }

    public GenericResponse getByTemplateNameAndTemplateTypeAndStatus(PaginationRequestDTO paginationDto) {

        Pageable pageable;
        Page<HelpDeskTemplate> list;
        String templateType = null;
        String templateName = null;
        Boolean templateStatus = null;

        if (paginationDto.getSortOrder().equalsIgnoreCase(ASC)) {
            pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
                    Sort.by(Sort.Direction.ASC, paginationDto.getSortField()));
        } else {
            pageable = PageRequest.of(paginationDto.getPageNo(), paginationDto.getPaginationSize(),
                    Sort.by(Sort.Direction.DESC, paginationDto.getSortField()));
        }
        if (Objects.nonNull(paginationDto.getFilters())) {
            if (Objects.nonNull(paginationDto.getFilters().get(TEMPLATE_TYPE))
                    && !paginationDto.getFilters().get(TEMPLATE_TYPE).toString().trim().isEmpty()) {
                templateType = String.valueOf(paginationDto.getFilters().get(TEMPLATE_TYPE));
            }

            if (Objects.nonNull(paginationDto.getFilters().get(TEMPLATE_NAME))
                    && !paginationDto.getFilters().get(TEMPLATE_NAME).toString().trim().isEmpty()) {
                templateName = String.valueOf(paginationDto.getFilters().get(TEMPLATE_NAME));
            }

            if (Objects.nonNull(paginationDto.getFilters().get(STATUS))
                    && !paginationDto.getFilters().get(STATUS).toString().trim().isEmpty()) {
                templateStatus = Boolean.valueOf(paginationDto.getFilters().get(STATUS).toString());
            }
        }
        list = getByFilter(templateType, templateName, templateStatus, pageable);
        if (isNull(list)) {
            list = helpdeskTemplateRepository.getAll(pageable);
        }
        if (isNull(list) || list.isEmpty()) {
            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
                    ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
        }
        Page<HelpDeskTemplateResponseDTO> finalResponse = list.map(helpDeskTemplateServiceSupport::toHelpDeskTemplateResponseDTO);
        return Library.getSuccessfulResponse(paginationMapper.convertToResponseDTO(finalResponse), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
                ErrorMessages.RECORED_FOUND);
    }

    private Page<HelpDeskTemplate> getByFilter(String templateType, String templateName, Boolean status, Pageable pageable) {
        Page<HelpDeskTemplate> list = null;
        if (Objects.nonNull(templateType) && Objects.nonNull(templateName) && Objects.nonNull(status)) {
            list = helpdeskTemplateRepository.getByTemplateNameAndTemplateTypeAndStatus(templateName, templateType, status, pageable);
        }

        if (Objects.isNull(templateType) && Objects.nonNull(templateName) && Objects.nonNull(status)) {
            list = helpdeskTemplateRepository.getByTemplateNameAndStatus(templateName, status, pageable);
        }

        if (Objects.isNull(templateType) && Objects.isNull(templateName) && Objects.nonNull(status)) {
            list = helpdeskTemplateRepository.getByStatus(status, pageable);
        }

        if (Objects.nonNull(templateType) && Objects.nonNull(templateName) && Objects.isNull(status)) {
            list = helpdeskTemplateRepository.getByTemplateNameAndTemplateType(templateName, templateType, pageable);
        }

        if (Objects.nonNull(templateType) && Objects.isNull(templateName) && Objects.isNull(status)) {
            list = helpdeskTemplateRepository.getByTemplateType(templateType, pageable);
        }

        if (Objects.isNull(templateType) && Objects.nonNull(templateName) && Objects.isNull(status)) {
            list = helpdeskTemplateRepository.getByTemplateName(templateName, pageable);
        }

        return list;
    }

    public GenericResponse getAllTemplateTypes() {
        List<String> entityTypeDTOList = getTemplateTypes();
        if (CollectionUtils.isEmpty(entityTypeDTOList)) {
            return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
                    ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
        }

        return Library.getSuccessfulResponse(entityTypeDTOList,
                ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
    }

    public GenericResponse createTemplate(HelpDeskTemplateRequestDTO helpDeskTemplateDTO) {

        Optional<HelpDeskTemplate> entityMasterTypeEntity = helpdeskTemplateRepository
                .findByTemplateId(helpDeskTemplateDTO.getTemplateId());
        if (entityMasterTypeEntity.isPresent()) {
            return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
                    ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[]{TEMPLATE_ID}));
        }

        if (nonNull(helpDeskTemplateDTO.getTemplateType()) && !getTemplateTypes().contains(helpDeskTemplateDTO.getTemplateType())) {
            return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
                    ResponseMessageConstant.INVALID_REQUEST_PARM.getMessage(new Object[]{TEMPLATE_TYPE}));
        }

//        GenericResponse mandatoryCheck = performMandatoryCheck(helpDeskTemplateDTO);
//        if (mandatoryCheck != null)
//            return mandatoryCheck;

        HelpDeskTemplate entity = helpdeskTemplateRepository.save(toHelpDeskTemplateEntity(helpDeskTemplateDTO));
        return Library.getSuccessfulResponse(helpDeskTemplateServiceSupport.toHelpDeskTemplateResponseDTO(entity), ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_CREATED);

    }



    public GenericResponse updateTemplate(HelpDeskTemplateRequestDTO helpDeskTemplateDTO) {

        if (isEmpty(helpDeskTemplateDTO.getTemplateId())) {
            return Library.getFailResponseCode(ErrorCode.BAD_REQUEST.getErrorCode(),
                    ResponseMessageConstant.MANDTORY_REQUEST_PARM.getMessage(new Object[]{TEMPLATE_ID}));
        }

        GenericResponse mandatoryCheck = performMandatoryCheck(helpDeskTemplateDTO);
        if (mandatoryCheck != null)
            return mandatoryCheck;

        Optional<HelpDeskTemplate> entityMasterType = helpdeskTemplateRepository.findByTemplateId(helpDeskTemplateDTO.getTemplateId());
        if (!entityMasterType.isPresent()) {
            return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
                    ResponseMessageConstant.NO_RECORD_FOUND.getMessage(new Object[]{TEMPLATE_ID}));
        }
        HelpDeskTemplate helpDeskTemplate = helpdeskTemplateRepository.save(toUpdateEntityMasterType(entityMasterType.get(), helpDeskTemplateDTO));
        return Library.getSuccessfulResponse(helpDeskTemplateServiceSupport.toHelpDeskTemplateResponseDTO(helpDeskTemplate), ErrorCode.CREATED.getErrorCode(), ErrorMessages.RECORED_UPDATED);

    }

    public GenericResponse getAddress() {
        return Library.getSuccessfulResponse(getFromEmailAddress(), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
                ErrorMessages.RECORED_FOUND);
    }
}
