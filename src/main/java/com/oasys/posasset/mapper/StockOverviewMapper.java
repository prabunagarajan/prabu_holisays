package com.oasys.posasset.mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oasys.helpdesk.utility.CommonDataController;
import com.oasys.helpdesk.utility.CommonUtil;
import com.oasys.posasset.dto.EalRequestMapResponseDTO;
import com.oasys.posasset.entity.EALRequestMapEntity;
import com.oasys.posasset.entity.EALStockEntity;

@Component
public class StockOverviewMapper {
	
	@Autowired
	private static CommonUtil commonUtil;

	@Autowired
	private CommonDataController commonDataController;
	

	
	public EalRequestMapResponseDTO entityToResponseDTO(EALRequestMapEntity entity) {
		//EalRequestMapResponseDTO ealResponseDTO = commonUtil.modalMap(entity, EalRequestMapResponseDTO.class);
		//String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		
		Integer barcodeusage=4;
		
		EalRequestMapResponseDTO ealResponseDTO =new EalRequestMapResponseDTO(); 
		ealResponseDTO.setPackagingSize(entity.getPackagingSize());
		ealResponseDTO.setCartonSize(entity.getCartonSize());
		ealResponseDTO.setNoofBarcode(entity.getNoofBarcode());
		ealResponseDTO.setNoofQrcode(entity.getNoofQrcode());
		ealResponseDTO.setRemarks(entity.getRemarks());
		ealResponseDTO.setEalrequestId(entity.getEalrequestId().getId());
		ealResponseDTO.setUnmappedType(entity.getUnmappedType());
		ealResponseDTO.setCodeType(entity.getCodeType());
		ealResponseDTO.setNoofBarcodepending(entity.getNoofBarcodepending());
		//ealResponseDTO.setNoofBarcodereceived(entity.getNoofBarcodereceived()-barcodeusage);
		ealResponseDTO.setNoofBarcodereceived(entity.getNoofBarcodereceived());
		ealResponseDTO.setNoofQrcodereceived(entity.getNoofQrcodereceived());
		ealResponseDTO.setNoofRollcodereceived(entity.getNoofRollcodereceived());
		ealResponseDTO.setNoofqrpending(entity.getNoofqrpending());
		ealResponseDTO.setStockApplnno(entity.getStockApplnno());
		ealResponseDTO.setStockDate(entity.getStockDate());
		ealResponseDTO.setLicencenumber(entity.getEalrequestId().getLicenseNo());
		return ealResponseDTO;
	}


	
	
	public EalRequestMapResponseDTO entityToResponseDTOstockoverview(EALStockEntity entity) {
		//EalRequestMapResponseDTO ealResponseDTO = commonUtil.modalMap(entity, EalRequestMapResponseDTO.class);
		//String createdByUserName = commonDataController.getUserNameById(entity.getCreatedBy());
		
		//Integer barcodeusage=4;
		
		EalRequestMapResponseDTO ealResponseDTO =new EalRequestMapResponseDTO(); 
		ealResponseDTO.setPackagingSize(entity.getPackagingSize());
		ealResponseDTO.setCartonSize(entity.getCartonSize());
		ealResponseDTO.setNoofBarcode(entity.getNoofBarcode());
		ealResponseDTO.setNoofQrcode(entity.getNoofQrcode());
		ealResponseDTO.setRemarks(entity.getRemarks());
		//ealResponseDTO.setEalrequestId(entity.getEalrequestId().getId());
		ealResponseDTO.setUnmappedType(entity.getUnmappedType());
		ealResponseDTO.setCodeType(entity.getCodeType());
		ealResponseDTO.setNoofBarcodepending(entity.getNoofBarcodepending());
		//ealResponseDTO.setNoofBarcodereceived(entity.getNoofBarcodereceived()-barcodeusage);
		ealResponseDTO.setNoofBarcodereceived(entity.getNoofBarcodereceived());
		ealResponseDTO.setNoofQrcodereceived(entity.getNoofQrcodereceived());
		ealResponseDTO.setNoofRollcodereceived(entity.getNoofRollcodereceived());
		ealResponseDTO.setNoofqrpending(entity.getNoofqrpending());
		ealResponseDTO.setStockApplnno(entity.getStockApplnno());
		ealResponseDTO.setStockDate(entity.getStockDate());
		ealResponseDTO.setLicencenumber(entity.getLicenseNo());
		ealResponseDTO.setEalrequestApplnno(entity.getEalrequestapplno());
		ealResponseDTO.setTotalnumofBarcode(entity.getTotalnumofBarcode());
		ealResponseDTO.setTotalnumofQrcode(entity.getTotalnumofQrcode());
		ealResponseDTO.setCreatedDate(String.valueOf(entity.getCreatedDate()));
		ealResponseDTO.setModifiedDate(String.valueOf(entity.getModifiedDate()));
		return ealResponseDTO;
	}

	
	
	
}

	


