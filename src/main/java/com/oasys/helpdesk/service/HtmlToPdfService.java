package com.oasys.helpdesk.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.openhtmltopdf.pdfboxout.PdfBoxRenderer;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class HtmlToPdfService {

	/**
	 * @param inputHTML
	 * @return
	 * @throws IOException
	 */
	private Document html5ParseDocument(String inputHTML) throws IOException {
		org.jsoup.nodes.Document doc;
		// System.out.println("parsing ...");
		// doc = Jsoup.parse(new File(inputHTML), "UTF-8");
		doc = Jsoup.parse(inputHTML);

		// System.out.println("parsing done ..." + doc);
		org.w3c.dom.Document document = new W3CDom().fromJsoup(doc);

		return document;
	}

	/**
	 * @param inputHTML
	 * @param outputPdf
	 * @throws IOException
	 */
	public void htmlToPdf(String inputHTML, String outputPdf) throws IOException {
		Document doc = html5ParseDocument(inputHTML);
		String baseUri = FileSystems.getDefault().getPath("font/").toUri().toString();
		log.info("baseUri: " + baseUri);
		OutputStream os = new FileOutputStream(outputPdf);
		PdfRendererBuilder builder = new PdfRendererBuilder();
		builder.useFastMode();
		builder.withUri(outputPdf);
		builder.toStream(os);
		String fontFilePath = HtmlToPdfService.class.getClassLoader()
				.getResource("font/gabriola/Gabriola.ttf").getFile();
		builder.useFont(new File(fontFilePath), "Gabriola");
		builder.withW3cDocument(doc, baseUri);
		// builder.useUriResolver(new MyResolver());
		builder.run();
		log.info("PDF generation completed");
		os.close();
	}

	public void htmlToPdfV3(String inputHTML, String outputPdf) throws Exception {
		log.info("Inside htmlToPdfV3() - START");
		// Document doc = html5ParseDocument(inputHTML);
		String baseUri = FileSystems.getDefault().getPath("/fileserver/").toUri().toString();
		log.info("baseUri: " + baseUri);
		OutputStream os = new FileOutputStream(outputPdf);
		PdfRendererBuilder builder = new PdfRendererBuilder();
		builder.withHtmlContent(inputHTML, baseUri);
		builder.useFastMode();
		builder.toStream(os);
		builder.run();

		// log.info("PDF generation completed");
		os.close();
		log.info("Inside htmlToPdfV3() - END");
	}

	/**
	 * @param inputHTML
	 * @param outputPdf
	 * @throws IOException
	 */
	public void htmlToPdfV2(String inputHTML, String outputPdf) throws IOException {
		Document doc = html5ParseDocument(inputHTML);
		Document doc1 = html5ParseDocument(inputHTML);
		String baseUri = FileSystems.getDefault().getPath("/fileserver/").toUri().toString();
		log.info("baseUri: " + baseUri);
		OutputStream os = new FileOutputStream(outputPdf);
		PdfRendererBuilder builder = new PdfRendererBuilder();
		// builder.withUri(outputPdf);
		builder.toStream(os);
		builder.withW3cDocument(doc, baseUri);
		// builder.withW3cDocument(doc1, baseUri);
		builder.run();
		log.info("PDF generation completed");
		os.close();
	}

	/**
	 * @param htmlContentList
	 * @param outputPdf
	 * @throws Exception
	 */
	public void htmlToPdf(List<String> htmlContentList, String outputPdf) throws Exception {
		OutputStream os = null;
		try {

			PDDocument doc = new PDDocument();

			String baseUri = FileSystems.getDefault().getPath("/fileserver/").toUri().toString();

			for (String htmlContent : htmlContentList) {
				htmlContent = htmlContent.replace("&nbsp;", "");
				htmlContent = htmlContent.replace("&", "&amp;");
				
				PdfRendererBuilder builder = new PdfRendererBuilder();
				builder.withHtmlContent(htmlContent, baseUri);
				builder.usePDDocument(doc);
				PdfBoxRenderer renderer = null;
				try {
					renderer = builder.buildPdfRenderer();
					renderer.createPDFWithoutClosing();

				} catch (Exception ex) {
					log.error("htmlContent: " + htmlContent);
					log.error("", ex);
				} finally {
					try {
						renderer.close();
					} catch (Exception ex) {

					}
				}
			}

			os = new FileOutputStream(outputPdf);
			doc.save(os);

		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception ex) {

			}
		}
	}

	/**
	 * @param sourceFilePath
	 * @param destinationFilePath
	 * @param entityName 
	 * @param districtName 
	 * @param authSectionEnabled 
	 * @throws IOException
	 */
	public void addPageNumbers(String sourceFilePath, String destinationFilePath, int totalPages) throws IOException {

		// PDDocument document, String numberingFormat, int offsetX, int offsetY,
		File file = new File(sourceFilePath);
		PDDocument document = PDDocument.load(file);
		addPageNumbers(document, "Page {0} of {1}", totalPages, 60, 18);
		document.save(new File(destinationFilePath));
		document.close();
	}

	/**
	 * @param document
	 * @param numberingFormat
	 * @param offsetX
	 * @param offsetY
	 * @param entityName 
	 * @param districtName 
	 * @param authSectionEnabled 
	 * @throws IOException
	 */
	public void addPageNumbers(PDDocument document, String numberingFormat, int totalPages, int offsetX, int offsetY)
			throws IOException {
		int page_counter = 1;
		int totalPagess = document.getNumberOfPages();
		for (PDPage page : document.getPages()) {
			PDPageContentStream contentStream = new PDPageContentStream(document, page,
					PDPageContentStream.AppendMode.APPEND, true, false);
			contentStream.beginText();
			contentStream.setFont(PDType1Font.TIMES_ITALIC, 10);
			PDRectangle pageSize = page.getMediaBox();
			float x = pageSize.getLowerLeftX();
			float y = pageSize.getLowerLeftY();
			contentStream.newLineAtOffset(x + pageSize.getWidth() - offsetX, y + offsetY);
			String text = MessageFormat.format(numberingFormat, page_counter, totalPagess);
			contentStream.showText(text);
			contentStream.endText();
			
            // Add current date
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
            contentStream.newLineAtOffset(x + 18, y + 15); 
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String currentDate = dateFormat.format(new Date());
            contentStream.showText("IESCMS Transport Pass Print Date " + currentDate);
            contentStream.endText();
            
//            if (authSectionEnabled) {
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
//                contentStream.newLineAtOffset(x + 18, y + 5);
//                contentStream.showText("Signature of Authorized");
//                contentStream.newLine();
//                contentStream.showText(entityName);
//                contentStream.newLine();
//                contentStream.showText("District - " + districtName);
//                contentStream.endText();
//            }
            
			contentStream.close();
			++page_counter;
		}
	}

	/**
	 * @param inputHTML
	 * @param outputPdf
	 * @throws IOException
	 */
	public void htmlToPdfFile(String inputHTML, String outputPdf) throws IOException {
		Document doc = html5ParseDocument(inputHTML);
		OutputStream os = new FileOutputStream(outputPdf);
		PdfRendererBuilder builder = new PdfRendererBuilder();
		builder.withUri(outputPdf);
		builder.toStream(os);
		builder.withW3cDocument(doc, null);
		builder.run();
		System.out.println("PDF generation completed");
		os.close();
	}

//	public static void main(String[] args) {
//		try {
//
//			// Source HTML file
//			// String inputHTML = HtmlToPdfService.class.getClassLoader()
//			// .getResource("co/oasys/scm/template/retail/TransportPassTemplateV1.html").getFile();
//			String inputHTML = AppUtil
//					.getFileContentFromClassPath("/com/oasys/scm/retail/template/TransportPassTemplateV1.html");
//			log.info("inputHTML: " + inputHTML);
//			// String inputHTML = "co/oasys/scm/template/TransportPassTemplateV1.html";
//			// Generated PDF file name
//			String outputPdf = "F:\\Java_IDE\\backup\\up_excise\\OASYS-UPEXCISE-SCM-SCHEDULER\\docs\\TransportPassV1.pdf";
//			// new HtmlToPdfService().htmlToPdf(inputHTML, outputPdf);
//
//			new HtmlToPdfService().htmlToPdfFile(inputHTML, outputPdf);
//			log.info("PDF Writtent");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
