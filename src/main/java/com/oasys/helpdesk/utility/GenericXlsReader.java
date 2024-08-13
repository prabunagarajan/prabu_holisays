package com.oasys.helpdesk.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GenericXlsReader {

//	public static void main(String a[]) {
//	try {
//
//		String fileName = "/home/user/Desktop/ReconciliationFile/12.xlsx";
//		String rowUniqueIdentifier = "bctransactionid";
//		String rowFilterKey = "txntypecode";
//		String rowFilterValue = "0";
//
//		Map<String, Map<String, String>> rowMap = getFileProcessed(fileName, rowUniqueIdentifier, rowFilterKey,
//				rowFilterValue);
//	} catch (Exception ex) {
//		ex.printStackTrace();
//	}
//}

	@SuppressWarnings("resource")
	public static Map<String, Map<String, String>> getFileProcessed(String fileName, String rowUniqueIdentifier,
			String rowFilterKey, String rowFilterValue) throws Exception {

		int columnCounter = 0;
		int lastColumnCount = 0;
		long rowCounter = 0;
		Row row = null;

		rowUniqueIdentifier = Library.toLowerCase(rowUniqueIdentifier);
		rowFilterKey = Library.toLowerCase(rowFilterKey);
		rowFilterValue = Library.toLowerCase(rowFilterValue);
		List<String> rowHeader = new ArrayList<String>();
		Map<String, Map<String, String>> rowMap = new HashMap<String, Map<String, String>>();
		List<Map> rowList = new ArrayList<Map>();
		FileInputStream fis =null;
		File myFile = null;
		XSSFWorkbook myWorkBook = null;
		try {
		myFile = new File(fileName);
		fis = new FileInputStream(myFile);
		myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Iterator<Row> rowIterator = mySheet.iterator();

		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			lastColumnCount = row.getLastCellNum();
			columnCounter = 0;
			Map<String, String> colMap = setDefaultValueInMap(rowHeader);
			while (columnCounter < lastColumnCount) {
				Cell cell = row.getCell(columnCounter);
				if (cell != null) {
					if (rowCounter == 0) {
						rowHeader.add(Library.toLowerCase(cell.getStringCellValue()));
					} else {
						colMap.put(rowHeader.get(columnCounter), getData(rowHeader.get(columnCounter), cell));
					}
				}
				columnCounter++;
			}
			if (rowCounter > 0) {

				if (rowFilterKey != null && colMap.containsKey(rowFilterKey) && colMap.get(rowFilterKey) != null
						&& ((String) colMap.get(rowFilterKey)).equals(rowFilterValue)) {
					rowMap.put(colMap.get(rowUniqueIdentifier), colMap);
					rowList.add(colMap);
				} else {
					if ((rowFilterKey == null || rowFilterValue.equals(""))
							&& colMap.containsKey(rowUniqueIdentifier)) {
						rowMap.put(colMap.get(rowUniqueIdentifier), colMap);
						rowList.add(colMap);
					}
				}
			}
			rowCounter++;
		}
		if (rowList.size() != rowMap.size()) {
			throw new Exception();
		}
		rowList = null;
		}finally {
			fis=null;
			myWorkBook=null;
		}
		System.gc();
		return rowMap;
	}

	private static String getData(String header, Cell cell) throws Exception {
		String strReturnValue = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			strReturnValue = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				if (isDateFeild(header)) {
					strReturnValue = DateFileConverter
							.getDateFromDateTime(DateFileConverter.getDate(cell.getDateCellValue()));
					break;
				}
				DateFileConverter.getTimeFromDateTime(DateFileConverter.getDate(cell.getDateCellValue()));
				break;
			}
			if (isAmountFeild(header)) {
				strReturnValue = (float) cell.getNumericCellValue() + "";
				break;
			}
			strReturnValue = (long) cell.getNumericCellValue() + "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			strReturnValue = cell.getBooleanCellValue() + "";
			break;
		default:
		}
		return Library.trimAndRemoveSpecialCharacter(strReturnValue);
	}

	private static boolean isAmountFeild(String strFeildName) throws Exception {
		if (strFeildName != null && !strFeildName.trim().equals("")) {
			strFeildName = strFeildName.trim().toLowerCase();
			if (strFeildName.indexOf("amt") != -1 || strFeildName.indexOf("ammount") != -1
					|| strFeildName.indexOf("amount") != -1) {
				return true;
			}
		}
		return false;
	}

	private static boolean isDateFeild(String strFeildName) throws Exception {
		if (strFeildName != null && !strFeildName.trim().equals("")) {
			strFeildName = strFeildName.trim().toLowerCase();
			if (strFeildName.indexOf("date") != -1 || strFeildName.indexOf("Date") != -1
					|| strFeildName.indexOf("DATE") != -1) {
				return true;
			}
		}
		return false;
	}

	private static Map<String, String> setDefaultValueInMap(List<String> rowHeader) throws Exception {
		Map colMap = new HashMap<String, String>();
		Iterator<String> iterator = rowHeader.iterator();
		while (iterator.hasNext()) {
			colMap.put((String) iterator.next(), null);
		}
		return colMap;
	}
}