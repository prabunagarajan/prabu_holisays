package com.oasys.helpdesk.service;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.EntityMasterTypeRequestDTO;
import com.oasys.helpdesk.dto.EntityMasterTypeResponseDTO;
import com.oasys.helpdesk.entity.EntityMasterType;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.repository.EntityMasterTypeRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Objects;

@Log4j2
@Component
public class EntityMasterTypeServiceSupport {

    @Autowired
    private EntityMasterTypeRepository entityMasterTypeRepository;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    CommonDataController commonDataController;

    @Autowired
    private PaginationMapper paginationMapper;

    private static final String EMC = "EMC";

    protected EntityMasterTypeResponseDTO toEntityMasterTypeDTO(EntityMasterType entityMasterType) {
        String createdByUserName = commonDataController.getUserNameById(entityMasterType.getCreatedBy());
        String modifiedByUserName = commonDataController.getUserNameById(entityMasterType.getModifiedBy());
        EntityMasterTypeResponseDTO entityMasterTypeResponseDTO = new EntityMasterTypeResponseDTO();

        entityMasterTypeResponseDTO.setSNo(entityMasterType.getId());
        entityMasterTypeResponseDTO.setEntityCode(entityMasterType.getEntityCode());
        entityMasterTypeResponseDTO.setEntityName(entityMasterType.getEntityName());
        entityMasterTypeResponseDTO.setIsParentUnitApplicable(entityMasterType.getIsApplicable());
        entityMasterTypeResponseDTO.setStatus(entityMasterType.isStatus());
        entityMasterTypeResponseDTO.setCreatedBy(createdByUserName);
        entityMasterTypeResponseDTO.setModifiedBy(modifiedByUserName);
        entityMasterTypeResponseDTO.setCreatedDate(String.valueOf(entityMasterType.getCreatedDate()));
        entityMasterTypeResponseDTO.setModifiedDate(String.valueOf(entityMasterType.getModifiedDate()));

        return entityMasterTypeResponseDTO;

    }

    protected static EntityMasterType toEntityMasterType(EntityMasterTypeRequestDTO entityMasterTypeDTO) {
        EntityMasterType entityMasterType = new EntityMasterType();
        entityMasterType.setEntityCode(entityMasterTypeDTO.getEntityCode());
        entityMasterType.setIsApplicable(entityMasterTypeDTO.getIsParentUnitApplicable());
        entityMasterType.setCreatedDate(Date.from(Instant.now()));
        entityMasterType.setModifiedDate(Date.from(Instant.now()));
        entityMasterType.setStatus(entityMasterTypeDTO.isStatus());
        entityMasterType.setEntityName(entityMasterTypeDTO.getEntityName());
        return entityMasterType;
    }

    protected static EntityMasterType toUpdateEntityMasterType(EntityMasterTypeRequestDTO entityMasterTypeDTO) {
        EntityMasterType entityMasterType = new EntityMasterType();
        entityMasterType.setEntityCode(entityMasterTypeDTO.getEntityCode());
        entityMasterType.setStatus(entityMasterTypeDTO.isStatus());
        entityMasterType.setEntityName(entityMasterTypeDTO.getEntityName());
        entityMasterType.setIsApplicable(entityMasterTypeDTO.getIsParentUnitApplicable());
        entityMasterType.setModifiedDate(Date.from(Instant.now()));
        return entityMasterType;
    }

    protected static String getUniqueEntityTypeCode() {
        return MenuPrefix.getType(EMC).toString() + RandomUtil.getRandomNumber();
    }

    protected EntityMasterTypeResponseDTO convertEntityToResponseDTO(EntityMasterType entity) {
        EntityMasterTypeResponseDTO responseDTO = commonUtil.modalMap(entity,
                EntityMasterTypeResponseDTO.class);
        String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
        String modifiedByUserName = commonDataController.getUserNameById(entity.getModifiedBy());

        responseDTO.setCreatedBy(createdByUserName);
        responseDTO.setModifiedBy(modifiedByUserName);
        responseDTO.setEntityCode(entity.getEntityCode());
        responseDTO.setSNo(entity.getId());
        responseDTO.setEntityName(entity.getEntityName());
        if (Objects.nonNull(entity.getCreatedDate())) {
            DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
            responseDTO.setCreatedDate(dateFormat.format(entity.getCreatedDate()));
        }
        if (Objects.nonNull(entity.getModifiedDate())) {
            DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
            responseDTO.setModifiedDate(dateFormat.format(entity.getModifiedDate()));
        }
        return responseDTO;
    }

}
