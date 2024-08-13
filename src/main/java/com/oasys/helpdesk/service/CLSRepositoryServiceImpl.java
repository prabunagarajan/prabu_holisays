package com.oasys.helpdesk.service;




import static com.oasys.helpdesk.constant.Constant.ASC;
import static com.oasys.helpdesk.constant.Constant.CODE;
import static com.oasys.helpdesk.constant.Constant.DEPARTMENT_NAME;
import static com.oasys.helpdesk.constant.Constant.TICKETSTATUS_NAME;
import static com.oasys.helpdesk.constant.Constant.DESC;
import static com.oasys.helpdesk.constant.Constant.ID;
import static com.oasys.helpdesk.constant.Constant.MODIFIED_DATE;
import static com.oasys.helpdesk.constant.Constant.STATUS;
import static com.oasys.helpdesk.constant.Constant.TYPE;

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
import com.oasys.helpdesk.dto.AssetTypeRequestDTO;
import com.oasys.helpdesk.dto.AssetTypeResponseDTO;
import com.oasys.helpdesk.dto.CLSRequestDTO;
import com.oasys.helpdesk.dto.DepartmentRequestDTO;
import com.oasys.helpdesk.dto.DepartmentResponseDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.dto.TicketstausResponseDTO;
import com.oasys.helpdesk.entity.AssetTypeEntity;
import com.oasys.helpdesk.entity.CLSRegisterEntity;
import com.oasys.helpdesk.entity.DepartmentEntity;
import com.oasys.helpdesk.entity.TicketStatusEntity;
import com.oasys.helpdesk.mapper.AssetTypeMapper;
import com.oasys.helpdesk.mapper.DepartmentMapper;
import com.oasys.helpdesk.mapper.PaginationMapper;
import com.oasys.helpdesk.mapper.TicketstatusMapper;
import com.oasys.helpdesk.repository.AssetTypeRepository;
import com.oasys.helpdesk.repository.CLSRepository;
import com.oasys.helpdesk.repository.DepartmentRepository;
import com.oasys.helpdesk.repository.TicketStatusrepository;
import com.oasys.helpdesk.response.TicketResponseDto;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import com.oasys.helpdesk.utility.MenuPrefix;
import com.oasys.helpdesk.utility.RandomUtil;

import lombok.extern.log4j.Log4j2;
@Service
@Log4j2
public class CLSRepositoryServiceImpl  implements CLSService {
	
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CLSRepository clsRepository;
	
	
	@Autowired
	private PaginationMapper paginationMapper;
	

	@Override
	public GenericResponse addreg(CLSRequestDTO requestDTO)	{

		Optional<CLSRegisterEntity> ticketOptional=clsRepository.findByUsernameIgnoreCase(requestDTO.getUserName().toUpperCase());

		if (ticketOptional.isPresent()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.ALREADY_EXISTS.getMessage(new Object[] { "userName" }));
		}
		
		requestDTO.setId(null);
		CLSRegisterEntity tcEntity = commonUtil.modalMap(requestDTO, CLSRegisterEntity.class);
		clsRepository.save(tcEntity);
		return Library.getSuccessfulResponse(tcEntity, ErrorCode.CREATED.getErrorCode(),ErrorMessages.RECORED_CREATED);
	}
	
	
	

}
