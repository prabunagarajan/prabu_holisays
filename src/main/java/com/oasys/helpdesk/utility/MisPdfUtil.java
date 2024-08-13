package com.oasys.helpdesk.utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MisPdfUtil {

	/**
	 * @param loginBean2
	 * @param htmlInput
	 * @param pdfOutputFile
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void createPDF(String htmlInputContent, HttpServletResponse httpResponse) throws Exception {
		log.info("===========START MisPdfUtil.createPDF=============");

		String url = "C:\\Users\\91984\\Downloads\\mf4_print\\images\\";
		String barcode = url + "barcode.png";
		String logo = url + "up_excise_logo.png";

		DateFormat preparedTimeFormat = new SimpleDateFormat("h:mm a");
		preparedTimeFormat.format(new Date());
		htmlInputContent = htmlInputContent.replace("$barcode", barcode);
		htmlInputContent = htmlInputContent.replace("$logo", logo);
		if (htmlInputContent == null || htmlInputContent.isEmpty()) {
			throw new Exception("HTML InputContent is empty");
		}

		// step 1
		//Document document = new Document();
		Document document = new Document(PageSize.A4_LANDSCAPE.rotate(),10f,10f,10f,10f);
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, httpResponse.getOutputStream());

		writer.setInitialLeading(2);
		// step 3
		document.open();

		// step 4
		InputStream is = new ByteArrayInputStream(htmlInputContent.toString().getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);

		// XMLWorkerHelper.getInstance().parseXHtml(writer, document, htmlInputStream);

		// step 5
		document.close();
		log.info("===========END MisPdfUtil.createPDF=============");
	}


	
	
	public boolean isDecimal(Object value) {
		boolean isDecimal = true;
		try {
			Double.parseDouble(String.valueOf(value));
		} catch (Exception ex) {
			isDecimal = false;
		}
		return isDecimal;
	}

	public String getFormattedValue(Object obj) {
		String value = null;
		try {
			value = String.format("%.2f", (double) obj);
		} catch (Exception ex) {
			value = obj != null ? String.valueOf(obj) : null;
		}
		return value;
	}

	public String getDecimalFormattedValue(Object obj) {
		String value = null;
		try {
			value = String.format("%.0f", (double) obj);
		} catch (Exception ex) {
			value = obj != null ? String.valueOf(obj) : null;
		}
		return value;
	}
}
