package com.oasys.helpdesk.service;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.conf.exception.RecordNotFoundException;
import com.oasys.helpdesk.constant.ErrorCode;
import com.oasys.helpdesk.constant.ErrorMessages;

import com.oasys.helpdesk.repository.TicketRepository;
import com.oasys.helpdesk.repository.TicketHistoryRepository;
import com.oasys.helpdesk.response.DashBoardResponseDto;

import com.oasys.helpdesk.utility.GenericResponse;
import com.oasys.helpdesk.utility.Library;



@Service
public class DashBoardService {

	@Autowired
	TicketRepository helpDeskTicketRepository;
	
	@Autowired
	TicketHistoryRepository ticketHistoryRepository;
	

	public GenericResponse getDashboardCount() {
	
		
		DashBoardResponseDto dashBoardResponseDto = new DashBoardResponseDto();
		dashBoardResponseDto.setTotalticket(helpDeskTicketRepository.getTotalTicketCount());
		dashBoardResponseDto.setOpenticket(helpDeskTicketRepository.getOpenTicketCount());
		dashBoardResponseDto.setInprogress(helpDeskTicketRepository.getInProgressTicketCount());
		//dashBoardResponseDto.setInprogress(ticketHistoryRepository.getInProgressTicketCount());
		dashBoardResponseDto.setClosed(helpDeskTicketRepository.getClosedTicketCount());
		dashBoardResponseDto.setRectified(ticketHistoryRepository.getRectifiedTicketCount());
		if(dashBoardResponseDto.getTotalticket() ==0 ) {
        	throw new RecordNotFoundException("No record found");
		}
        
		if (dashBoardResponseDto.getTotalticket() !=0) {
			return Library.getSuccessfulResponse(dashBoardResponseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
      public GenericResponse getFieldAgentDashboardCount(Long id) {
	
		
		DashBoardResponseDto dashBoardResponseDto = new DashBoardResponseDto();
		dashBoardResponseDto.setTotalticket(helpDeskTicketRepository.getFieldAgentTotalTicketCount(id));
		dashBoardResponseDto.setOpenticket(helpDeskTicketRepository.getFieldAgentOpenTicketCount(id));
		dashBoardResponseDto.setInprogress(ticketHistoryRepository.getFieldAgentInProgressTicketCount(id));
		dashBoardResponseDto.setRectified(ticketHistoryRepository.getFieldAgentRectifiedTicketCount(id));
		dashBoardResponseDto.setP1Count(helpDeskTicketRepository.getP1Count(id));
		dashBoardResponseDto.setP2Count(helpDeskTicketRepository.getP2Count(id));
		if(dashBoardResponseDto.getTotalticket() ==0 ) {
        	throw new RecordNotFoundException("No record found");
		}
        
		if (dashBoardResponseDto.getTotalticket() !=0) {
			return Library.getSuccessfulResponse(dashBoardResponseDto, ErrorCode.SUCCESS_RESPONSE.getErrorCode(),
					ErrorMessages.RECORED_FOUND);
		} else {
			throw new RecordNotFoundException();
		}
	}
	
	
	
	
	





	
}
