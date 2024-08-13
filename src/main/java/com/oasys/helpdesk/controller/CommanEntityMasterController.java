package com.oasys.helpdesk.controller;

import java.util.Calendar;
import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.dto.Brandlabeldto;
import com.oasys.helpdesk.dto.CommanMasterUnitCodeDTO;
import com.oasys.helpdesk.dto.CommaonMasterDistrictDto;
import com.oasys.helpdesk.dto.CommonMasterRequestDTO;
import com.oasys.helpdesk.dto.DownloadDTO;
import com.oasys.helpdesk.dto.DownlofileDTO;
import com.oasys.helpdesk.dto.EntityDTO;
import com.oasys.helpdesk.dto.LicenceDTO;
import com.oasys.helpdesk.dto.UMsearchDTO;
import com.oasys.helpdesk.dto.UploadDTO;
import com.oasys.helpdesk.dto.UserDto;
import com.oasys.helpdesk.service.CommonMasterService;
import com.oasys.helpdesk.utility.GenericResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@Api(value = "HelpDeskData", description = "This controller contain all operations for Department Module")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 409, message = "Conflict occurred") })

@RequestMapping("/commonmaster")
@Log4j2
public class CommanEntityMasterController {

	@Autowired
	private CommonMasterService commonmasterservice;

	@PostMapping("/entitytype")
	public String Entitytype(@Valid @RequestBody CommonMasterRequestDTO requestDTO) {
		long millisstart = System.currentTimeMillis();
		System.out.println("BEFORE HIT-----" + millisstart);
		log.info("BEFORE HIT-----" + millisstart);
		String response = commonmasterservice.APIcall(requestDTO);
		long millisend = System.currentTimeMillis();
		System.out.println("After HIT-----" + millisend);
		log.info("After HIT-----" + millisend);
        Long totaltime=(millisend)-(millisstart);
        System.out.println("Total Time EntityTypeAPI TAKEN:::" + totaltime);
        log.info("Total Time EntityTypeAPI TAKEN:::" + totaltime);
        log.info("ENDTIME:::" + millisend +"STARTTIME::::"+millisstart + "::::TOTALTIME::::" + (millisend-millisstart));
		return response;
	}

	@PostMapping("/district")
	public String Districtentity(@Valid @RequestBody CommaonMasterDistrictDto requestDTO) {

		String response = commonmasterservice.APIcallDistrict(requestDTO);

		return response;
	}

	@PostMapping("/taluk")
	public String Talukentity(@Valid @RequestBody CommaonMasterDistrictDto requestDTO) {

		String response = commonmasterservice.APIcallTaluk(requestDTO);

		return response;
	}

	@PostMapping("/country")
	public String Countryentity(@Valid @RequestBody CommaonMasterDistrictDto requestDTO) {

		String response = commonmasterservice.APIcallCountry(requestDTO);

		return response;
	}

	@PostMapping("/licencemanagement")
	public String LicenceManagement(@Valid @RequestBody CommaonMasterDistrictDto requestDTO) {

		String response = commonmasterservice.APIcallLicence(requestDTO);

		return response;
	}

	@PostMapping("/uploaddocument")
	public String UploadDocument(@Valid @RequestBody UploadDTO requestDTO) {

		String response = commonmasterservice.APIcalluploaddoucment(requestDTO);

		return response;
	}

	@PostMapping("/downloaddocument")
	/// public HashMap<String, String> DownloadDocument(@Valid @RequestBody
	/// DownloadDTO requestDTO) {
	public String DownloadDocument(@Valid @RequestBody DownloadDTO requestDTO) {
		String response = commonmasterservice.APIcalldownloaddoucment(requestDTO);

		return response;
	}

	@PostMapping("/downloadfile")
	/// public HashMap<String, String> DownloadDocument(@Valid @RequestBody
	/// DownloadDTO requestDTO) {
	public String Downloadfile(@Valid @RequestBody DownlofileDTO requestDTO) {
		String response = commonmasterservice.APIcalldownloadfile(requestDTO);

		return response;
	}

	@PostMapping("/brandlabel")
	public String Brandlabel(@Valid @RequestBody Brandlabeldto requestDTO) {

		String response = commonmasterservice.Barandlabel(requestDTO);

		return response;
	}

	@PostMapping("/commonmasterlogin")
	public HashMap<String, String> Commonmasterlogin(@Valid @RequestBody LicenceDTO requestDTO) {

		HashMap<String, String> response = commonmasterservice.APIlogincall(requestDTO);

		return response;
	}

	@PostMapping("/serachliceneapp")
	public String serachliceneapp(@Valid @RequestBody LicenceDTO requestDTO) {

		String response = commonmasterservice.APIsearchcall(requestDTO);

		return response;
	}

	@PostMapping("/search")
	public GenericResponse Usermanagementsearch(@Valid @RequestBody UMsearchDTO requestDTO) {

		GenericResponse response = commonmasterservice.Usermanagementsearch(requestDTO);

		return response;
	}

	@GetMapping("/getByRole/{id}")
	public GenericResponse getByRole(@PathVariable("id") String id) {

		GenericResponse response = commonmasterservice.getByRole(id);

		return response;
	}

	@PostMapping("/alluser")
	public String Talukentity(@Valid @RequestBody UserDto requestDTO) {

		String response = commonmasterservice.Usercall(requestDTO);

		return response;
	}

	@PostMapping("/getuserbyrole")
	public String Getuserbyrole(@Valid @RequestBody UserDto requestDTO) {

		String response = commonmasterservice.APIcallgetuserbyrole(requestDTO);

		return response;
	}

	@PostMapping("/getrolebyusertype")
	public String Getuserbyroletype(@Valid @RequestBody UserDto requestDTO) {

		String response = commonmasterservice.APIcallroletype(requestDTO);

		return response;
	}

	@PostMapping("/getdownloadlink")
	/// public HashMap<String, String> DownloadDocument(@Valid @RequestBody
	/// DownloadDTO requestDTO) {
	public String Getdownloadlink(@Valid @RequestBody DownloadDTO requestDTO) {
		String response = commonmasterservice.APIcalldownloaddoucment(requestDTO);

		return response;
	}

	@PostMapping("/entity")
	public String entitydetails(@Valid @RequestBody UMsearchDTO requestDTO) {

		String response = commonmasterservice.EntityDetails(requestDTO);

		return response;
	}

	@PostMapping("/getentityname")
	public String GetentityName(@Valid @RequestBody EntityDTO requestDTO) {

		String response = commonmasterservice.APIcallentityType(requestDTO);

		return response;
	}

	@PostMapping("/getunitname")
	public String GetunitName(@Valid @RequestBody EntityDTO requestDTO) {

		String response = commonmasterservice.APIcallunitname(requestDTO);

		return response;
	}

	@PostMapping("/getapplicationnumber")
	public String CommonMasterGetUserName(@Valid @RequestBody CommaonMasterDistrictDto requestDTO) {
		String responce = commonmasterservice.applicationNumberAPI(requestDTO);
		return responce;

	}

	@PostMapping("/getunitcode")
	public String userUnitCodeAPI(@Valid @RequestBody CommanMasterUnitCodeDTO requestDTO) {
		String responce = commonmasterservice.userUnitCodeAPI(requestDTO);
		return responce;
	}

}
