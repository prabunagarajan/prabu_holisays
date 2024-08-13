package com.oasys.helpdesk.mapper;




import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.constant.Constant;
import com.oasys.helpdesk.dto.AssetTypeResponseDTO;
import com.oasys.helpdesk.dto.DepartmentResponseDTO;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.DepartmentEntity;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;

@Component
public class DepartmentMapper {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonDataController commonDataController;
	
	public DepartmentResponseDTO convertEntityToResponseDTO(DepartmentEntity depentity) {
		DepartmentResponseDTO depResponseDTO = commonUtil.modalMap(depentity, DepartmentResponseDTO.class);
		String createdByUserName=commonDataController.getUserNameById(depentity.getCreatedBy());
		String modifiedByUserName=commonDataController.getUserNameById(depentity.getModifiedBy());
		
		depResponseDTO.setCreated_by(createdByUserName);
		depResponseDTO.setModified_by(modifiedByUserName);
		if (Objects.nonNull(depentity.getCreatedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			depResponseDTO.setCreated_date(dateFormat.format(depentity.getCreatedDate()));
		}
		if (Objects.nonNull(depentity.getModifiedDate())) {
			DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
			depResponseDTO.setModified_date(dateFormat.format(depentity.getModifiedDate()));
		}
		return depResponseDTO;
	}
	
	
	
	

}
