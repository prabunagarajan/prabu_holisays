package com.oasys.helpdesk.repository;

public interface BarQrOverviewDTO {
	
	 String getPackagingSize();
	 
	 Integer getNoofBarcodereceived();
	 
	 Integer getNoofQrcodereceived();
	 
	 Integer getNoofBarcodepending();
	 
	 Integer getNoofqrpending(); 
	 
	 String getNoofBarcode();
	 
	 String getNoofQrcode();
	 
     Integer getNoofBarcodedamaged();
	 
	 Integer getNoofQrcodedamaged();
	 
	 String getUnmappedType();


}
