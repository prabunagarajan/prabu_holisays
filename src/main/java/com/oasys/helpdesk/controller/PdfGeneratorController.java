package com.oasys.helpdesk.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oasys.helpdesk.service.PdfGeneratorService;

import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@RequestMapping("/pdfgen")
public class PdfGeneratorController {
	
	@Autowired
	private PdfGeneratorService pdfGeneratorService;
	
	@GetMapping(value = "/tpfile/{tpNumber}")
	public ResponseEntity<InputStreamResource> getTransportPassPdfFileByTpType(@PathVariable String tpNumber,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("Inside getTransportPassPdfFileByTpType() - START");
		try {

			String filePath = pdfGeneratorService.getTransportPassFilePath(tpNumber);

			if (StringUtils.isBlank(filePath)) {
				throw new Exception("File not found");
			}

			return downloadPdfFile(filePath);

		} catch (Exception ex) {
			log.error("Error at getTransportPassPdfFileByTpType()", ex);
		} 

		return null;
	}

	
	private ResponseEntity<InputStreamResource> downloadPdfFile(String filePath) throws Exception {
		log.info("Inside downloadPdfFile() - START");

		File file = new File(filePath);

		HttpHeaders headers = new HttpHeaders();
		// headers.add("content-disposition", "inline;filename=" + file.getName());
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + file.getName());

		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/pdf")).body(resource);

	}
	
	@GetMapping(value = "/ealtpfile/{tpNumber}")
	public ResponseEntity<InputStreamResource> getTransportPassPdfFileByTpTypeEal(@PathVariable String tpNumber,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("Inside getTransportPassPdfFileByTpType() - START");
		try {

			String filePath = pdfGeneratorService.getTransportPassFilePathEAL(tpNumber);

			if (StringUtils.isBlank(filePath)) {
				throw new Exception("File not found");
			}

			return downloadPdfFile(filePath);

		} catch (Exception ex) {
			log.error("Error at getTransportPassPdfFileByTpType()", ex);
		} 

		return null;
	}
	
//	@GetMapping(value = "/ealSlip")
//	public ResponseEntity<InputStreamResource> getEalSlip(@RequestParam String applnNo, HttpServletRequest request,
//			HttpServletResponse response) throws IOException {
//		log.info("Inside getEalSlip() - START");
//		try {
//
//			String filePath = pdfGeneratorService.getSlipFilePathEAL(applnNo);
//
//			if (StringUtils.isBlank(filePath)) {
//				throw new Exception("File not found");
//			}
//
//			return downloadPdfFile(filePath);
//
//		} catch (Exception ex) {
//			log.error("Error at getEalSlip()", ex);
//			// Handle the exception appropriately, maybe return a ResponseEntity with an
//			// error message
//		}
//
//		// Return an appropriate ResponseEntity if the method fails for some reason
//		return ResponseEntity.notFound().build(); // Or any other appropriate response
//	}

	@GetMapping(value = "/ealSlip")
	public ResponseEntity<InputStreamResource> getEalSlip(@RequestParam String applnNo, HttpServletRequest request,
	        HttpServletResponse response) {
	    log.info("Inside getEalSlip() - START");

	    try {
	        String filePath = pdfGeneratorService.getSlipFilePathEAL(applnNo);

	        if (StringUtils.isBlank(filePath)) {
	            log.error("File not found for application number: {}", applnNo);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new InputStreamResource(new ByteArrayInputStream("File not found".getBytes())));
	        }

	        return downloadPdfFile(filePath);

	    } catch (Exception ex) {
	        log.error("Error at getEalSlip()", ex);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new InputStreamResource(new ByteArrayInputStream("Internal server error".getBytes())));
	    }
	}

}
