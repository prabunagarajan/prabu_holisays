package com.oasys.posasset.dto;

public interface StockOverviewMapResponseDTO {
	String getLicenseNumber();

	int getCartonSize();

	int getStockNoOfBarcode();

	int getStockNoOfQRCode();

	int getEalRequestNoOfBarcode();

	int getEalRequestNoOfQRCode();

	int getUsedBrCode();

	int getUsedQRCode();

	int getBalanceBrCode();

	int getBalanceQRCode();

	int getBottledBrCode();

	int getBottledQRCode();

	int getDamagedNoOfBarcode();

	int getDamagedNoOfBottleQRCode();

	int getActualBarcodeBalance();

	int getActualQRCodeBalance();

	int getUsedWastageBarcode();

	int getUsedWastageQRCode();

	String getPackagingSize();
}
