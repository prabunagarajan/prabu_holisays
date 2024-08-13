package com.oasys.posasset.dto;

public interface StockOverviewUnMapResponseDTO {

    String getLicenseNumber();
    String getPrintingType();
    String getUnmappedType();
    String getMapType();
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
}
