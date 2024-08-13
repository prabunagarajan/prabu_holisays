package com.oasys.helpdesk.service;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.entity.LevelMaster;
import com.oasys.helpdesk.repository.LevelMasterRepository;
import com.oasys.helpdesk.request.LevelMasterDto;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class LevelMasterServiceImpl implements LevelMasterService{

	@Autowired
	LevelMasterRepository levelMasterRepository;


	@Override
	public GenericResponse getAllLevel() {
		List<LevelMaster> masterList = levelMasterRepository.findAll();
		if (masterList.size() > 0) {
			List<LevelMasterDto> levelResponse = new ArrayList<>(0);
			masterList.forEach(master ->
					levelResponse.add(convertEntityToDto(master))
			);
			return Library.getSuccessfulResponse(levelResponse,
					ErrorCode.SUCCESS_RESPONSE.getErrorCode(), ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}

	@Override
	public GenericResponse getLevelById(Long id) {
		Optional<LevelMaster> levelMaster = levelMasterRepository.findById(id);
		if (!levelMaster.isPresent() ) {
			throw new RecordNotFoundException("No record found");
		}
		if (levelMaster.get().getId() != null) {
			return Library.getSuccessfulResponse(convertEntityToDto(levelMaster.get()), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}

	@Override
	public GenericResponse createLevel(LevelMasterDto levelRequestDto) {
		LevelMaster levelMaster = new LevelMaster();
		BeanUtils.copyProperties(levelRequestDto, levelMaster);
		levelMaster = levelMasterRepository.save(levelMaster);
		return Library.getSuccessfulResponse(convertEntityToDto(levelMaster), ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_UPDATED);
	}

	private LevelMasterDto convertEntityToDto(LevelMaster levelMaster){
		LevelMasterDto levelRequestDto = new LevelMasterDto();
		BeanUtils.copyProperties(levelMaster, levelRequestDto);
		return levelRequestDto;
	}
}
