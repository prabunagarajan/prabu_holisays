package com.oasys.helpdesk.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.MasterLicenseTypeResponseDto;
import com.oasys.helpdesk.entity.MasterLicenseTypeEntity;
import com.oasys.helpdesk.utility.CommonDataController;

@Component
public class MasterLicenseTypeMapper {

	@Autowired
	private CommonDataController commondatacontroller;

	public MasterLicenseTypeResponseDto convertEntityToResponseDTO(MasterLicenseTypeEntity masterlicensetypeentity) {

		MasterLicenseTypeResponseDto licenseTypeResponseDto = new MasterLicenseTypeResponseDto();

		licenseTypeResponseDto.setId(masterlicensetypeentity.getId());

		licenseTypeResponseDto.setLincensetypename(masterlicensetypeentity.getLincensetypename());

		licenseTypeResponseDto.setCode(masterlicensetypeentity.getCode());

		licenseTypeResponseDto.setActive(masterlicensetypeentity.isActive());

		String createdUser = commondatacontroller.getUserNameById(masterlicensetypeentity.getCreatedBy());
		String modifiedUser = commondatacontroller.getUserNameById(masterlicensetypeentity.getModifiedBy());

		licenseTypeResponseDto.setCreated_by(createdUser);
		if (Objects.nonNull(masterlicensetypeentity.getCreatedDate())) {

			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);

			licenseTypeResponseDto.setCreated_date(dateFormat.format(masterlicensetypeentity.getCreatedDate()));
		}

		licenseTypeResponseDto.setModified_by(modifiedUser);
		if (Objects.nonNull(masterlicensetypeentity.getModifiedDate())) {

			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);

			licenseTypeResponseDto.setCreated_date(dateFormat.format(masterlicensetypeentity.getModifiedDate()));
		}

		return licenseTypeResponseDto;

	}
}
