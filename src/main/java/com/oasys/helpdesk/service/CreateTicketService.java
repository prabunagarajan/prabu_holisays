package com.oasys.helpdesk.service;

import java.text.ParseException;
import java.util.List;

import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO2;
import com.oasys.helpdesk.dto.TicketcounRequest;
import com.oasys.helpdesk.request.CreateTicketDashboardDTO;
import com.oasys.helpdesk.request.CreateTicketRequestDto;
import com.oasys.helpdesk.request.CreateTicketRequestDto2;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface CreateTicketService {

	GenericResponse getAll(String type);

	GenericResponse getByStatus(Long id);

	GenericResponse getById(Long id);

	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO)
			throws ParseException;

	GenericResponse getAllByRequestFilterapp(PaginationRequestDTO2 requestData, AuthenticationDTO authenticationDTO)
			throws ParseException;

	GenericResponse getAllByRequestFilterPaymentAppSearchNew(PaginationRequestDTO2 requestData) throws ParseException;

	GenericResponse getCount(String date, AuthenticationDTO authenticationDTO);

	GenericResponse getCount1(String date, String todate, String issueFrom, AuthenticationDTO authenticationDTO);

	// GenericResponse getCountapp(List<CreateTicketRequestDto> requestDto);

	GenericResponse getCountapp(CreateTicketRequestDto2 requestDto);

	GenericResponse add(CreateTicketRequestDto requestDto);

	GenericResponse addapp(CreateTicketDashboardDTO requestDto);
	
	GenericResponse updateapp(CreateTicketDashboardDTO requestDto);

	GenericResponse listStatusViaApp(List<CreateTicketDashboardDTO> requestDto);

	GenericResponse update(CreateTicketDashboardDTO requestDto);

	GenericResponse updatedesc(CreateTicketDashboardDTO requestDto);

	GenericResponse updatefielder(CreateTicketDashboardDTO requestDto);

	GenericResponse getCount_percentage(AuthenticationDTO authenticationDTO);

	GenericResponse getbyMonth(AuthenticationDTO authenticationDTO);

	GenericResponse getByStatusName(String name, String licenseNumber);

	GenericResponse getByAssignToId(CreateTicketDashboardDTO requestDto);

	GenericResponse getByAssignToIdfromdatetodate(CreateTicketRequestDto requestDto);

	GenericResponse updateAssigneeForSLAExpiredTicket();

	GenericResponse viewReport(PaginationRequestDTO paginationRequestDTO, AuthenticationDTO authenticationDTO)
			throws ParseException;

	GenericResponse getByTicketNumber(String ticketNumber);

	GenericResponse getTicketByLicenseNumber(String licenseNumber);

	GenericResponse FlagLIstAPI(CreateTicketDashboardDTO requestDto);

	GenericResponse UpdateFlag(CreateTicketDashboardDTO requestDto);

	GenericResponse getTicketByPhnOrEmail(String search);

	GenericResponse getByStatusViaApp(String name, Long issueFromId);

	GenericResponse getCountticketstatus(TicketcounRequest requestDto, AuthenticationDTO authenticationDTO);

	GenericResponse getAllByRequestFilterfieldapp(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO)
			throws ParseException;

	GenericResponse getmaindashboard(CreateTicketDashboardDTO requestDto);

	GenericResponse getdistrictwiseticket(CreateTicketDashboardDTO requestDto);

	GenericResponse getdistrictcategorywiseticket(String fromDate, String toDate, String district);

	GenericResponse maindashboardemail();

	GenericResponse getdistrictwiseentityticket(CreateTicketRequestDto requestDto);

	GenericResponse getdistrictwiseshopcodeticket(CreateTicketRequestDto requestDto);

	GenericResponse getBylicencenumber(String licencenumber);

	GenericResponse viewReportincident(PaginationRequestDTO paginationRequestDTO, AuthenticationDTO authenticationDTO)
			throws ParseException;

	GenericResponse viewReportquery(PaginationRequestDTO paginationRequestDTO, AuthenticationDTO authenticationDTO)
			throws ParseException;

	GenericResponse updateviewstausticket(CreateTicketDashboardDTO requestDto);

	GenericResponse getdistrictdashboard(CreateTicketDashboardDTO requestDto);

	GenericResponse getByStatusdistrict(String name, String districtcode);

	GenericResponse getdistrictwiseincident(CreateTicketRequestDto requestDto);

	GenericResponse searchByFilter(PaginationRequestDTO paginationRequestDTO) throws ParseException;

	GenericResponse searchByFilterdistricthadle(PaginationRequestDTO paginationRequestDTO) throws ParseException;

	GenericResponse getCount_percentageincident(AuthenticationDTO authenticationDTO);

	GenericResponse getbyMonthincident(AuthenticationDTO authenticationDTO);

	GenericResponse getResolutionofticketssla12hrs(CreateTicketDashboardDTO requestDto);

	GenericResponse getResolutionofticketssla24hrs(CreateTicketDashboardDTO requestDto);

	GenericResponse getTollFreeSummary(CreateTicketDashboardDTO requestDto);

	GenericResponse getsecuritymanagements(CreateTicketDashboardDTO requestDto);

	GenericResponse getAllByRequestFilterlicensenr(PaginationRequestDTO requestData) throws ParseException;

	GenericResponse getrecoverytimeobjective(CreateTicketDashboardDTO requestDto);

	GenericResponse getrecoverypointobjective(CreateTicketDashboardDTO requestDto);

	GenericResponse getCount_percentage_dashboardprocess(CreateTicketDashboardDTO requestDto,
			AuthenticationDTO authenticationDTO);

	GenericResponse getbyticketDashboardByMonth(AuthenticationDTO authenticationDTO);
	
	GenericResponse getAllByfilter(PaginationRequestDTO paginationRequestDTO) throws ParseException;

	
	
}
