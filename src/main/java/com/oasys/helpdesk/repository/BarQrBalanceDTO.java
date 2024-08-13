package com.oasys.helpdesk.repository;

public interface BarQrBalanceDTO {
	
	 String getPackagingSize();
	 
	 Integer getNoofBarcodereceived();
	 
	 Integer getNoofQrcodereceived();

	 Integer getNoofBarcodedamaged();
	 
	 Integer getNoofQrcodedamaged();
	 
	 Integer getNoofRollcodereceived();
	 
	 Integer getDispatchNoofQrcodereceived();
	 
	 Integer getDispatchNoofBarcodereceived();
	 
	 Integer getDispatchNoofRollcodereceived();

}
