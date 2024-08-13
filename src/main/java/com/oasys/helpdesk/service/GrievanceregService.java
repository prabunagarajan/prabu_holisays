package com.oasys.helpdesk.service;

import java.text.ParseException;
import javax.validation.Valid;
import com.oasys.helpdesk.dto.GrievanceRegRequestDTO;
import com.oasys.helpdesk.dto.GrievanceTicketStatusDTO;
import com.oasys.helpdesk.dto.PaginationRequestDTO;
import com.oasys.helpdesk.request.CreateTicketRequestDto;
import com.oasys.helpdesk.request.GrievanceOTPVerificationRequestDTO;
import com.oasys.helpdesk.security.AuthenticationDTO;
import com.oasys.helpdesk.utility.GenericResponse;

public interface GrievanceregService {

	GenericResponse addreg(GrievanceRegRequestDTO requestDto);

	GenericResponse getAll();

	GenericResponse getBytoglelist(GrievanceRegRequestDTO requestDto);

	GenericResponse getById(Long id);

	GenericResponse getAllActive();

	GenericResponse getCode();

	GenericResponse getCodetypeofuser(GrievanceRegRequestDTO requestDto);

	GenericResponse getAllByRequestFilter1(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO)
			throws ParseException;

	GenericResponse getAllByRequestFilter(PaginationRequestDTO requestData, AuthenticationDTO authenticationDTO)
			throws ParseException;

	// GenericResponse getCount(String date, AuthenticationDTO authenticationDTO);

	GenericResponse getCount(GrievanceRegRequestDTO requestDto, AuthenticationDTO authenticationDTO);

	GenericResponse update(GrievanceRegRequestDTO requestDto);

	GenericResponse getCount_percentage(AuthenticationDTO authenticationDTO);

	GenericResponse getbyMonth(AuthenticationDTO authenticationDTO);

	GenericResponse updateinspectex(GrievanceRegRequestDTO requestDto);

	GenericResponse getBycommonserach(GrievanceRegRequestDTO requestDto);

	GenericResponse getCount_percentageinspect(GrievanceRegRequestDTO requestDto, AuthenticationDTO authenticationDTO);

	GenericResponse getbyMonthinspect(GrievanceRegRequestDTO requestDto, AuthenticationDTO authenticationDTO);

	GenericResponse sendOTP(String phoneNumber);

	GenericResponse verifyOTP(@Valid GrievanceOTPVerificationRequestDTO otpVerificationDTO);

	GenericResponse FlagLIstAPI(GrievanceRegRequestDTO requestDto);

	GenericResponse UpdateFlag(GrievanceRegRequestDTO requestDto);

	GenericResponse ViewList(GrievanceRegRequestDTO requestDto);

	GenericResponse autoEscalate();

	GenericResponse ListByAll(PaginationRequestDTO paginationRequestDTO);

	GenericResponse getCount_percentage_grievance_dashboardprocess(CreateTicketRequestDto requestDto,
			AuthenticationDTO authenticationDTO);

	GenericResponse getbyGrievanceDashboardByMonth(AuthenticationDTO authenticationDTO);

	GenericResponse addGrievanceTicketStatus(GrievanceTicketStatusDTO requestDTO);
	GenericResponse getGrievanceAllActive();

}
