package com.oasys.helpdesk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;
import com.oasys.helpdesk.constant.ResponseMessageConstant;
import com.oasys.helpdesk.dto.SiteVisitStatusDTO;
import com.oasys.helpdesk.dto.YearMasterDTO;
import com.oasys.helpdesk.entity.SiteIssueTypeEntity;
import com.oasys.helpdesk.entity.SiteVisitStatusEntity;
import com.oasys.helpdesk.entity.YearMasterEntity;
import com.oasys.helpdesk.repository.SiteVisitStatusRepository;
import com.oasys.helpdesk.repository.YearMasterRepository;
import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;

@Service
public class YearMasterAndSiteVisitStatusServiceImpl implements YearMasterAndSiteVisitStatusService {

	@Autowired
	private YearMasterRepository yearMasterRepository;

	@Autowired
	private SiteVisitStatusRepository siteVisitStatusRepository;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;

	public GenericResponse createYearMaster(YearMasterEntity yearMasterEntity) {

		YearMasterEntity entity = commonUtil.modalMap(yearMasterEntity, YearMasterEntity.class);

		List<YearMasterEntity> yearMaster = yearMasterRepository.findByYearMaster(yearMasterEntity.getYearCode());
		if (!yearMaster.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
		} else {
			entity.setYearCode(yearMasterEntity.getYearCode());
			entity.setActive(yearMasterEntity.isActive());
			entity = yearMasterRepository.save(entity);
			return Library.getSuccessfulResponse(entity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		}
	}

//	public GenericResponse getByIdYearMaster(Long id) {
//
//		Optional<YearMasterEntity> yearMaster = yearMasterRepository.findById(id);
//		List<YearMasterDTO> Entitylist = new ArrayList<YearMasterDTO>();
//		try {
//			YearMasterDTO entity = new YearMasterDTO();
//			entity.setId(yearMaster.get().getId());
//			entity.setActive(yearMaster.get().isActive());
//			entity.setCreatedBy(yearMaster.get().getCreatedBy());
//			entity.setModifiedBy(yearMaster.get().getModifiedBy());
//			entity.setYearCode(yearMaster.get().getYearCode());
//
//			Long userid = (long) yearMaster.get().getCreatedBy();
//			String createdByUserName = commonDataController.getUserNameById(userid);
//			String modifiedByUserName = commonDataController.getUserNameById(userid);
//			entity.setCreatedByName(createdByUserName);
//			entity.setModifiedByName(modifiedByUserName);
//			entity.setCreatedDate(yearMaster.get().getCreatedDate().toString());
//			entity.setModifiedDate(yearMaster.get().getModifiedDate().toString());
//			Entitylist.add(entity);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if (!yearMaster.isPresent()) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		return Library.getSuccessfulResponse(Entitylist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//
//	}

	public GenericResponse createSiteVisitStatus(SiteVisitStatusEntity siteVisitStatusEntity) {

		SiteVisitStatusEntity entity = commonUtil.modalMap(siteVisitStatusEntity, SiteVisitStatusEntity.class);

		List<SiteVisitStatusEntity> yearMaster = siteVisitStatusRepository
				.findByCodeAndName(siteVisitStatusEntity.getCode(), siteVisitStatusEntity.getName());
		if (!yearMaster.isEmpty()) {
			return Library.getFailResponseCode(ErrorCode.INVALID_DATA.getErrorCode(),
					ResponseMessageConstant.RECORD_ALREADY_EXISTS.getMessage());
		} else {
			entity.setCode(siteVisitStatusEntity.getCode());
			entity.setName(siteVisitStatusEntity.getName());
			entity.setStatus(siteVisitStatusEntity.isStatus());
			entity = siteVisitStatusRepository.save(entity);
			return Library.getSuccessfulResponse(entity, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_CREATED);
		}
	}

//	public GenericResponse getByIdSiteVisitStatus(Long id) {
//
//		Optional<SiteVisitStatusEntity> sitevisitstatus = siteVisitStatusRepository.findById(id);
//		List<SiteVisitStatusDTO> Entitylist = new ArrayList<SiteVisitStatusDTO>();
//		try {
//			SiteVisitStatusDTO entity = new SiteVisitStatusDTO();
//			entity.setId(sitevisitstatus.get().getId());
//			//entity.setActive(sitevisitstatus.get().isActive());
//			entity.setCreatedBy(sitevisitstatus.get().getCreatedBy());
//			entity.setModifiedBy(sitevisitstatus.get().getModifiedBy());
//			entity.setCode(sitevisitstatus.get().getCode());
//			entity.setName(sitevisitstatus.get().getName());
//
//			Long userid = (long) sitevisitstatus.get().getCreatedBy();
//			String createdByUserName = commonDataController.getUserNameById(userid);
//			String modifiedByUserName = commonDataController.getUserNameById(userid);
//			entity.setCreatedByName(createdByUserName);
//			entity.setModifiedByName(modifiedByUserName);
//			entity.setCreatedDate(sitevisitstatus.get().getCreatedDate().toString());
//			entity.setModifiedDate(sitevisitstatus.get().getModifiedDate().toString());
//			Entitylist.add(entity);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if (!sitevisitstatus.isPresent()) {
//			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
//					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
//		}
//		return Library.getSuccessfulResponse(Entitylist, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
//				ErrorMessages.RECORED_FOUND);
//
//	}

	@Override
	public GenericResponse getAllActiveSiteVisitStatus() {
		List<SiteVisitStatusEntity> List = siteVisitStatusRepository
				.findAllByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(List, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}

	@Override
	public GenericResponse getAllActiveYearMaster() {
		List<YearMasterEntity> List = yearMasterRepository
				.findAllByIsActiveOrderByModifiedDateDesc(Boolean.TRUE);
		if (CollectionUtils.isEmpty(List)) {
			return Library.getFailResponseCode(ErrorCode.NO_RECORD_FOUND.getErrorCode(),
					ResponseMessageConstant.NO_RECORD_FOUND.getMessage());
		}

		return Library.getSuccessfulResponse(List, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
				ErrorMessages.RECORED_FOUND);
	}



}
