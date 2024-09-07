package com.devar.cabs.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;



import lombok.extern.log4j.Log4j2;


@Log4j2
public class AppUtil {


	private static final String DATE_TIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

	private static final String DATE_FORMAT_STR = "yyyy-MM-dd";

	/**
	 * @return
	 */
	public static String getFormattedCurrentDate() {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_STR);
			return simpleDateFormat.format(new java.util.Date());
		} catch (Exception ex) {
			log.error("Exception at getFormattedDate()", ex);
		}
		return null;
	}

	/**
	 * @return
	 */
	public static String getFormattedCurrentDateAndTime() {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
			return simpleDateFormat.format(new java.util.Date());
		} catch (Exception ex) {
			log.error("Exception at getFormattedDate()", ex);
		}
		return null;
	}

	/**
	 * @param format
	 * @param givenDate
	 * @return
	 */
	public static String getFormattedDate(String format, Date givenDate) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.format(givenDate);
		} catch (Exception ex) {
			log.error("Exception at getFormattedDate()", ex);
		}
		return null;
	}

	/**
	 * @param format
	 * @return
	 */
	public static String getFormattedCurrentDate(String format) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.format(new java.util.Date());
		} catch (Exception ex) {
			log.error("Exception at getFormattedDate()", ex);
		}
		return null;
	}

	public static String getFormattedCurrentDate(String givenFormat, String requiredFormat, String dateValue) {
		String date = null;
		try {
			// "yyyy-MM-dd"
			SimpleDateFormat simpleDateRequiredFormat = new SimpleDateFormat(requiredFormat);
			SimpleDateFormat simpleDateGivenFormat = new SimpleDateFormat(givenFormat);
			return simpleDateRequiredFormat.format(simpleDateGivenFormat.parse(dateValue));

		} catch (Exception ex) {
			log.error("Exception at getFormattedCurrentDate(3)", ex);
		}
		return date;
	}

	public static Date getFormattedDateObject(String givenFormat, String dateValue) {
		Date date = null;
		try {
			// SimpleDateFormat simpleDateFormatFrom = new SimpleDateFormat("yyyy-MM-dd");

			if (!StringUtils.isEmpty(givenFormat) && !StringUtils.isEmpty(dateValue)) {
				SimpleDateFormat simpleDateFormatFrom = new SimpleDateFormat(givenFormat);
				return (simpleDateFormatFrom.parse(dateValue));
			}
		} catch (ParseException ex) {
			if (ex.getMessage().indexOf("Unparseable date") > -1) {
				log.error("ERROR(2): Given date " + dateValue + " is not in parseable format. Given format: "
						+ givenFormat);
			} else {
				log.error("Exception at getFormattedDateObject(1)", ex);
			}
		} catch (Exception ex) {
			log.error("Exception at getFormattedDateObject(2)", ex);
		}
		return date;
	}

	/**
	 * @param requiredFormat
	 * @param dateValue
	 * @return
	 */
	public static String getFormattedCurrentDate(String requiredFormat, String dateValue) {
		String date = null;
		try {
			// "yyyy-MM-dd"
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(requiredFormat);
			return simpleDateFormat.format(simpleDateFormat.parse(dateValue));

		} catch (Exception ex) {
			log.error("Exception at getFormattedCurrentDate(3)", ex);
		}
		return date;
	}

	/**
	 * @param format
	 * @return
	 */
	public static String getCurrentDate(String format) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.format(new java.util.Date());
		} catch (Exception ex) {
			log.error("Exception at getCurrentDate()", ex);
		}
		return null;
	}

	/**
	 * @param date
	 * @return
	 */
	public static Date getDateFormat(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
		try {
			return formatter.parse(date);
		} catch (ParseException ex) {
			log.error("Exception at getDateFormat()", ex);
		}
		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String quoteValue(Object value) {
		return getValueWithSingleQuoteWithNullCheck(value);
	}

	/**
	 * @param value
	 * @return
	 */
	private static String getValueWithSingleQuoteWithNullCheck(Object value) {
		if (value == null || "".equals(String.valueOf(value))) {
			return "NULL";
		}
		return new StringBuilder().append("'").append(value).append("'").toString();
	}

	/**
	 * @param value
	 * @param removeSingleQuote
	 * @return
	 */
	public static String quoteValue(Object value, boolean removeSingleQuote) {
		return getValueWithSingleQuoteWithNullCheck(value, removeSingleQuote);
	}

	/**
	 * @param value
	 * @param removeSingleQuote
	 * @return
	 */
	private static String getValueWithSingleQuoteWithNullCheck(Object value, boolean removeSingleQuote) {

		if (removeSingleQuote == false) {
			return getValueWithSingleQuoteWithNullCheck(value);
		}

		if (value == null || "".equals(String.valueOf(value))) {
			return "NULL";
		}

		String valueStr = String.valueOf(value).replace("'", "");
		return new StringBuilder().append("'").append(valueStr).append("'").toString();

	}

	/**
	 * @param dirPath
	 */
	public static boolean makeDir(String dirPath) {
		boolean directoryCreated = false;
		try {
			File dir = new File(dirPath);
			if (dir.isDirectory() == false) {
				directoryCreated = dir.mkdirs();
			}
		} catch (Exception ex) {
			log.error("Exception at makeDir()", ex);
		}
		return directoryCreated;
	}

	/**
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static int stringToInt(String value, int defaultValue) {
		try {
			if (value != null && !value.isEmpty()) {
				return Integer.parseInt(value.trim());
			}
		} catch (Exception ex) {
			log.error("Exception at stringToInt(1) " + ex.toString());
		}
		return defaultValue;
	}

	/**
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static Double stringToDouble(String value, Double defaultValue) {
		try {
			if (value != null && !value.isEmpty()) {
				return Double.valueOf(value.trim());
			}
		} catch (Exception ex) {
			log.error("Exception at stringToDouble(1) " + ex.toString());
		}
		return defaultValue;
	}

	/**
	 * @param value
	 * @return
	 */
	public static Double stringToDouble(String value) {
		try {
			if (value != null && !value.isEmpty()) {
				return Double.valueOf(value.trim());
			}
		} catch (Exception ex) {
			log.error("Exception at stringToDouble(2) " + ex.toString());
		}
		return null;
	}

	/**
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static Long stringToLong(String value, Long defaultValue) {
		try {
			if (value != null && !value.isEmpty()) {
				return Long.valueOf(value.trim());
			}
		} catch (Exception ex) {
			log.error("Exception at stringToLong(1) " + ex.toString());
		}
		return defaultValue;
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static String getFileContentFromClassPath(String filePath) {
		return getFileContent(filePath, true, false);
	}

	/**
	 * @param filePath
	 * @param inClassPath
	 * @return
	 */
	public static String getFileContent(String filePath, boolean inClassPath) {
		return getFileContent(filePath, inClassPath, false);
	}

	/**
	 * @param filePath
	 * @param inClassPath
	 * @param trimContent
	 * @return
	 */
	public static String getFileContent(String filePath, boolean inClassPath, boolean trimContent) {
		String fileContent = null;
		InputStream in = null;
		BufferedReader bReader = null;
		try {

			if (inClassPath) {
				in = AppUtil.class.getResourceAsStream(filePath);
				bReader = new BufferedReader(new InputStreamReader(in));
			} else {
				bReader = new BufferedReader(new FileReader(filePath));
			}

			String str;
			StringBuilder sb = new StringBuilder();
			while ((str = bReader.readLine()) != null) {
				if (trimContent) {
					str = str.trim();
				}
				sb.append(str);// .append("\n");
			}

			fileContent = sb.toString();
		} catch (NullPointerException ex) {
			log.fatal("Null exception. Please check file '" + filePath + "' if it exists.");
			log.fatal("NullPointerException at getFileContent(1)", ex);
		} catch (Exception e) {
			log.fatal("Exception at getFileContent(2) >>>> " + e.toString());
		} finally {
			try {
				if (bReader != null) {
					bReader.close();
				}
			} catch (Exception e) {
				log.fatal("Exception at getFileContent(3) >>>> " + e.toString());
			}

			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				log.fatal("Exception at getFileContent(3) >>>> " + e.toString());
			}
		}
		return fileContent;
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static String getFileContent(String filePath) {
		StringBuilder content = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		} catch (Exception e) {
			log.fatal("Exception at getFileContent(4)  {}", e.toString());
		}

		return content.toString();
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static byte[] getFileContentBytesFromClassPath(String filePath) {
		return getFileContentBytes(filePath, true);
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static byte[] getFileContentBytesFromFile(String filePath) {
		return getFileContentBytes(filePath, false);
	}

	/**
	 * @param filePath
	 * @param inClassPath
	 * @return
	 */
	public static byte[] getFileContentBytes(String filePath, boolean inClassPath) {

		InputStream in = null;
		byte[] bytes = null;
		try {

			if (inClassPath) {
				// bytes = Files.readAllBytes(Paths.get(filePath));
				in = AppUtil.class.getResourceAsStream(filePath);
				bytes = IOUtils.toByteArray(in);
			} else {
				bytes = Files.readAllBytes(new File(filePath).toPath());
			}

		} catch (NullPointerException ex) {
			log.fatal("Null exception. Please check file '" + filePath + "' if it exists.");
			log.fatal("NullPointerException at getFileContentBytes(1)", ex);
		} catch (Exception e) {
			log.fatal("Exception at getFileContentBytes(2) >>>> " + e.toString());
		} finally {

			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				log.fatal("Exception at getFileContentBytes(3) >>>> " + e.toString());
			}
		}
		return bytes;
	}

	/**
	 * @param filePath
	 */
	public static void deleteFile(String filePath) {
		try {
			File file = new File(filePath);
			if (file.isFile()) {
				file.delete();
			}
		} catch (Exception ex) {

		}

	}

	/**
	 * @param value
	 * @param decimalCount
	 * @return
	 */
	public static String trimDecimal(String value, int decimalCount) {

		if (value != null) {
			int idx = value.indexOf(".");
			if (idx > -1) {
				if (decimalCount == 0) {
					value = value.substring(0, idx);
				} else {
					boolean strHasLen = value.length() > (idx + 1 + decimalCount);
					if (strHasLen) {
						value = value.substring(0, (idx + 1 + decimalCount));
					}
				}
				return value;
			}
		}

		return value;
	}

	/**
	 * @param value
	 * @param format
	 * @param defaultValue
	 * @return
	 */
	public static String formatDouble(Double value, String format, String defaultValue) {
		String rtnValue = null;
		try {
			if (value != null) {
				// "0.00"
				rtnValue = new DecimalFormat(format).format(value);
			}
		} catch (Exception ex) {
			log.error("Exception at formatDouble(1) " + ex.toString());
		}
		return (rtnValue == null ? defaultValue : rtnValue);
	}

	/**
	 * @param value
	 * @param decimalFormat
	 * @param defaultValue
	 * @return
	 */
	public static String formatDouble(Double value, DecimalFormat decimalFormat, String defaultValue) {
		String rtnValue = null;
		try {
			if (value != null) {
				// "0.00"
				rtnValue = decimalFormat.format(value);
			}
		} catch (Exception ex) {
			log.error("Exception at formatDouble(2) " + ex.toString());
		}
		return (rtnValue == null ? defaultValue : rtnValue);
	}

	/**
	 * @param list
	 * @return
	 */
	public static String getListToCsvWithSingleQuote(List<String> list) {
		StringBuilder sb = new StringBuilder();
		// String[] inwardNumberSplit = csvValue.split(",");
		int listSize = list != null ? list.size() : 0;
		if (listSize == 0) {
			return null;
		}
		int cnt = 1;
		for (String token : list) {
			sb.append("'").append(token.trim()).append("'");
			if (cnt < listSize) {
				sb.append(",");
			}
			cnt++;

		}
		return sb.toString();
	}

	/**
	 * @param list
	 * @return
	 */
	public static String getListToCsvWithoutSingleQuote(List<String> list) {
		StringBuilder sb = new StringBuilder();
		int listSize = list != null ? list.size() : 0;
		if (listSize == 0) {
			return null;
		}
		int cnt = 1;
		for (String token : list) {
			sb.append(token.trim());
			if (cnt < listSize) {
				sb.append(",");
			}
			cnt++;

		}
		return sb.toString();
	}

	/**
	 * @param input
	 * @return
	 */
	public static boolean isInteger(String input) {
		try {
			// Define a regular expression pattern for an integer
			Pattern pattern = Pattern.compile("^[-+]?\\d+$");

			return pattern.matcher(input).matches();
		} catch (Exception ex) {
			log.error("Exception at isInteger()", ex);
		}

		return false;
	}

	/**
	 * @param input
	 * @return
	 */
	public static boolean isDouble(String input) {
		// Define a regular expression pattern for a double value
		try {
			Pattern pattern = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");

			return pattern.matcher(input).matches();
		} catch (Exception ex) {
			log.error("Exception at isDouble()", ex);
		}
		return false;
	}

	public static boolean isCSV(String input) {
		// Define a regular expression pattern for a CSV string
		String csvPattern = "^[^,]+(,[^,]+)*$";

		return Pattern.matches(csvPattern, input);
	}

	/**
	 * @param csvValue
	 * @return
	 */
//	public static List<String> getCsvToList(String csvValue) {
//
//		List<String> list = new ArrayList<>();
//
//		try {
//			CSVParser csvParser = CSVParser.parse(csvValue, CSVFormat.DEFAULT);
//			for (CSVRecord record : csvParser) {
//				for (String value : record) {
//					list.add(value);
//				}
//			}
//		} catch (Exception ex) {
//			list = null;
//			log.error("Exception at getCsvToList()", ex);
//		}
//
//		return list;
//	}

	/**
	 * @param value
	 * @return
	 */
	public static boolean isTrue(String value) {
		value = value == null ? "false" : value.toLowerCase();
		//
		if (!"true".equals(value) && !"false".equals(value)) {
			value = "false";
		}
		return Boolean.valueOf(value);
	}

	public static Integer stringToInt(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Exception ex) {
			log.error("Exception at stringToInt() " + ex.toString());
		}
		return null;
	}

//	public static String readJsonUrl() throws Exception {
//
//		System.out.println("jsonData");
//
//		String url = "https://www.upexciseportal.in/BarcodeQrcode/rest/mrp_gatepass/mrp_detail?date=01042022";
//
//		URL urlObj = new URL(url);
//
//		ObjectMapper mapper = new ObjectMapper();
//		Map<String, Object> map = mapper.readValue(urlObj, Map.class);
//
//		System.out.println("jsonData" + map);
//
//		Gson gson = new Gson();
//
//		String jsonData = gson.toJson(map);
//
//		log.info("jsonData: " + jsonData);
//
//		return gson.toJson(map);
//
//	}
//
//	public static void main(String... strings) throws Exception {
//		String value = AppUtil.getFileContent("C:\\Users\\jajotech\\Desktop\\karthick_files\\test.txt", false, false);
//		System.out.println(value);
//	}

	/**
	 * @param content
	 * @param outFilePath
	 */
	public static void writeToFile(String content, String outFilePath) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(outFilePath);
			bw = new BufferedWriter(fw);
			bw.write(content);
		} catch (Exception ex) {
			log.error("Exception at writeToFile() >>>> " + ex.toString());
		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}

	/**
	 * @param date
	 * @param totalDays
	 * @return
	 */
	public static Date addDayToDate(Date date, int totalDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, totalDays);
		return calendar.getTime();
	}

	/**
	 * @param date
	 * @param totalDays
	 * @return
	 */
	public static Date addDaysToDate(Date date, int totalDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, totalDays);
		return calendar.getTime();
	}

	/**
	 * @param pattern
	 * @param values
	 * @return
	 */
	public static final String formatMessage(String pattern, Object... values) {
		String formattedTxt = null;
		try {
			formattedTxt = MessageFormat.format(pattern, values);
		} catch (Exception ex) {
			StringBuilder sb = new StringBuilder();
			sb.append(pattern);
			int valuesLen = values != null ? values.length : 0;
			if (valuesLen > 0) {
				for (Object txt : values) {
					sb.append(txt).append(" / ");
				}

			}
			formattedTxt = sb.toString();
			log.error("Exception at formatMessage()", ex);
		}
		return formattedTxt;
	}

	/**
	 * @param dateTime
	 * @param dateTimeFormat
	 * @return
	 */
	public static Timestamp getTimestamp(String dateTime, String dateTimeFormat) {
		if (org.apache.commons.lang3.StringUtils.isBlank(dateTime)
				|| org.apache.commons.lang3.StringUtils.isBlank(dateTimeFormat)) {

			return null;

		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);

			// Parse the string to obtain a Date object
			Date parsedDate = dateFormat.parse(dateTime);

			// Convert the Date object to a Timestamp
			return new Timestamp(parsedDate.getTime());

		} catch (Exception ex) {
			log.error("Exception at getTimestamp()", ex);
		}
		return null;
	}

	/**
	 * @param dateTime
	 * @param dateTimeFormat
	 * @param hoursToAdd
	 * @return
	 */
	public static Timestamp getTimestamp(String dateTime, String dateTimeFormat, Integer hoursToAdd) {
		if (org.apache.commons.lang3.StringUtils.isBlank(dateTime)
				|| org.apache.commons.lang3.StringUtils.isBlank(dateTimeFormat)) {

			return null;

		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);

			// Parse the string to obtain a Date object
			Date parsedDate = dateFormat.parse(dateTime);

			if (hoursToAdd != null && hoursToAdd.intValue() > -1) {

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(parsedDate);

				// Add hours to the date
				calendar.add(Calendar.HOUR_OF_DAY, hoursToAdd);

				// Get the final Date object
				Date finalDate = calendar.getTime();

				// Convert the Date object to a Timestamp
				return new Timestamp(finalDate.getTime());
			} else {
				return new Timestamp(parsedDate.getTime());
			}

		} catch (Exception ex) {
			log.error("Exception at getTimestamp()", ex);
		}
		return null;
	}

	/**
	 * @param hoursToAdd
	 * @return
	 */
	public static Timestamp getCurrentTimestampPlusHours(Integer hoursToAdd) {

		try {
			// Get the current timestamp
			LocalDateTime currentDateTime = LocalDateTime.now();

			LocalDateTime newDateTime = currentDateTime.plusHours(hoursToAdd);

			return Timestamp.valueOf(newDateTime);

		} catch (Exception ex) {
			log.error("Exception at getCurrentTimestamp()", ex);
		}
		return null;
	}
//	/**
//	 * @param dirPath
//	 * @return
//	 */
//	public static boolean isDirectory(String dirPath) {
//		try {
//			File dir = new File(dirPath);
//			return (dir.isDirectory());
//		} catch (Exception ex) {
//			log.error("Exception at isDirectory()", ex);
//		}
//		return false;
//	}



}
