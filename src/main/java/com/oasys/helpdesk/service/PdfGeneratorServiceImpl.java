package com.oasys.helpdesk.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.oasys.helpdesk.util.AppUtil;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class PdfGeneratorServiceImpl implements PdfGeneratorService {
	
	@Autowired
	private QrCodeService qrCodeService;
	
	@Autowired
	private HtmlToPdfService htmlToPdfService;
	
	@Autowired
	private JdbcTemplate helpdeskJdbcTemplate;
	
	@Value("${helpdesk.schema}")
	private String helpdeskSchema;
	

	@Override
	public String getTransportPassFilePath(String tpNumber) throws IOException {

		log.info("Inside getTransportPassFilePath() - START");

		String logoFileEncodedString = null;

		String qrCodeBase64Content = null;// indentQrBase64Content

		/*
		 * NEED TO CHANGE LOGO PATH
		 */

		byte[] logoFileContent = AppUtil.getFileContentBytes("/images/logo.png", true);

		int logoFileContentLength = logoFileContent != null ? logoFileContent.length : 0;

		if (logoFileContentLength > 0) {
			logoFileEncodedString = Base64.getEncoder().encodeToString(logoFileContent);
		}
		
		String transportPassTemplateContent = null;
		
		transportPassTemplateContent = AppUtil.getFileContentFromClassPath("/templates/TpTemplateV1.html");
		
//		String indentReqPdfDir = JdbcUtil.getAppConfigValue(appConfigService, scmRetailJdbcTemplate,
//				"SCM_INDENT_REQUEST_PDF_FILE_DIR");
		
		String tpNumberPdfDir = null;
		tpNumberPdfDir = tpNumberPdfDir == null ? "" : tpNumberPdfDir.trim();

		if (StringUtils.isEmpty(tpNumberPdfDir)) {
			tpNumberPdfDir = "/fileserver/UPEXCISE/FILES/DOCUMENTS/INDENT_REQUEST";
		}

		File dir = new File(tpNumberPdfDir);
		if (dir.isDirectory() == false) {
			dir.mkdirs();
		}
		
		String qrFilePath = tpNumberPdfDir + "/" + tpNumber + ".png";
		
		qrCodeService.createSingleQRImage(tpNumber, 100, qrFilePath);

		byte[] qrImgFileContent = FileUtils.readFileToByteArray(new File(qrFilePath));
		qrCodeBase64Content = Base64.getEncoder().encodeToString(qrImgFileContent);

		qrCodeBase64Content = qrCodeBase64Content == null ? "" : qrCodeBase64Content;
		
		byte[] logoWatermarkFileContent = AppUtil
				.getFileContentBytesFromClassPath("/images/logo_1percent_opacity.png");
		String logoWatermarkEncodedString = Base64.getEncoder().encodeToString(logoWatermarkFileContent);
		
		String selectTpSql = "select erpb.application_date, dp.carton_size, dp.tp_applnno,erpb.requested_appln_no, case when erpb.status = 1 then 'Approved' WHEN erpb.status = 6 THEN 'Dispatched' else erpb.status end as status, erpb.license_type, erpb.entity_name, erpb.entity_type, erpb.entity_address,\r\n"
				+ "erpb.pu_license_type, erpb.pu_entity_name, erpb.pu_entity_address, dp.vehicle_agency_name, dp.vehicle_number, dp.vehicle_agency_address, dp.driver_name,\r\n"
				+ "dp.route_type, dp.route_details, dp.major_route, dp.distance_kms, dp.valid_upto, dp.gross_weight_Qtls, dp.tare_weight_Qtls, dp.net_weight_Qtls,\r\n"
				+ "dp.GPS_device_id, dp.Remarks, dp.code_type, dp.no_of_barcode , dp.no_of_qrcode, dp.no_of_roll, dp.no_barcode_received, dp.no_qrcode_received , dp.no_roll_received \r\n"
				+ "from "+helpdeskSchema+".eal_request_pu_bwfl erpb "
				+ "join "+helpdeskSchema+".dispatch_tp_quantity_pu_bwfl dp on erpb.id=dp.eal_requestid \r\n"
				+ "where dp.tp_applnno =${tpNumber};";
		
		selectTpSql = selectTpSql.replace("${tpNumber}",AppUtil.quoteValue(tpNumber));
		
		Map<String, Object> tpMap = getMap(helpdeskJdbcTemplate, selectTpSql);
		
		String application_date = getString(tpMap, "application_date");
		String carton_size = getString( tpMap, "carton_size");
		String tp_applnno = getString(tpMap, "tp_applnno");
		String requested_appln_no = getString(tpMap,"requested_appln_no");
		String status = getString(tpMap, "status");
		
		String license_type = getString(tpMap, "license_type");
		String entity_name = getString( tpMap, "entity_name");
		String entity_type = getString(tpMap, "entity_type");
		String entity_address = getString(tpMap, "entity_address");
		String pu_license_type = getString( tpMap, "pu_license_type");
		String pu_entity_name = getString(tpMap, "pu_entity_name");
		String pu_entity_address = getString(tpMap, "pu_entity_address");
		
		String vehicle_agency_name = getString(tpMap, "vehicle_agency_name");
		String vehicle_number = getString( tpMap, "vehicle_number");
		String driver_name = getString(tpMap, "driver_name");
		String route_type = getString( tpMap, "route_type");
		String route_details = getString(tpMap, "route_details");
		String major_route = getString(tpMap, "major_route");
		String distance_kms = getString(tpMap, "distance_kms");
		String valid_upto = getString( tpMap, "valid_upto");
		
		String gross_weight_Qtls = getString(tpMap, "gross_weight_Qtls");
		String tare_weight_Qtls = getString(tpMap, "tare_weight_Qtls");
		String net_weight_Qtls = getString( tpMap, "net_weight_Qtls");
		String GPS_device_id = getString(tpMap, "GPS_device_id");
		String Remarks = getString(tpMap, "Remarks");
		String code_type = getString(tpMap, "code_type");
		
		String no_of_barcode = getString(tpMap, "no_of_barcode");
		String no_of_qrcode = getString(tpMap, "no_of_qrcode");
		String no_of_roll = getString(tpMap, "no_of_roll");
		String no_barcode_received = getString( tpMap, "no_barcode_received");
		String no_qrcode_received = getString( tpMap, "no_qrcode_received");
		String no_roll_received = getString(tpMap, "no_roll_received");

		
		
		transportPassTemplateContent = transportPassTemplateContent.replace("${upExciseLogoBase64Content}", logoFileEncodedString);
		transportPassTemplateContent = transportPassTemplateContent.replace("${qrCodeBase64Content}", qrCodeBase64Content);
		transportPassTemplateContent = transportPassTemplateContent.replace("${logoWatermarkEncodedString}", logoWatermarkEncodedString);
		
		transportPassTemplateContent = transportPassTemplateContent.replace("${application_date}", application_date);
		transportPassTemplateContent = transportPassTemplateContent.replace("${tp_applnno}", tp_applnno);
		transportPassTemplateContent = transportPassTemplateContent.replace("${requested_appln_no}", requested_appln_no);
		transportPassTemplateContent = transportPassTemplateContent.replace("${status}", status);
		transportPassTemplateContent = transportPassTemplateContent.replace("${license_type}", license_type);
		transportPassTemplateContent = transportPassTemplateContent.replace("${entity_name}", entity_name);
		transportPassTemplateContent = transportPassTemplateContent.replace("${entity_type}", entity_type);
		transportPassTemplateContent = transportPassTemplateContent.replace("${entity_address}", entity_address);
		transportPassTemplateContent = transportPassTemplateContent.replace("${pu_license_type}", pu_license_type);
		transportPassTemplateContent = transportPassTemplateContent.replace("${pu_entity_name}", pu_entity_name);
		transportPassTemplateContent = transportPassTemplateContent.replace("${pu_entity_address}", pu_entity_address);
		transportPassTemplateContent = transportPassTemplateContent.replace("${vehicle_agency_name}", vehicle_agency_name);
		
		transportPassTemplateContent = transportPassTemplateContent.replace("${vehicle_number}", vehicle_number);
		transportPassTemplateContent = transportPassTemplateContent.replace("${driver_name}", driver_name);
		transportPassTemplateContent = transportPassTemplateContent.replace("${route_type}", route_type);
		transportPassTemplateContent = transportPassTemplateContent.replace("${route_details}", route_details);
		transportPassTemplateContent = transportPassTemplateContent.replace("${major_route}", major_route);
		transportPassTemplateContent = transportPassTemplateContent.replace("${distance_kms}", distance_kms);
		transportPassTemplateContent = transportPassTemplateContent.replace("${valid_upto}", valid_upto);
		transportPassTemplateContent = transportPassTemplateContent.replace("${gross_weight_Qtls}", gross_weight_Qtls);
		transportPassTemplateContent = transportPassTemplateContent.replace("${tare_weight_Qtls}", tare_weight_Qtls);
		transportPassTemplateContent = transportPassTemplateContent.replace("${net_weight_Qtls}", net_weight_Qtls);
		transportPassTemplateContent = transportPassTemplateContent.replace("${GPS_device_id}", GPS_device_id);
		transportPassTemplateContent = transportPassTemplateContent.replace("${Remarks}", Remarks);
		transportPassTemplateContent = transportPassTemplateContent.replace("${code_type}", code_type);
		
//		transportPassTemplateContent = transportPassTemplateContent.replace("${carton_size}", carton_size);
//		transportPassTemplateContent = transportPassTemplateContent.replace("${no_of_barcode}", no_of_barcode);
//		transportPassTemplateContent = transportPassTemplateContent.replace("${no_of_qrcode}", no_of_qrcode);
//		transportPassTemplateContent = transportPassTemplateContent.replace("${no_of_roll}", no_of_roll);
//		transportPassTemplateContent = transportPassTemplateContent.replace("${no_barcode_received}", no_barcode_received);
//		transportPassTemplateContent = transportPassTemplateContent.replace("${no_qrcode_received}", no_qrcode_received);
//		transportPassTemplateContent = transportPassTemplateContent.replace("${no_roll_received}", no_roll_received);
		
		// PDF Current date set in Footer
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		String currentDate = format.format(date);

		transportPassTemplateContent = transportPassTemplateContent.replace("${currentDate}", AppUtil.quoteValue(currentDate));
		
		
		String selectCartonDetialsSql = "select erpb.application_date, dp.carton_size, dp.tp_applnno,erpb.requested_appln_no, case when erpb.status = 1 then 'Approved' WHEN erpb.status = 6 THEN 'Dispatched' else erpb.status end as status, erpb.license_type, erpb.entity_name, erpb.entity_type, erpb.entity_address,\r\n"
				+ "erpb.pu_license_type, erpb.pu_entity_name, erpb.pu_entity_address, dp.vehicle_agency_name, dp.vehicle_number, dp.vehicle_agency_address, dp.driver_name,\r\n"
				+ "dp.route_type, dp.route_details, dp.major_route, dp.distance_kms, dp.valid_upto, dp.gross_weight_Qtls, dp.tare_weight_Qtls, dp.net_weight_Qtls,\r\n"
				+ "dp.GPS_device_id, dp.Remarks, dp.code_type, dp.no_of_barcode , dp.no_of_qrcode, dp.no_of_roll, dp.no_barcode_received, dp.no_qrcode_received , dp.no_roll_received \r\n"
				+ "from "+helpdeskSchema+".eal_request_pu_bwfl erpb "
				+ "join "+helpdeskSchema+".dispatch_tp_quantity_pu_bwfl dp on erpb.id=dp.eal_requestid \r\n"
				+ "where dp.tp_applnno =${tpNumber};";
		
		selectCartonDetialsSql = selectCartonDetialsSql.replace("${tpNumber}",AppUtil.quoteValue(tpNumber));
		
		
		List<Map<String, Object>> itemMapList = helpdeskJdbcTemplate.queryForList(selectCartonDetialsSql);
		
		String lineItemTemplateContent = AppUtil.getFileContentFromClassPath("/templates/LineItemTemplateV1.html");
		
		StringBuilder indentItemSB = new StringBuilder();
		
		for (Map<String, Object> itemMap : itemMapList) {
			
			String lineItem = lineItemTemplateContent;
			lineItem = lineItem.replace("${brandName}", getString(itemMap, "brand_name", "0"));
			lineItem = lineItem.replace("${carton_size}", getString(itemMap, "carton_size", "0"));
			lineItem = lineItem.replace("${no_of_barcode}", getString(itemMap, "no_of_barcode", "0"));
			lineItem = lineItem.replace("${no_of_qrcode}", getString(itemMap, "no_of_qrcode", "0"));
			lineItem = lineItem.replace("${no_of_roll}", getString(itemMap, "no_of_roll", "0"));
			lineItem = lineItem.replace("${no_barcode_received}", getString(itemMap, "no_barcode_received", "0"));
			lineItem = lineItem.replace("${no_qrcode_received}", getString(itemMap, "no_qrcode_received", "0"));
			lineItem = lineItem.replace("${no_roll_received}", getString(itemMap, "no_roll_received", "0"));
			
			indentItemSB.append(lineItem);
		}
		
		transportPassTemplateContent = transportPassTemplateContent.replace("${lineItems}", indentItemSB.toString());
		
//		indentContent = loadTransportPassContent(indentContent, tpNumber);

		tpNumber = tpNumber == null ? "" : tpNumber.trim();
		tpNumber = tpNumber.replace(" ", "");

		String outputPdfFilePath = new StringBuilder().append(tpNumberPdfDir).append("/Transportpass_")
				.append(tpNumber).append(".pdf").toString();
		
		new HtmlToPdfService().htmlToPdf(transportPassTemplateContent, outputPdfFilePath);
		
//		 String sourcePdfFilePath = qrFilePath + "/" + tpNumber + "_source.pdf";
//
//		String destinationPdfFilePath = qrFilePath + "/" + tpNumber + ".pdf";
		
//		htmlToPdfService.addPageNumbers(sourcePdfFilePath, destinationPdfFilePath, 0);
		
		return outputPdfFilePath;
	}
	
	public static String getString(Map<String, Object> map, String key) {
		// logInfo("getString() - Key: " + key);
		try {
			Object obj = map.get(key);
			if (obj != null) {
				return String.valueOf(obj);
			}
		} catch (Exception ex) {
			log.error("Exception at getString()", ex);
		}
		return null;
	}
	
	public static Map<String, Object> getMap(JdbcTemplate jdbcTemplate, String qry) {
		return getMap(jdbcTemplate, qry, null);
	}
	
	public static Map<String, Object> getMap(JdbcTemplate jdbcTemplate, String qry, Object[] values) {
		Map<String, Object> rtnMap = null;
		List<Map<String, Object>> mapList = null;
		try {
			if (values != null) {
				mapList = jdbcTemplate.queryForList(qry, values);
			} else {
				mapList = jdbcTemplate.queryForList(qry);
			}
			if (mapList != null && !mapList.isEmpty()) {
				rtnMap = mapList.get(0);
			}
			//
		} catch (org.springframework.jdbc.BadSqlGrammarException ex) {
			log.error("# ################ START ############### #");
			log.error("BadSqlGrammarException-Qry: " + qry);
			log.error("# ####################################### #");
		} catch (Exception ex) {
			log.error("Exception at getMap()", ex);
			log.error("# ################ START ############### #");
			log.error("Exception-Qry: " + qry);
			log.error("# ####################################### #");
		}
		return rtnMap;
	}
	
	
	public static String getString(Map<String, Object> map, String key, String defaultValue) {
		// logInfo("getString() - Key: " + key);
		try {
			Object obj = map.get(key);
			if (obj != null) {
				return String.valueOf(obj);
			}
		} catch (Exception ex) {
			log.error("Exception at getString()", ex);
		}
		return defaultValue;
	}

//	private String loadTransportPassContent(String indentContent, String tpNumber) {
//		
//		String templateHtml = null;
//		
//		templateHtml = AppUtil.getFileContentFromClassPath("/templates/TransportpassTemplateV1.html");
//		
//		
//		return indentContent;
//	}
	

	
	@Override
	public String getTransportPassFilePathEAL(String tpNumber) throws IOException {

		log.info("Inside getTransportPassFilePath() - START");

		String logoFileEncodedString = null;

		String qrCodeBase64Content = null;// indentQrBase64Content

		/*
		 * NEED TO CHANGE LOGO PATH
		 */

		byte[] logoFileContent = AppUtil.getFileContentBytes("/images/logo.png", true);

		int logoFileContentLength = logoFileContent != null ? logoFileContent.length : 0;

		if (logoFileContentLength > 0) {
			logoFileEncodedString = Base64.getEncoder().encodeToString(logoFileContent);
		}
		
		String transportPassTemplateContent = null;
		
		transportPassTemplateContent = AppUtil.getFileContentFromClassPath("/templates/EalTpTemplateV2.html");
		
//		String indentReqPdfDir = JdbcUtil.getAppConfigValue(appConfigService, scmRetailJdbcTemplate,
//				"SCM_INDENT_REQUEST_PDF_FILE_DIR");
		
		String tpNumberPdfDir = null;
		tpNumberPdfDir = tpNumberPdfDir == null ? "" : tpNumberPdfDir.trim();

		if (StringUtils.isEmpty(tpNumberPdfDir)) {
			tpNumberPdfDir = "/fileserver/UPEXCISE/FILES/DOCUMENTS/INDENT_REQUEST";
		}

		File dir = new File(tpNumberPdfDir);
		if (dir.isDirectory() == false) {
			dir.mkdirs();
		}
		
		String qrFilePath = tpNumberPdfDir + "/" + tpNumber + ".png";
		
		qrCodeService.createSingleQRImage(tpNumber, 100, qrFilePath);

		byte[] qrImgFileContent = FileUtils.readFileToByteArray(new File(qrFilePath));
		qrCodeBase64Content = Base64.getEncoder().encodeToString(qrImgFileContent);

		qrCodeBase64Content = qrCodeBase64Content == null ? "" : qrCodeBase64Content;
		
		byte[] logoWatermarkFileContent = AppUtil
				.getFileContentBytesFromClassPath("/images/logo_1percent_opacity.png");
		String logoWatermarkEncodedString = Base64.getEncoder().encodeToString(logoWatermarkFileContent);
		
		String selectTpSql = "select erpb.application_date, dp.carton_size, dp.tp_applnno,erpb.requested_appln_no, case when erpb.status = 1 then 'Approved' WHEN erpb.status = 6 THEN 'Dispatched' else erpb.status end as status, erpb.license_type, erpb.entity_name, erpb.entity_type, erpb.entity_address,\r\n"
				+ "dp.vendor_name,dp.vendor_address, dp.vehicle_agency_name, dp.vehicle_number, dp.vehicle_agency_address, dp.driver_name,\r\n"
				+ "dp.route_type, dp.route_details, dp.major_route, dp.distance_kms, dp.valid_upto, dp.gross_weight_Qtls, dp.tare_weight_Qtls, dp.net_weight_Qtls,\r\n"
				+ "dp.GPS_device_id, dp.Remarks, dp.code_type, dp.no_of_barcode , dp.no_of_qrcode, dp.no_of_roll, dp.no_barcode_received, dp.no_qrcode_received , dp.no_roll_received \r\n"
				+ "from "+helpdeskSchema+".eal_request erpb "
				+ "join "+helpdeskSchema+".dispatch_tp_quantity dp on erpb.id=dp.eal_requestid \r\n"
				+ "where dp.tp_applnno =${tpNumber};";
		
		selectTpSql = selectTpSql.replace("${tpNumber}",AppUtil.quoteValue(tpNumber));
		
		Map<String, Object> tpMap = getMap(helpdeskJdbcTemplate, selectTpSql);
		
		String application_date = getString(tpMap, "application_date");
		String carton_size = getString( tpMap, "carton_size");
		String tp_applnno = getString(tpMap, "tp_applnno");
		String requested_appln_no = getString(tpMap,"requested_appln_no");
		String status = getString(tpMap, "status");
		
		String license_type = getString(tpMap, "license_type");
		String entity_name = getString( tpMap, "entity_name");
		String entity_type = getString(tpMap, "entity_type");
		String entity_address = getString(tpMap, "entity_address");
		String vendor_name = getString(tpMap, "vendor_name");
		String vendor_address = getString(tpMap, "vendor_address");
		
		String vehicle_agency_name = getString(tpMap, "vehicle_agency_name");
		String vehicle_number = getString( tpMap, "vehicle_number");
		String driver_name = getString(tpMap, "driver_name");
		String route_type = getString( tpMap, "route_type");
		String route_details = getString(tpMap, "route_details");
		String major_route = getString(tpMap, "major_route");
		String distance_kms = getString(tpMap, "distance_kms");
		String valid_upto = getString( tpMap, "valid_upto");
		
		String gross_weight_Qtls = getString(tpMap, "gross_weight_Qtls");
		String tare_weight_Qtls = getString(tpMap, "tare_weight_Qtls");
		String net_weight_Qtls = getString( tpMap, "net_weight_Qtls");
		String GPS_device_id = getString(tpMap, "GPS_device_id");
		String Remarks = getString(tpMap, "Remarks");
		String code_type = getString(tpMap, "code_type");
		
		String no_of_barcode = getString(tpMap, "no_of_barcode");
		String no_of_qrcode = getString(tpMap, "no_of_qrcode");
		String no_of_roll = getString(tpMap, "no_of_roll");
		String no_barcode_received = getString( tpMap, "no_barcode_received");
		String no_qrcode_received = getString( tpMap, "no_qrcode_received");
		String no_roll_received = getString(tpMap, "no_roll_received");

		
		
		transportPassTemplateContent = transportPassTemplateContent.replace("${upExciseLogoBase64Content}", logoFileEncodedString);
		transportPassTemplateContent = transportPassTemplateContent.replace("${qrCodeBase64Content}", qrCodeBase64Content);
		transportPassTemplateContent = transportPassTemplateContent.replace("${logoWatermarkEncodedString}", logoWatermarkEncodedString);
		
		transportPassTemplateContent = transportPassTemplateContent.replace("${application_date}", application_date);
		transportPassTemplateContent = transportPassTemplateContent.replace("${tp_applnno}", tp_applnno);
		transportPassTemplateContent = transportPassTemplateContent.replace("${requested_appln_no}", requested_appln_no);
		transportPassTemplateContent = transportPassTemplateContent.replace("${status}", status);
		transportPassTemplateContent = transportPassTemplateContent.replace("${license_type}", license_type);
		transportPassTemplateContent = transportPassTemplateContent.replace("${entity_name}", entity_name);
		transportPassTemplateContent = transportPassTemplateContent.replace("${entity_type}", entity_type);
		transportPassTemplateContent = transportPassTemplateContent.replace("${entity_address}", entity_address);
		transportPassTemplateContent = transportPassTemplateContent.replace("${vendor_name}", vendor_name);
		transportPassTemplateContent = transportPassTemplateContent.replace("${vendor_address}", vendor_address);
		transportPassTemplateContent = transportPassTemplateContent.replace("${vehicle_agency_name}", vehicle_agency_name);
		
		transportPassTemplateContent = transportPassTemplateContent.replace("${vehicle_number}", vehicle_number);
		transportPassTemplateContent = transportPassTemplateContent.replace("${driver_name}", driver_name);
		transportPassTemplateContent = transportPassTemplateContent.replace("${route_type}", route_type);
		transportPassTemplateContent = transportPassTemplateContent.replace("${route_details}", route_details);
		transportPassTemplateContent = transportPassTemplateContent.replace("${major_route}", major_route);
		transportPassTemplateContent = transportPassTemplateContent.replace("${distance_kms}", distance_kms);
		transportPassTemplateContent = transportPassTemplateContent.replace("${valid_upto}", valid_upto);
		transportPassTemplateContent = transportPassTemplateContent.replace("${gross_weight_Qtls}", gross_weight_Qtls);
		transportPassTemplateContent = transportPassTemplateContent.replace("${tare_weight_Qtls}", tare_weight_Qtls);
		transportPassTemplateContent = transportPassTemplateContent.replace("${net_weight_Qtls}", net_weight_Qtls);
		transportPassTemplateContent = transportPassTemplateContent.replace("${GPS_device_id}", GPS_device_id);
		transportPassTemplateContent = transportPassTemplateContent.replace("${Remarks}", Remarks);
		transportPassTemplateContent = transportPassTemplateContent.replace("${code_type}", code_type);
		
//		transportPassTemplateContent = transportPassTemplateContent.replace("${carton_size}", carton_size);
//		transportPassTemplateContent = transportPassTemplateContent.replace("${no_of_barcode}", no_of_barcode);
//		transportPassTemplateContent = transportPassTemplateContent.replace("${no_of_qrcode}", no_of_qrcode);
//		transportPassTemplateContent = transportPassTemplateContent.replace("${no_of_roll}", no_of_roll);
//		transportPassTemplateContent = transportPassTemplateContent.replace("${no_barcode_received}", no_barcode_received);
//		transportPassTemplateContent = transportPassTemplateContent.replace("${no_qrcode_received}", no_qrcode_received);
//		transportPassTemplateContent = transportPassTemplateContent.replace("${no_roll_received}", no_roll_received);
		
		// PDF Current date set in Footer
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		String currentDate = format.format(date);

		transportPassTemplateContent = transportPassTemplateContent.replace("${currentDate}", AppUtil.quoteValue(currentDate));
		
		
		String selectCartonDetialsSql = "select erpb.application_date, dp.carton_size, dp.tp_applnno,erpb.requested_appln_no, case when erpb.status = 1 then 'Approved' WHEN erpb.status = 6 THEN 'Dispatched' else erpb.status end as status, erpb.license_type, erpb.entity_name, erpb.entity_type, erpb.entity_address,\r\n"
				+ "dp.vendor_name,dp.vendor_address, dp.vehicle_agency_name, dp.vehicle_number, dp.vehicle_agency_address, dp.driver_name,\r\n"
				+ "dp.route_type, dp.route_details, dp.major_route, dp.distance_kms, dp.valid_upto, dp.gross_weight_Qtls, dp.tare_weight_Qtls, dp.net_weight_Qtls,\r\n"
				+ "dp.GPS_device_id, dp.Remarks, dp.code_type, dp.no_of_barcode , dp.no_of_qrcode, dp.no_of_roll, dp.no_barcode_received, dp.no_qrcode_received , dp.no_roll_received \r\n"
				+ "from "+helpdeskSchema+".eal_request erpb "
				+ "join "+helpdeskSchema+".dispatch_tp_quantity dp on erpb.id=dp.eal_requestid \r\n"
				+ "where dp.tp_applnno =${tpNumber};";
		
		selectCartonDetialsSql = selectCartonDetialsSql.replace("${tpNumber}",AppUtil.quoteValue(tpNumber));
		
		
		List<Map<String, Object>> itemMapList = helpdeskJdbcTemplate.queryForList(selectCartonDetialsSql);
		
		String lineItemTemplateContent = AppUtil.getFileContentFromClassPath("/templates/LineItemTemplateV1.html");
		
		StringBuilder indentItemSB = new StringBuilder();
		
		for (Map<String, Object> itemMap : itemMapList) {
			
			String lineItem = lineItemTemplateContent;
			lineItem = lineItem.replace("${brandName}", getString(itemMap, "brand_name", "0"));
			lineItem = lineItem.replace("${carton_size}", getString(itemMap, "carton_size", "0"));
			lineItem = lineItem.replace("${no_of_barcode}", getString(itemMap, "no_of_barcode", "0"));
			lineItem = lineItem.replace("${no_of_qrcode}", getString(itemMap, "no_of_qrcode", "0"));
			lineItem = lineItem.replace("${no_of_roll}", getString(itemMap, "no_of_roll", "0"));
			lineItem = lineItem.replace("${no_barcode_received}", getString(itemMap, "no_barcode_received", "0"));
			lineItem = lineItem.replace("${no_qrcode_received}", getString(itemMap, "no_qrcode_received", "0"));
			lineItem = lineItem.replace("${no_roll_received}", getString(itemMap, "no_roll_received", "0"));
			
			indentItemSB.append(lineItem);
		}
		
		transportPassTemplateContent = transportPassTemplateContent.replace("${lineItems}", indentItemSB.toString());
		
//		indentContent = loadTransportPassContent(indentContent, tpNumber);

		tpNumber = tpNumber == null ? "" : tpNumber.trim();
		tpNumber = tpNumber.replace(" ", "");

		String outputPdfFilePath = new StringBuilder().append(tpNumberPdfDir).append("/EALTransportpass_")
				.append(tpNumber).append(".pdf").toString();
		
		new HtmlToPdfService().htmlToPdf(transportPassTemplateContent, outputPdfFilePath);
		
//		 String sourcePdfFilePath = qrFilePath + "/" + tpNumber + "_source.pdf";
//
//		String destinationPdfFilePath = qrFilePath + "/" + tpNumber + ".pdf";
		
//		htmlToPdfService.addPageNumbers(sourcePdfFilePath, destinationPdfFilePath, 0);
		
		return outputPdfFilePath;
	}
	

	@Override
	public String getSlipFilePathEAL(String applnNo) throws IOException {

		log.info("Inside getSlipFilePath() - START");

		String logoFileEncodedString = null;

		String qrCodeBase64Content = null;// indentQrBase64Content

		/*
		 * NEED TO CHANGE LOGO PATH
		 */

		byte[] logoFileContent = AppUtil.getFileContentBytes("/images/logo.png", true);

		int logoFileContentLength = logoFileContent != null ? logoFileContent.length : 0;

		if (logoFileContentLength > 0) {
			logoFileEncodedString = Base64.getEncoder().encodeToString(logoFileContent);
		}
		
		String transportPassTemplateContent = null;
		
		transportPassTemplateContent = AppUtil.getFileContentFromClassPath("/templates/EALIssueSlip.html");
		
//		String indentReqPdfDir = JdbcUtil.getAppConfigValue(appConfigService, scmRetailJdbcTemplate,
//				"SCM_INDENT_REQUEST_PDF_FILE_DIR");
		
		String tpNumberPdfDir = null;
		tpNumberPdfDir = tpNumberPdfDir == null ? "" : tpNumberPdfDir.trim();

		if (StringUtils.isEmpty(tpNumberPdfDir)) {
			tpNumberPdfDir = "/fileserver/UPEXCISE/FILES/DOCUMENTS/INDENT_REQUEST";
		}

		File dir = new File(tpNumberPdfDir);
		if (dir.isDirectory() == false) {
			dir.mkdirs();
		}
		
		String generatedNumber = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

		
		String qrFilePath = tpNumberPdfDir + "/" + generatedNumber + ".png";
		
		qrCodeService.createSingleQRImage(generatedNumber, 100, qrFilePath);

		byte[] qrImgFileContent = FileUtils.readFileToByteArray(new File(qrFilePath));
		qrCodeBase64Content = Base64.getEncoder().encodeToString(qrImgFileContent);

		qrCodeBase64Content = qrCodeBase64Content == null ? "" : qrCodeBase64Content;
		
		byte[] logoWatermarkFileContent = AppUtil
				.getFileContentBytesFromClassPath("/images/logo_1percent_opacity.png");
		String logoWatermarkEncodedString = Base64.getEncoder().encodeToString(logoWatermarkFileContent);
		
		String selectTpSql = "SELECT COALESCE(era.application_date, '-') AS application_date,\r\n" + 
				"    CASE WHEN MONTH(era.application_date) >= 6 THEN CONCAT(YEAR(era.application_date), '-', YEAR(era.application_date) + 1)\r\n" + 
				"        ELSE CONCAT(YEAR(era.application_date) - 1, '-', YEAR(era.application_date))\r\n" + 
				"    END AS financial_year, COALESCE(era.entity_type, '-') AS entity_type, COALESCE(era.entity_name, '-') AS entity_name,\r\n" + 
				"    COALESCE(era.license_type, '-') AS license_type,COALESCE(era.license_no, '-') AS license_no,\r\n" + 
				"    COALESCE(era.entity_address, '-') AS entity_address,COALESCE(era.requested_appln_no, '-') AS requested_appln_no,\r\n" + 
				"    COALESCE(era.date_of_packaging, '-') AS date_of_packaging,COALESCE(era.liquor_type, '-') AS liquor_type,\r\n" + 
				"    COALESCE(era.liquor_sub_type, '-') AS liquor_sub_type,COALESCE(erma.packaging_size, '-') AS packaging_size,\r\n" + 
				"    COALESCE(era.brand_name, '-') AS brand_name,erma.code_type ,erma.carton_size ,\r\n" + 
				"    erma.no_of_barcode ,erma.no_of_qrcode ,erma.no_of_roll,\r\n" + 
				"    CASE era.status WHEN 0 THEN 'INPROGRESS' WHEN 1 THEN 'APPROVED' WHEN 2 THEN 'FORWARDED'\r\n" + 
				"        WHEN 3 THEN 'REQUESTFORCLARIFICATION' WHEN 4 THEN 'DRAFT' WHEN 5 THEN 'REJECT'\r\n" + 
				"        WHEN 6 THEN 'DISPATCHED' ELSE 'UNKNOWN' END AS status\r\n" + 
				"FROM "+helpdeskSchema+".eal_request_aec era\r\n" + 
				"JOIN  "+helpdeskSchema+".eal_request_map_aec erma \r\n" + 
				"ON era.id = erma.ealrequest_id \r\n" + 
				"WHERE era.requested_appln_no =${applnNo};";
		
		selectTpSql = selectTpSql.replace("${applnNo}",AppUtil.quoteValue(applnNo));
		
		Map<String, Object> tpMap = getMap(helpdeskJdbcTemplate, selectTpSql);
		
		String application_date = getString(tpMap, "application_date");
		String financial_year = getString( tpMap, "financial_year");
		String entity_type = getString(tpMap, "entity_type");
		String entity_name = getString( tpMap, "entity_name");
		String license_type = getString(tpMap, "license_type");
		String license_no = getString(tpMap, "license_no");
		String entity_address = getString(tpMap, "entity_address");
		String requested_appln_no = getString(tpMap,"requested_appln_no");
		String date_of_packaging = getString(tpMap,"date_of_packaging");
		String liquor_type = getString(tpMap,"liquor_type");
		String liquor_sub_type = getString(tpMap,"liquor_sub_type");
		String packaging_size = getString(tpMap,"packaging_size");
		String brand_name = getString(tpMap,"brand_name");
		

		
		
		transportPassTemplateContent = transportPassTemplateContent.replace("${upExciseLogoBase64Content}", logoFileEncodedString);
		transportPassTemplateContent = transportPassTemplateContent.replace("${qrCodeBase64Content}", qrCodeBase64Content);
		transportPassTemplateContent = transportPassTemplateContent.replace("${logoWatermarkEncodedString}", logoWatermarkEncodedString);
		
		transportPassTemplateContent = transportPassTemplateContent.replace("${generatedNumber}", generatedNumber);
		transportPassTemplateContent = transportPassTemplateContent.replace("${application_date}", application_date);
		transportPassTemplateContent = transportPassTemplateContent.replace("${financial_year}", financial_year);
		transportPassTemplateContent = transportPassTemplateContent.replace("${entity_type}", entity_type);
		transportPassTemplateContent = transportPassTemplateContent.replace("${entity_name}", entity_name);
		transportPassTemplateContent = transportPassTemplateContent.replace("${license_type}", license_type);
		transportPassTemplateContent = transportPassTemplateContent.replace("${license_no}", license_no);
		transportPassTemplateContent = transportPassTemplateContent.replace("${entity_address}", entity_address);
		transportPassTemplateContent = transportPassTemplateContent.replace("${requested_appln_no}", requested_appln_no);
		transportPassTemplateContent = transportPassTemplateContent.replace("${date_of_packaging}", date_of_packaging);
		transportPassTemplateContent = transportPassTemplateContent.replace("${liquor_type}", liquor_type);
		transportPassTemplateContent = transportPassTemplateContent.replace("${liquor_sub_type}", liquor_sub_type);
		transportPassTemplateContent = transportPassTemplateContent.replace("${packaging_size}", packaging_size);
		transportPassTemplateContent = transportPassTemplateContent.replace("${brand_name}", brand_name);
		
		
		// PDF Current date set in Footer
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		String currentDate = format.format(date);

		transportPassTemplateContent = transportPassTemplateContent.replace("${currentDate}", AppUtil.quoteValue(currentDate));
		
		
		String selectCartonDetialsSql = "SELECT COALESCE(era.application_date, '-') AS application_date,\r\n" + 
				"    CASE WHEN MONTH(era.application_date) >= 6 THEN CONCAT(YEAR(era.application_date), '-', YEAR(era.application_date) + 1)\r\n" + 
				"        ELSE CONCAT(YEAR(era.application_date) - 1, '-', YEAR(era.application_date))\r\n" + 
				"    END AS financial_year, COALESCE(era.entity_type, '-') AS entity_type, COALESCE(era.entity_name, '-') AS entity_name,\r\n" + 
				"    COALESCE(era.license_type, '-') AS license_type,COALESCE(era.license_no, '-') AS license_no,\r\n" + 
				"    COALESCE(era.entity_address, '-') AS entity_address,COALESCE(era.requested_appln_no, '-') AS requested_appln_no,\r\n" + 
				"    COALESCE(era.date_of_packaging, '-') AS date_of_packaging,COALESCE(era.liquor_type, '-') AS liquor_type,\r\n" + 
				"    COALESCE(era.liquor_sub_type, '-') AS liquor_sub_type,COALESCE(erma.packaging_size, '-') AS packaging_size,\r\n" + 
				"    COALESCE(era.brand_name, '-') AS brand_name,erma.code_type ,erma.carton_size ,\r\n" + 
				"    erma.no_of_barcode ,erma.no_of_qrcode ,erma.no_of_roll,\r\n" + 
				"    CASE era.status WHEN 0 THEN 'INPROGRESS' WHEN 1 THEN 'APPROVED' WHEN 2 THEN 'FORWARDED'\r\n" + 
				"        WHEN 3 THEN 'REQUESTFORCLARIFICATION' WHEN 4 THEN 'DRAFT' WHEN 5 THEN 'REJECT'\r\n" + 
				"        WHEN 6 THEN 'DISPATCHED' ELSE 'UNKNOWN' END AS status\r\n" + 
				"FROM "+helpdeskSchema+".eal_request_aec era\r\n" + 
				"JOIN  "+helpdeskSchema+".eal_request_map_aec erma \r\n" + 
				"ON era.id = erma.ealrequest_id \r\n" + 
				"WHERE era.requested_appln_no =${applnNo};";
		
		selectCartonDetialsSql = selectCartonDetialsSql.replace("${applnNo}",AppUtil.quoteValue(applnNo));
		
		
		List<Map<String, Object>> itemMapList = helpdeskJdbcTemplate.queryForList(selectCartonDetialsSql);
		
		String lineItemTemplateContent = AppUtil.getFileContentFromClassPath("/templates/LineItemEalSlip.html");
		
		StringBuilder indentItemSB = new StringBuilder();
		
		for (Map<String, Object> itemMap : itemMapList) {
			
			String lineItem = lineItemTemplateContent;
			lineItem = lineItem.replace("${code_type}", getString(itemMap, "code_type", "0"));
			lineItem = lineItem.replace("${carton_size}", getString(itemMap, "carton_size", "0"));
			lineItem = lineItem.replace("${no_of_barcode}", getString(itemMap, "no_of_barcode", "0"));
			lineItem = lineItem.replace("${no_of_qrcode}", getString(itemMap, "no_of_qrcode", "0"));
			lineItem = lineItem.replace("${no_of_roll}", getString(itemMap, "no_of_roll", "0"));
			lineItem = lineItem.replace("${status}", getString(itemMap, "status", "0"));
			
			indentItemSB.append(lineItem);
		}
		
		transportPassTemplateContent = transportPassTemplateContent.replace("${lineItems}", indentItemSB.toString());
		
//		indentContent = loadTransportPassContent(indentContent, tpNumber);

		applnNo = applnNo == null ? "" : applnNo.trim();
		applnNo = applnNo.replace(" ", "");

		String outputPdfFilePath = new StringBuilder().append(tpNumberPdfDir).append("/EALSlip_")
				.append(generatedNumber).append(".pdf").toString();
		
		new HtmlToPdfService().htmlToPdf(transportPassTemplateContent, outputPdfFilePath);
		
//		 String sourcePdfFilePath = qrFilePath + "/" + tpNumber + "_source.pdf";
//
//		String destinationPdfFilePath = qrFilePath + "/" + tpNumber + ".pdf";
		
//		htmlToPdfService.addPageNumbers(sourcePdfFilePath, destinationPdfFilePath, 0);
		
		return outputPdfFilePath;
	}
	

}
