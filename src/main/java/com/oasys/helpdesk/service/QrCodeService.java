package com.oasys.helpdesk.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class QrCodeService {


	private static Font qrCodeFont = null;

//	public String buildQrCode() {
//		
//		ByteArrayOutputStream outputStream = QRCode.from(qrCodeText).to(ImageType.PNG)
//				.withHint(EncodeHintType.MARGIN, 2).withSize(100, 100).stream();
//		
//	}

	public static Font getQrCodeFont() {
		if (qrCodeFont == null) {
			qrCodeFont = new Font("Arial", Font.PLAIN, 25);
		}
		return qrCodeFont;
	}

	public void createSingleQRImage(String qrCodeText, int size, String filePath) {

		log.info("qrCodeText: {}",qrCodeText);
		log.info("size:{}",size);
		log.info("filePath: {}:",filePath);
		
		try {

			if (StringUtils.isBlank(qrCodeText)) {
				throw new Exception("QR Content is empty");
			}

			if (StringUtils.isBlank(filePath)) {
				throw new Exception("QR Content file path is empty");
			}

			File outputFile = new File(filePath);

			if (outputFile.getParentFile() != null) {
				File dir = new File(outputFile.getParentFile().getAbsolutePath());

				if (dir.isDirectory() == false) {
					dir.mkdirs();
				}
			}

			Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			hintMap.put(EncodeHintType.MARGIN, 2);

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
			// Make the BufferedImage that are to hold the QRCode
			int matrixWidth = byteMatrix.getWidth();
			BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
			image.createGraphics();

			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, matrixWidth, matrixWidth);
			// Paint and save the image using the ByteMatrix
			graphics.setColor(Color.BLACK);

			for (int i = 0; i < matrixWidth; i++) {
				for (int j = 0; j < matrixWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}

			ImageIO.write(image, "png", outputFile);

			log.info("Qr code written");
		} catch (Exception ex) {
			log.error("Exception at createQRImage()", ex);
		}
	}


}
