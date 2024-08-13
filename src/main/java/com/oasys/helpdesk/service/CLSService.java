package com.oasys.helpdesk.service;
import com.oasys.helpdesk.dto.CLSRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.TicketstausRequestDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface CLSService {
	
	GenericResponse addreg(CLSRequestDTO requestDto);

}
