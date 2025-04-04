package com.quotepro.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.quotepro.common.enumeration.CommonEnum;
import com.quotepro.common.enumeration.ToastrAction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Create For Master Common Methods
 *
 * @author Emerging Five
 */
public class Common {

	public static final Logger LOGGER = LoggerFactory.getLogger(Common.class);
	private static BigInteger oldTxnnumber = new BigInteger("0");

	/**
	 * Return a true false boolean.
	 *
	 * @param s Any String
	 * @return The string passed in or empty string if it is null.
	 */
	public static boolean checkNullAndEmpty(String... var) {
		boolean flag = false;
		if (var != null) {
			if (var.length > 0) {
				for (int i = 0; i < var.length; i++) {
					String string = var[i];
					if (null != string && string.trim().length() > 0) {
						flag = true;
					} else {
						flag = false;
						return flag;
					}
				}
			}
		}
		return flag;
	}

	public static boolean checkNullAndEmpty(Object... var) {
		boolean flag = false;
		if (var.length > 0) {
			for (int i = 0; i < var.length; i++) {
				Object obj = var[i];
				if (null != obj && checkNullAndEmpty(String.valueOf(obj))) {
					flag = true;
				} else {
					flag = false;
					return flag;
				}
			}
		}
		return flag;
	}

	public static boolean checkListNullAndEmpty(List<?> list) {
		boolean flag = false;
		if (list != null) {
			if (!list.isEmpty()) {
				if (list.size() > 0) {
					flag = true;
				} else {
					flag = false;
					return flag;
				}
			} else {
				flag = false;
				return flag;
			}

		}
		return flag;
	}

	public static String getString(String string) {
		if (string != null) {
			if (!string.trim().isEmpty()) {
				return string.trim();
			}
		}
		return null;
	}

	public static String getOnlyString(Object string) {
		if (string != null) {
			return string.toString();
		}
		return "";
	}

	public static Integer getInteger(String string) {
		if (string != null) {
			if (!string.trim().isEmpty()) {
				return Integer.parseInt(string.trim());
			}
		}
		return null;
	}

	/**
	 * Return a true false boolean.
	 *
	 * @param s Any String
	 * @return The string passed in or empty string if it is null.
	 */
	public static boolean isNumeric(String str) {
		if (null != str && str.matches("\\d*\\.?\\d+")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Return a true false boolean.
	 *
	 * @param s Any String
	 * @return The string passed in or empty string if it is null.
	 */
	public static boolean checkDateFormat(String str) {
		if (null != str && str.trim().matches("\\d{2}/\\d{2}/\\d{4}")) {
			return true;
		} else {
			return false;
		}
	}

	public static String uploadFile(File fileObj, String currentFilename, String dirName, String filenameSaveAs,
			String serverPath) {
		try {
			File theDir = null;
			String extention = null;
			String uploadpath = null;
			if (null != fileObj && fileObj.length() > 0) {
				extention = currentFilename.substring(currentFilename.lastIndexOf("."));
				uploadpath = "/" + dirName + "/" + filenameSaveAs + extention;
				// Now Check Is Dir Exist
				theDir = new File(serverPath + "/" + dirName + "/");
				if (!theDir.exists())
					theDir.mkdirs(); // It will create Directories according to 'theDir' path
				// Now Check is File already Exist
				File f = new File(serverPath + "/" + uploadpath);
				if (f.exists()) { // if File is Exist then Existing file will be delete
					f.delete();
				}
				// Now File Copy in Dir
				File srcFile = new File(serverPath + "/" + uploadpath);
				try (FileInputStream fileInputStream = new FileInputStream(fileObj)) {
					if (null != fileInputStream) {
						Files.copy(fileInputStream, srcFile.toPath());
						return uploadpath;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (null != fileObj)
						FileUtils.forceDelete(fileObj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public static long getTxnnumber() {
		Calendar calendar = Calendar.getInstance();
		String year = "" + calendar.get(Calendar.YEAR);
		String month = "" + (calendar.get(Calendar.MONTH) + 1);
		String day = "" + calendar.get(Calendar.DATE);
		String hour = "" + calendar.get(Calendar.HOUR);
		String minutes = "" + calendar.get(Calendar.MINUTE);
		String second = "" + calendar.get(Calendar.SECOND);
		String milisecond = "" + calendar.get(Calendar.MILLISECOND);
		String nenoSec = "" + System.nanoTime();
		year = year.substring(2);
		StringBuilder sbNumber = new StringBuilder("" + year);
		if (month.length() == 1)
			month = "0" + month;
		if (day.length() == 1)
			day = "0" + day;
		if (hour.length() == 1)
			hour = "0" + hour;
		if (minutes.length() == 1)
			minutes = "0" + minutes;
		if (second.length() == 1)
			second = "0" + second;
		if (milisecond.length() == 1)
			milisecond = "0" + milisecond;
		if (milisecond.length() > 2)
			milisecond = milisecond.substring(0, 2);
		if (nenoSec.length() == 1)
			nenoSec = "000" + nenoSec;
		if (nenoSec.length() == 2)
			nenoSec = "00" + nenoSec;
		if (nenoSec.length() == 3)
			nenoSec = "0" + nenoSec;
		if (nenoSec.length() > 4)
			nenoSec = nenoSec.substring(0, 4);

		sbNumber.append(month);
		sbNumber.append(day);
		sbNumber.append(hour);
		sbNumber.append(minutes);
		sbNumber.append(second);
		sbNumber.append(milisecond);
		sbNumber.append(nenoSec);
		return Long.parseLong(sbNumber.toString());
	}

	public static List<?> getDataFromQuery(String strQuery, Session session) {
		try {
			Query<?> query = session.createNativeQuery(strQuery);
			List<?> dataList = query.getResultList();
			return dataList;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static List<?> getDatainList(EntityManagerFactory emf, String sqlQuery) throws Exception {
		Session session = emf.createEntityManager().unwrap(Session.class);
		List<?> list = null;
		try {
			if (checkNullAndEmpty(sqlQuery)) {
				Query<?> query = session.createNativeQuery(sqlQuery);
				list = query.getResultList();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (null != session) {
				session.clear();
				session.close();
			}
		}
		return list;
	}

//	public static List<Map<String, Object>> queryRunner(EntityManagerFactory emf, String sqlQuery) throws Exception {
//		Session session = emf.createEntityManager().unwrap(Session.class);
//		Connection connection = ((SessionImpl) session).connection();
//		List<Map<String, Object>> list = null;
//		try {
//			if (checkNullAndEmpty(sqlQuery)) {
//				list = new QueryRunner().query(connection, sqlQuery, new MapListHandler());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception(e);
//		} finally {
//			if (null != connection)
//				connection.close();
//			if (null != session) {
//				session.clear();
//				session.close();
//			}
//		}
//		return list;
//	}

	public static String getMaxValue(String tablename, String columnname, String whereClause, Session session) {
		try {
			String strQuery = "SELECT  NVL(MAX(" + columnname + ") ,0)+1 FROM  " + tablename;
			if (null != whereClause && whereClause.trim().length() > 0)
				strQuery += " " + whereClause;
			List<?> maxPlusValList = getDataFromQuery(strQuery, session);
			if (null != maxPlusValList && !maxPlusValList.isEmpty())
				return maxPlusValList.get(0).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

	public static void persistanceexception(PersistenceException ex, EntityTransaction transaction,
			Map<String, Object> map, String linkid, EntityManagerFactory emf, HttpServletRequest request) {
		SQLException etrp = (SQLException) ex.getCause().getCause();
		etrp.printStackTrace();
		String errorString = "", errorTitle = "";
		if (null != transaction && transaction.isActive()) {
			transaction.rollback();
			LOGGER.info("TRANSACTION ROLLBACKED.");
		}
		LOGGER.info("error : {}", etrp);
		map.put("error", etrp.getMessage());
		errorString = CommonEnum.SQL_SERVER_ERROR.getValue();
		errorTitle = CommonEnum.ERROR.getValue();

		map.put("iserror", "Y");
		map.put("errorString", errorString);
		map.put("errorType", ToastrAction.ERROR);
		map.put("errorTitle", errorTitle);
		StringWriter sw = new StringWriter();
		etrp.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();
		// dbUtils.saveErrorMsg(etrp.getMessage(), exceptionAsString, linkid, emf,
		// request);
	}

	public static void exception(Exception e, EntityTransaction transaction, Map<String, Object> map, String linkid,
			EntityManagerFactory emf, HttpServletRequest request) {
		if (null != transaction && transaction.isActive()) {
			transaction.rollback();
			LOGGER.info("TRANSACTION ROLLBACKED.");
		}
		LOGGER.error("{}", e);
		map.put("error", e.getMessage());
		String errorString = "", errorTitle = "";
		errorString = CommonEnum.INTERNAL_SERVER_ERROR.getValue();
		errorTitle = CommonEnum.ERROR.getValue();

		map.put("iserror", "Y");
		map.put("errorString", errorString);
		map.put("errorType", ToastrAction.ERROR);
		map.put("errorTitle", errorTitle);
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();
		// dbUtils.saveErrorMsg(e.getMessage(), exceptionAsString, linkid, emf,
		// request);
	}

	public static void throwException(PersistenceException ex, Exception e, EntityTransaction transaction,
			Map<String, Object> map, String linkid, EntityManagerFactory emf, HttpServletRequest request) {
		if (null != e) {
			e.printStackTrace();
			exception(e, transaction, map, linkid, emf, request);
		} else if (null != ex) {
			ex.printStackTrace();
			persistanceexception(ex, transaction, map, linkid, emf, request);
		}
	}

	public static void throwThrowableException(Throwable ex, EntityTransaction transaction, Map<String, Object> map,
			String linkid, EntityManagerFactory emf, HttpServletRequest request) {

		exception(new Exception(ex.getMessage()), transaction, map, linkid, emf, request);

	}

	public static Timestamp convertStringDatetoTimestamp(String dateString) {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = formatter.parse(dateString);
			return new Timestamp(date.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Timestamp convertStringDatetoTimestampnew(String dateString) {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy/MM/dd");
			Date date = formatter.parse(dateString);
			return new Timestamp(date.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertStringDateStringtoDateNumber(String dateString) {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = formatter.parse(dateString);
			DateFormat formatter2;
			formatter2 = new SimpleDateFormat("yyyyMMdd");
			return formatter2.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertCurrentDatetoDateNumber() {
		try {
			Date date = new Date();
			DateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
			return formatter2.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date convertStringDatetoDate(String dateString) {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = formatter.parse(dateString);
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getTxnnumberUnique() {
		LocalDateTime dateTime = LocalDateTime.now();
		StringBuffer stringBuffer = new StringBuffer();
		String year = "" + dateTime.getYear();
		stringBuffer.append(year.substring(2));
		try {
			TimeUnit.MILLISECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stringBuffer.append(String.format("%02d", dateTime.getMonthValue()));
		stringBuffer.append(String.format("%02d", dateTime.getDayOfMonth()));
		stringBuffer.append(String.format("%02d", dateTime.getHour()));
		stringBuffer.append(String.format("%02d", dateTime.getMinute()));
		stringBuffer.append(String.format("%02d", dateTime.getSecond()));
		String nenosec = "" + dateTime.getNano();
		if (checkNullAndEmpty(nenosec) && nenosec.length() > 7)
			nenosec = nenosec.substring(0, 7);

		stringBuffer.append(nenosec);
		BigInteger txnnumber = new BigInteger(stringBuffer.toString());
		if (txnnumber.compareTo(oldTxnnumber) == 0 || txnnumber.compareTo(oldTxnnumber) == -1) {
			oldTxnnumber = oldTxnnumber.add(new BigInteger("1"));
		} else {
			oldTxnnumber = txnnumber;
		}
		return String.valueOf(oldTxnnumber);
	}

	public static String gettitleTask(EntityManager em, int tableid, Long textkey, String Languageid) {
		String sql = "select ContentValue from Rcarcontentmaster c where uuid ='" + textkey + "' and TableId='"
				+ tableid + "' and LanguageId ='" + Languageid + "' and ContentLabel='ARTitle' ";
		return em.createNativeQuery(sql).getSingleResult().toString();
	}

	public static void fillDataToResponseMap(Map<String, Object> responseMap, boolean isError, String errorTitle,
			String errorString, String errorType, Object data) {
		if (isError) {
			responseMap.put("iserror", "Y");
		} else {
			responseMap.put("iserror", "N");
		}

		if (StringUtils.hasText(errorTitle)) {
			responseMap.put("errorTitle", errorTitle);
		}

		if (StringUtils.hasText(errorString)) {
			responseMap.put("errorString", errorString);
		}

		if (StringUtils.hasText(errorType)) {
			responseMap.put("errorType", errorType);
		}

		responseMap.put("data", data);
	}

	public static void fillResponseMap(Map<String, Object> responseMap, boolean isError, String errorTitle,
			String errorString, String errorType) {
		if (isError) {
			responseMap.put("iserror", "Y");
		} else {
			responseMap.put("iserror", "N");
		}

		if (StringUtils.hasText(errorTitle)) {
			responseMap.put("errorTitle", errorTitle);
		}

		if (StringUtils.hasText(errorString)) {
			responseMap.put("errorString", errorString);
		}

		if (StringUtils.hasText(errorType)) {
			responseMap.put("errorType", errorType);
		}
	}

	public static void errorfillResponseMapAPI(Map<String, Object> responMap) {
		responMap.put("Status", 0);
		responMap.put("Message", "System Error!");
	}

	public static void errorfillResponseMapAPIMessage(Map<String, Object> responMap, String message) {
		responMap.put("Status", 0);
		responMap.put("Message", message);
	}

	public static void successfillResponseMapAPIMessage(Map<String, Object> responMap, String message) {
		responMap.put("Status", 1);
		responMap.put("Message", message);
	}
}
