package com.quotepro.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import jakarta.servlet.http.HttpServletRequest;

public class Const {
	// private static final org.apache.log4j.Logger loger =
	// org.apache.log4j.Logger.getLogger(DeviceLoad.class);
	public static final Logger loger = LoggerFactory.getLogger(Const.class);
	public static final String version = "4";
	public static int OS = 1;
	public static int Procedur = 0;
	public static int Procedur1 = 0;
	public static int NProcedur1 = 0;
	public static final String DB = "MASTER";
	// public static final String DB = "is_machine";
	public static final String DB_PREFIX = "is_machine2";
	public static final String DB_USER = "root";
//	    public static final String DB_PASSWORD = "logix7460832";
	public static final String DB_PASSWORD = "implies";
	public static String path = "";
	public static Map<?, ?> listDevices, listCPANEL, listFormulla, listSignal;;
	public static List<?> listShift;
	public static List<?> lstEffRange, lstMachineSta, lstFontSize, lstFactory, lstNotification, listLastEff;
	public static List<?> lTermminalGM, lTermminal;
	public static final String TASK_ADD = "add";
	public static final String TASK_EDIT = "edit";
	public static final String TASK_DELETE = "delete";
	public static final String TASK_SEARCH = "search";
	public static final String TASK_PRINT = "print";
	public static String APP_PATH = "/home/eideed/public_html/EMS";
	public static String SOFTTYPE = "WJ";

	public static final String GRD_EDIT = "<![CDATA[<a href=\\\"#\\\" onClick=\\\"editData({ID},event)\\\"><i style=\\\"color:#E88A05\\\" class=\\\"glyphicon glyphicon-pencil\\\"></i></a>]]>";
	public static final String GRD_DELETE = "<![CDATA[<a href=\\\"#\\\" onClick=\\\"deleteData({ID},event)\\\"><i style=\\\"color:#CE4B27\\\" class=\\\"glyphicon glyphicon-remove\\\"></i></a>]]>";
	public static final String GRD_SEARCH = "<![CDATA[<a href=\\\"#\\\" onClick=\\\"searchData({ID},event)\\\"><i style=\\\"color:#7C798F\\\" class=\\\"glyphicon glyphicon-search\\\"></i></a>]]>";
	public static final String GRD_SUMMARY = "<![CDATA[<a href=\\\"#\\\" onClick=\\\"summaryData({ID},event)\\\"><i style=\\\"color:#03366A\\\" class=\\\"glyphicon glyphicon-th\\\"></i></a>]]>";

	public static final String GRD_PRINT = "/home/eideed/public_html/EMS";
	public static final String WHATSAPP_URL = "http://dev.impliessolution.com:8080/emsws/login/SendWA";
	public static final String OFF_URL = "http://dev.impliessolution.com:8080/emsws/login/AddOffMachine";

	@Autowired
	private static ResourceLoader resourceLoader;

	@Autowired
	public static SessionBean sessionBean;

	public static SessionBean getSessionBean() {
		return sessionBean;
	}

	public static Map getListSignal() {
		return listSignal;
	}

	public static void setListSignal(Map listSignal) {
		Const.listSignal = listSignal;
	}
	

	public static Map getListFormulla() {
		return listFormulla;
	}

	public static void setListFormulla(Map listFormulla) {
		Const.listFormulla = listFormulla;
	}

	public static List getListLastEff() {
		return listLastEff;
	}

	public static void setListLastEff(List listLastEff) {
		Const.listLastEff = listLastEff;
	}

	public static List getLstNotification() {
		return lstNotification;
	}

	public static void setLstNotification(List lstNotification) {
		Const.lstNotification = lstNotification;
	}

	public static List getLstFactory() {
		return lstFactory;
	}

	public static void setLstFactory(List lstFactory) {
		Const.lstFactory = lstFactory;
	}

	public static Map getListDevices() {
		return listDevices;
	}

	public static void setListDevices(Map listDevices) {
		Const.listDevices = listDevices;
	}

	public static Date getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	public static String getMessage(String locale, String VMSG) {
		locale = locale.equals("en") ? "" : ("_" + locale);
		// locale ="";
		String msg = "";
		try {
			Resource resource = resourceLoader.getResource("/WEB-INF/classes/message" + locale + ".properties");
			Properties prop = new Properties();
			prop.load(resource.getInputStream());
			msg = prop.getProperty(VMSG);
		} catch (Exception ex) {

		}
		return msg;
	}

	public static List getListShift() {
		return listShift;
	}

	public static void setListShift(List listShift) {
		Const.listShift = listShift;
	}

	public static Map getListCPANEL() {
		return listCPANEL;
	}

	public static void setListCPANEL(Map listCPANEL) {
		Const.listCPANEL = listCPANEL;
	}

	@SuppressWarnings("rawtypes")
	public static boolean getEndShift(String time) throws Throwable {
		Map hm;
		for (int i = 0; i < getListShift().size(); i++) {
			hm = (HashMap) Const.getListShift().get(i);
			if (hm.get("ENDTIME").toString().equals(time)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static String getCurrentShift(String time) throws Throwable {
		Date time1, time2, cTime;
		Calendar calendar1, calendar2, calendarTime;
		Map hm;
		for (int i = 0; i < getListShift().size(); i++) {
			hm = (HashMap) Const.getListShift().get(i);
			//
			time1 = new SimpleDateFormat("HH:mm:ss").parse(hm.get("STARTTIME").toString());
			calendar1 = Calendar.getInstance();
			calendar1.setTime(time1);

			time2 = new SimpleDateFormat("HH:mm:ss").parse(hm.get("ENDTIME").toString());
			calendar2 = Calendar.getInstance();
			calendar2.setTime(time2);
//	            calendar2.add(Calendar.DATE, 1);

			if (time.trim().length() == 6) {
				cTime = new SimpleDateFormat("HHmmss").parse(time);
			} else {
				cTime = new SimpleDateFormat("HH:mm:ss").parse(time);

			}
			calendarTime = Calendar.getInstance();
			calendarTime.setTime(cTime);

			Date x = calendarTime.getTime();
			if ((x.equals(calendar1.getTime()) || x.after(calendar1.getTime()))
					&& (x.equals(calendar2.getTime()) || x.before(calendar2.getTime()))) {
				return hm.get("SID").toString();
			}
		}
		hm = (HashMap) Const.getListShift().get(Const.getListShift().size() - 1);
		return hm.get("SID").toString();
	}

	@SuppressWarnings("rawtypes")
	public static String getShiftDate(String shift, String date, String dateformat, String time, String timeformat)
			throws Throwable {
		LocalDate sdate = LocalDate.parse(date, DateTimeFormatter.ofPattern(dateformat));
		LocalTime ltime = LocalTime.parse(time, DateTimeFormatter.ofPattern(timeformat));
		LocalTime stime = LocalTime.parse(((HashMap) Const.getListShift().get(0)).get("STARTTIME").toString(),
				DateTimeFormatter.ofPattern("HH:mm:ss"));
		if (!shift.equals("1")) {
			if (ltime.isBefore(stime)) {
				sdate = sdate.minusDays(1);
			}
		}
		return sdate.toString();
	}

	@SuppressWarnings("rawtypes")
	public static String getCurrentShift(String time, int minute) throws Throwable {
		Date time1, time2, cTime;
		Calendar calendar1, calendar2, calendarTime;
		Map hm;
		for (int i = 0; i < getListShift().size(); i++) {
			hm = (HashMap) Const.getListShift().get(i);
			//
			time1 = new SimpleDateFormat("HH:mm:ss").parse(hm.get("STARTTIME").toString());
			calendar1 = Calendar.getInstance();
			calendar1.setTime(time1);

			time2 = new SimpleDateFormat("HH:mm:ss").parse(hm.get("ENDTIME").toString());
			calendar2 = Calendar.getInstance();
			calendar2.setTime(time2);
//	            calendar2.add(Calendar.DATE, 1);

			if (time.trim().length() == 6) {
				cTime = new SimpleDateFormat("HHmmss").parse(time);
			} else {
				cTime = new SimpleDateFormat("HH:mm:ss").parse(time);

			}
			calendarTime = Calendar.getInstance();
			calendarTime.setTime(cTime);
			calendarTime.add(Calendar.MINUTE, -minute);

			Date x = calendarTime.getTime();
			if ((x.equals(calendar1.getTime()) || x.after(calendar1.getTime()))
					&& (x.equals(calendar2.getTime()) || x.before(calendar2.getTime()))) {
				return hm.get("SID").toString();
			}
		}
		hm = (HashMap) Const.getListShift().get(Const.getListShift().size() - 1);
		return hm.get("SID").toString();
	}

	@SuppressWarnings("rawtypes")
	public static long getCurrentShiftTime() throws Throwable {
		Date time1, time2, cTime;
		Calendar calendar1, calendar2, calendarTime;
		String time = LocalTime.now().toString();
		time = time.substring(0, time.lastIndexOf("."));
		Map hm;
		for (int i = 0; i < getListShift().size(); i++) {
			hm = (HashMap) Const.getListShift().get(i);
			//
			time1 = new SimpleDateFormat("HH:mm:ss").parse(hm.get("STARTTIME").toString());
			calendar1 = Calendar.getInstance();
			calendar1.setTime(time1);

			time2 = new SimpleDateFormat("HH:mm:ss").parse(hm.get("ENDTIME").toString());
			calendar2 = Calendar.getInstance();
			calendar2.setTime(time2);
//	            calendar2.add(Calendar.DATE, 1);

			cTime = new SimpleDateFormat("HH:mm:ss").parse(time);

			calendarTime = Calendar.getInstance();
			calendarTime.setTime(cTime);

			Date x = calendarTime.getTime();
			if ((x.equals(calendar1.getTime()) || x.after(calendar1.getTime()))
					&& (x.equals(calendar2.getTime()) || x.before(calendar2.getTime()))) {
				long diff = cTime.getTime() - time1.getTime();
				long diffMinutes = diff / (60 * 1000);
				return diffMinutes + 1;
			}
		}
		hm = (HashMap) Const.getListShift().get(Const.getListShift().size() - 1);
		time1 = new SimpleDateFormat("HH:mm:ss").parse(hm.get("STARTTIME").toString());
		time2 = new SimpleDateFormat("HH:mm:ss").parse(hm.get("ENDTIME").toString());
		long diff1 = time2.getTime() - time1.getTime();
		cTime = new SimpleDateFormat("HH:mm:ss").parse(time);
		time2 = new SimpleDateFormat("HH:mm:ss").parse("00:00:00");
		long diff2 = cTime.getTime() - time1.getTime();
		long diffMinutes = (diff1 + diff2) / (60 * 1000);
		return diffMinutes;
	}

	public static List getLstEffRange() {
		return lstEffRange;
	}

	public static void setLstEffRange(List lstEffRange) {
		Const.lstEffRange = lstEffRange;
	}

	@SuppressWarnings("rawtypes")
	public static List getLstMachineSta() {
		return lstMachineSta;
	}

	@SuppressWarnings("rawtypes")
	public static void setLstMachineSta(List lstMachineSta) {
		Const.lstMachineSta = lstMachineSta;
	}

	public static List getLstFontSize() {
		return lstFontSize;
	}

	public static void setLstFontSize(List lstFontSize) {
		Const.lstFontSize = lstFontSize;
	}

	@SuppressWarnings("rawtypes")
	public static List getlTermminalGM() {
		return lTermminalGM;
	}

	public static void setlTermminalGM(List lTermminalGM) {
		Const.lTermminalGM = lTermminalGM;
	}

	@SuppressWarnings("rawtypes")
	public static List getlTermminal() {
		return lTermminal;
	}

	public static void setlTermminal(List lTermminal) {
		Const.lTermminal = lTermminal;
	}

	public static String getDTime() throws Throwable {
		Date time1 = new Date();
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(time1);
		calendar1.add(Calendar.SECOND, 5);
		time1 = calendar1.getTime();
		SimpleDateFormat smf = new SimpleDateFormat("yyMMddHHmmssu");
		String sTime = smf.format(time1);
//	          sTime += calendar1.DAY_OF_WEEK;
//	        System.out.println("sTime = " + sTime);
		return sTime;
	}

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		Const.path = path;
	}

	public static String getXPath() {
		try {
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
					"wmic path win32_physicalmedia where tag='\\\\\\\\.\\\\PHYSICALDRIVE0' get serialnumber /format:list");
			builder.redirectErrorStream(true);
			Process p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (!line.trim().isEmpty()) {
					break;
				}
			}
			line = line.substring(line.indexOf("=") + 1).trim();
			return line;
		} catch (Exception ex) {
			return "";
		}
	}

	public static String encrypt(String text) {
		try {
			text = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".substring(text.length()) + text;
			String key = "qnc48s19bmrhawxo"; // 128 bit key
			// Create key and cipher

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec secretKeySpecy = new SecretKeySpec(getKeyBytes(key), "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(getKeyBytes(key));
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpecy, ivParameterSpec);
			byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
			return Base64.encodeBase64String(encrypted);

		} catch (Exception e) {
			return "";
		}
	}

	public static String descrypt(String text) {
		text = text.trim();
		try {
			String key = "qnc48s19bmrhawxo"; // 128 bit key
			// Create key and cipher
			byte[] cipheredBytes = Base64.decodeBase64(text);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec secretKeySpecy = new SecretKeySpec(getKeyBytes(key), "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(getKeyBytes(key));
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
			cipheredBytes = cipher.doFinal(cipheredBytes);
			return new String(cipheredBytes);
		} catch (Exception e) {
			return "";
		}
	}

	public static int CompareL(String Key) {
		try {

			if (getXPath().equals(descrypt(Key)) && !getXPath().isEmpty() && !descrypt(Key).isEmpty()) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
		// return 1;
	}

	private static byte[] getKeyBytes(String key) throws UnsupportedEncodingException {
		byte[] keyBytes = new byte[16];
		byte[] parameterKeyBytes = key.getBytes("UTF-8");
		System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
		return keyBytes;
	}

	public static String extractPostRequestBody(HttpServletRequest request) {
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			Scanner s = null;
			try {
				s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return s.hasNext() ? s.next() : "";
		}
		return "";
	}

	public static String formatDecimal(String val) {
		DecimalFormat format = new DecimalFormat("0.##"); // Choose the number of decimal places to work with in case
															// they are different than zero and zero value will be
															// removed
		format.setRoundingMode(RoundingMode.DOWN);
		return format.format(Float.parseFloat(val));
	}

}
