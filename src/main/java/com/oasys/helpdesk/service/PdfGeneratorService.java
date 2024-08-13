package com.oasys.helpdesk.service;

import java.io.IOException;

public interface PdfGeneratorService {

	String getTransportPassFilePath(String tpNumber) throws IOException;
	
	String getTransportPassFilePathEAL(String tpNumber) throws IOException;
	
	String getSlipFilePathEAL(String applnNo) throws IOException;

}
