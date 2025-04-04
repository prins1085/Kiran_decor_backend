package com.quotepro.common.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quotepro.common.enumeration.CommonEnum;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class ServiceModel {
	public static final Logger loger = LoggerFactory.getLogger(ServiceModel.class);

	private static EntityManagerFactory emf = null;

	public static DBUtils dbUtils;

	public static HttpServletRequest request;

	@Autowired
	public ServiceModel(EntityManagerFactory emf, DBUtils dbUtils, HttpServletRequest request) {
		this.emf = emf;
		this.dbUtils = dbUtils;
		this.request = request;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map insertDataN(Map param) {
		EntityManager manager = null;
		EntityTransaction transaction = null;
		HashMap<String, Object> responMap = new HashMap<String, Object>();
		ServiceBean bean = (ServiceBean) param.get("Bean");
		String s, sShiftDate;
		String checkQuery = "";
		String LT = "";
		Date ftime = Const.getCurrentTime();
		int i, j, lPulse, iPulse;
		int iCount;

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy HHmmss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMyy HHmm00");
		Map mapFormulla = Const.getListFormulla();
		Map newParam = new HashMap();

		try {
			manager = emf.createEntityManager();

			if (manager.isOpen() && manager != null) {
				transaction = manager.getTransaction();
				if (transaction != null) {
					transaction.begin();
					String M = bean.getM().split(",")[0];
					if (bean.getI1() != null) {
						String[] I1 = bean.getI1().split(",");
						String[] T1 = bean.getT1().split(",");
						String[] R1 = bean.getR1().split(",");
						if (I1.length > 1) {
							sort(I1, T1, R1);
						}

						for (i = 0; i < I1.length; i++) {
							Date ctime = sdf.parse(T1[i].replace('T', ' '));
							Date cttime = Const.getCurrentTime();
							Calendar ccalendar = new GregorianCalendar();
							ccalendar.setTime(ctime);
							Calendar ctcalendar = new GregorianCalendar();
							ctcalendar.setTime(cttime);
							Calendar ctcalendarS = new GregorianCalendar();
							ctcalendarS.setTime(cttime);
							ctcalendarS.add(Calendar.MINUTE, -30);
							Calendar ctcalendarE = new GregorianCalendar();
							ctcalendarE.setTime(cttime);
							ctcalendarE.add(Calendar.MINUTE, 30);
							if (i == 0) {
								if (ctime.compareTo(ctcalendarS.getTime()) < 0
										|| ctime.compareTo(ctcalendarE.getTime()) > 0) {
									T1[i] = sdf1.format(cttime).replace(" ", "T");
								} else {
									T1[i] = sdf.format(ctime).replace(" ", "T");
									int addP = 1;
									if (bean.getI2() != null) {
										String[] I2 = bean.getI2().split(",");
										String[] T2 = bean.getT2().split(",");
										sort(I2, T2, null);
										if (I2[0].equals("0")) {
											addP = 0;
										}
									}
									if (addP == 1) {
										for (j = 1; j <= Integer.parseInt(Const.getListCPANEL().get("SKIPM") != null
												? Const.getListCPANEL().get("SKIPM").toString()
												: "1"); j++) {

											iCount = dbUtils.getSingleIntData("SELECT COUNT(*) FROM " + M + " "
													+ " WHERE DATE_SUB(ETIME,INTERVAL SECOND(ETIME) SECOND)=DATE_SUB(STR_TO_DATE('"
													+ T1[i].replace('T', ' ') + "','%d%m%y %H%i%s'),INTERVAL " + j
													+ " MINUTE)", manager);
											if (iCount == 0) {
												s = Const.getCurrentShift(T1[i].split("T")[1], 1);
												LocalDateTime sdate = LocalDateTime.parse(T1[i].replace('T', ' '),
														DateTimeFormatter.ofPattern("ddMMyy HHmmss"));
												String ssstime = sdate
														.format(DateTimeFormatter.ofPattern("ddMMyy HHmm"));
												sShiftDate = Const.getShiftDate(s, ssstime.split(" ")[0], "ddMMyy",
														ssstime.split(" ")[1], "HHmm");
												lPulse = Integer.parseInt(I1[i]);
												if (Integer.parseInt(I1[i]) > Integer
														.parseInt(Const.getListCPANEL().get("HRPM").toString())) {
													lPulse = dbUtils.getSingleIntData("SELECT PULSE FROM " + M
															+ " WHERE  ETIME IN (SELECT max(ETIME) FROM " + M + ")",
															manager);
												}
//												if (Integer.parseInt(param.get("BRESET").toString()) > Integer
//														.parseInt(Const.getListCPANEL().get("BRTIME") != null
//																? Const.getListCPANEL().get("BRTIME").toString()
//																: "30")) {
												iPulse = dbUtils.getSingleIntData(" SELECT IFNULL((SELECT  PULSE FROM "
														+ M + " WHERE PULSE !=0 AND SHIFT=" + s + "\n"
														+ " AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('"
														+ sShiftDate
														+ "','%Y-%m-%d') GROUP BY PULSE ORDER BY COUNT(PULSE) DESC LIMIT 1),0)",
														manager);
//												} else {
//													iPulse = Integer.parseInt(param.get("ARPM").toString());
//												}
												manager.createNativeQuery("INSERT IGNORE INTO " + M
														+ "(ETIME,PULSE,RPM,PICK,SHIFT,SHIFTDATE,FLAG,RTIME,BID) VALUES "
														+ " (DATE_SUB(STR_TO_DATE('" + T1[i].replace('T', ' ')
														+ "','%d%m%y %H%i%s'),INTERVAL " + j + " MINUTE)," + " '"
														+ lPulse + "'," + " '" + iPulse + "',"
														+ param.get("PICK").toString() + ",'" + s + "',"
														+ " STR_TO_DATE('" + sShiftDate + "','%Y-%m-%d'),'P','" + R1[i]
														+ "'," + param.get("BID") + ")").executeUpdate();

//												if (Integer.parseInt(param.get("BRESET").toString()) > Integer
//														.parseInt(Const.getListCPANEL().get("BRTIME") != null
//																? Const.getListCPANEL().get("BRTIME").toString()
//																: "30")) {
												if (!param.get("ARPM").toString().equals(String.valueOf(iPulse))) {
													System.out.println("ARPM IN = ");

													manager.createNativeQuery("UPDATE " + M + " SET RPM=" + iPulse
															+ " WHERE SHIFT=" + s + "\n"
															+ " AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('"
															+ sShiftDate + "','%Y-%m-%d')").executeUpdate();
													manager.createNativeQuery("UPDATE MACHINE SET RPM=" + iPulse
															+ " WHERE MCODE='" + M + "'").executeUpdate();

													newParam.put("ARPM", iPulse);
												}
												newParam.put("FRPM", iPulse);
											} else {
												break;
											}
										}
									}
								} // P Insert Else End
							} else {
								if (ccalendar.get(Calendar.MONTH) == ctcalendar.get(Calendar.MONTH)
										&& ccalendar.get(Calendar.DAY_OF_MONTH) == ctcalendar.get(Calendar.DAY_OF_MONTH)
										&& ccalendar.get(Calendar.YEAR) != ctcalendar.get(Calendar.YEAR)) {
									ccalendar.set(Calendar.YEAR, ctcalendar.get(Calendar.YEAR));
									ctime = ccalendar.getTime();
								}
								T1[i] = sdf.format(ctime).replace(" ", "T");
							}
							iCount = dbUtils.getSingleIntData("SELECT COUNT(*) FROM " + M + " "
									+ " WHERE ETIME=STR_TO_DATE('" + T1[i].replace('T', ' ') + "','%d%m%y %H%i%s')",
									manager);

							if (iCount == 0) {
								s = Const.getCurrentShift(T1[i].split("T")[1]);
								sShiftDate = Const.getShiftDate(s, T1[i].split("T")[0], "ddMMyy", T1[i].split("T")[1],
										"HHmmss");

								lPulse = Integer.parseInt(I1[i]);
								if (Integer.parseInt(I1[i]) > Integer
										.parseInt(Const.getListCPANEL().get("HRPM").toString())) {
									lPulse = dbUtils.getSingleIntData("SELECT PULSE FROM " + M
											+ " WHERE  ETIME IN (SELECT max(ETIME) FROM " + M + ")", manager);
								}

								if (Integer.parseInt(param.get("BRESET").toString()) > Integer
										.parseInt(Const.getListCPANEL().get("BRTIME") != null
												? Const.getListCPANEL().get("BRTIME").toString()
												: "30")) {
									iPulse = dbUtils.getSingleIntData(" SELECT IFNULL((SELECT  PULSE FROM " + M
											+ " WHERE PULSE !=0 AND SHIFT=" + s + "\n"
											+ " AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('" + sShiftDate
											+ "','%Y-%m-%d') GROUP BY PULSE ORDER BY COUNT(PULSE) DESC LIMIT 1),0)",
											manager);
								} else {
									iPulse = Integer.parseInt(param.get("ARPM").toString());
								}

								manager.createNativeQuery("INSERT IGNORE INTO " + M
										+ "(ETIME,PULSE,RPM,PICK,SHIFT,SHIFTDATE,FLAG,RTIME,BID) VALUES "
										+ " (STR_TO_DATE('" + T1[i].replace('T', ' ') + "','%d%m%y %H%i%s')," + " '"
										+ lPulse + "'," + " '" + iPulse + "'," + param.get("PICK").toString() + ",'" + s
										+ "'," + " STR_TO_DATE('" + sShiftDate + "','%Y-%m-%d'),'M','" + R1[i] + "',"
										+ param.get("BID") + ")").executeUpdate();

								if (!param.get("ARPM").toString().equals(String.valueOf(iPulse))) {
									manager.createNativeQuery("UPDATE " + M + " SET RPM=" + iPulse + " WHERE SHIFT=" + s
											+ "\n" + " AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('" + sShiftDate
											+ "','%Y-%m-%d')").executeUpdate();
									manager.createNativeQuery(
											"UPDATE MACHINE SET RPM=" + iPulse + " WHERE MCODE='" + M + "'")
											.executeUpdate();

									newParam.put("ARPM", iPulse);
								}
								newParam.put("FRPM", iPulse);
								String qShortage = "";
								if (Const.getListCPANEL().get("QTYSHORTAGE") != null
										&& Const.getListCPANEL().get("QTYSHORTAGE").toString().equals("1")) {
									qShortage = "SUM(IF(PULSE>0,IF(IFNULL(RTIME,0)>60,60,IFNULL(RTIME,0)),0)) - ROUND((SUM(IF(PULSE>0,IF(IFNULL(RTIME,0)>60,60,IFNULL(RTIME,0)),0)) * "
											+ param.get("QSHORTAGE").toString() + " /100 ),0) ";
								} else {
									qShortage = "SUM(IF(PULSE>0,IF(IFNULL(RTIME,0)>60,60,IFNULL(RTIME,0)),0))";
								}
								String sQuery = "SELECT ROUND((" + qShortage
										+ ")/60/COUNT(*)*100,2) EFFI,SUM(PULSE) ROUNDS FROM " + M + " WHERE SHIFT=" + s
										+ "\n"
										+ "                        AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('"
										+ sShiftDate + "','%Y-%m-%d')";
								if (Const.getListCPANEL().get("OFFEFF") != null
										&& Const.getListCPANEL().get("OFFEFF").toString().equals("1")) {

									sQuery = "SELECT ROUND((" + qShortage + ")/60/" + Const.getCurrentShiftTime()
											+ "*100,2) EFFI,SUM(PULSE) ROUNDS FROM " + M + " WHERE SHIFT=" + s + "\n"
											+ "                        AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('"
											+ sShiftDate + "','%Y-%m-%d')";
								}

								List mEffi = dbUtils.getListKeyValuePairAndListAsync(sQuery, manager, true);

								// if (Common.checkListNullAndEmpty(mEffi)) {
								if (mEffi.size() > 0) {
									Map hmEE = (HashMap) mEffi.get(0);
									newParam.put("EFFI",
											hmEE.get("EFFI") != null ? hmEE.get("EFFI").toString() : "0.00");
									newParam.put("ROUNDS",
											hmEE.get("ROUNDS") != null ? hmEE.get("ROUNDS").toString() : "0.00");
								} else {
									newParam.put("EFFI", "0.00");
									newParam.put("ROUNDS", "0");
								}
								sQuery = "SELECT COUNT(*) FROM BREAKDOWN WHERE MCODE='" + M + "' AND SHIFT=" + s + "\n"
										+ "                        AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('"
										+ sShiftDate + "','%Y-%m-%d')";
								String BREAK = dbUtils.getSingleStringData(sQuery, manager);
								newParam.put("BREAK", BREAK);
								newParam.put("CSHIFT", s);
							} // M Insert If End

							if (!param.get("TAKA_PROD").toString().equals("-1")) {
								if (Const.SOFTTYPE.equals("CR")) {
									newParam.put("TAKA_PROD", String.format("%.2f", Double
											.parseDouble(param.get("TAKA_PROD").toString())
											+ (Integer.parseInt(R1[i]) > 0 && Integer.parseInt(I1[i]) > 0
													? (Double.parseDouble(I1[i])
															* Double.parseDouble(param.get("KGS").toString())
															/ Double.parseDouble(param.get("COUNTER").toString()))
													: 0)));
								} else {
									newParam.put("TAKA_PROD", String.format("%.2f", Double
											.parseDouble(param.get("TAKA_PROD").toString())
											+ (Integer.parseInt(R1[i]) > 0 && Integer.parseInt(I1[i]) > 0
													? (Double.parseDouble(I1[i])
															/ Double.parseDouble(param.get("PICK").toString()) / 39.37)
													: 0)));
								}

//								List TAANDFLAG = dbUtils.getarraylist(dbUtils.getListKeyValuePairAndListAsync(
//										"SELECT IFNULL(DBTAKAALERT,0) DBTAKAALERT,IFNULL(TAKAALERTFLAG,0) TAKAALERTFLAG   FROM BEAMMASTER B WHERE BID = "
//												+ param.get("BID"),
//										manager, true));
//								String DBTAKAALERT = "0.00";
//								String TAKAALERTFLAG = "0";
//
//								String[] tafARRAY = (String[]) TAANDFLAG.get(0);
//								TAKAALERTFLAG = tafARRAY[0];
//								DBTAKAALERT = tafARRAY[1];

//								if (Double.parseDouble(DBTAKAALERT.toString()) != 0.00
//										&& Double.parseDouble(newParam.get("TAKA_PROD").toString()) >= Double
//												.parseDouble(DBTAKAALERT.toString())
//										&& TAKAALERTFLAG.toString().equals("1")
//										&& Const.getListCPANEL().get("TAKAALERT").toString().equals("1")) {
//									newParam.put("TAKAALERT", "1");
//								} else {
//									newParam.put("TAKAALERT", "0");
//								}

								if (Double.parseDouble(param.get("DBTAKAALERT").toString()) != 0.00
										&& Double.parseDouble(newParam.get("TAKA_PROD").toString()) >= Double
												.parseDouble(param.get("DBTAKAALERT").toString())
										&& param.get("TAKAALERTFLAG").toString().equals("1")
										&& Const.getListCPANEL().get("TAKAALERT").toString().equals("1")) {
									newParam.put("TAKAALERT", "1");
								} else {
									newParam.put("TAKAALERT", "0");
								}

							} else {
								newParam.put("TAKAALERT", "0");
							}
						} // End For Loop
						if (Const.getListCPANEL().get("ISRLOG") != null
								&& Const.getListCPANEL().get("ISRLOG").toString().equals("1")) {
							long diffInMillies = Math.abs(Const.getCurrentTime().getTime() - ftime.getTime());
							LT += " L2:" + String.valueOf(diffInMillies);
							ftime = Const.getCurrentTime();
							if (bean.getM()
									.equals("M" + (Const.getListCPANEL().get("ISRLOGM") != null
											? Const.getListCPANEL().get("ISRLOGM").toString()
											: "001"))) {
								loger.error(bean.getM() + " : " + LT);
							}
						}

					}
					int iInsertStop = 0;
					if (bean.getI2() != null) {
						String[] I2 = bean.getI2().split(",");
						String[] T2 = bean.getT2().split(",");
						if (I2.length > 0) {
							sort(I2, T2, null);
						}

						for (i = 0; i < I2.length; i++) {
							Date ctime = sdf.parse(T2[i].replace('T', ' '));
							Date cttime = Const.getCurrentTime();
							Calendar ccalendar = new GregorianCalendar();
							ccalendar.setTime(ctime);
							Calendar ctcalendar = new GregorianCalendar();
							ctcalendar.setTime(cttime);
							Calendar ctcalendarS = new GregorianCalendar();
							ctcalendarS.setTime(cttime);
							ctcalendarS.add(Calendar.MINUTE, -30);
							Calendar ctcalendarE = new GregorianCalendar();
							ctcalendarE.setTime(cttime);
							ctcalendarE.add(Calendar.MINUTE, 30);
							if (ccalendar.get(Calendar.MONTH) == ctcalendar.get(Calendar.MONTH)
									&& ccalendar.get(Calendar.DAY_OF_MONTH) == ctcalendar.get(Calendar.DAY_OF_MONTH)
									&& ccalendar.get(Calendar.YEAR) != ctcalendar.get(Calendar.YEAR)) {
								ccalendar.set(Calendar.YEAR, ctcalendar.get(Calendar.YEAR));
								ctime = ccalendar.getTime();
							}
							if (ctime.compareTo(ctcalendarS.getTime()) < 0
									|| ctime.compareTo(ctcalendarE.getTime()) > 0) {
								T2[i] = sdf1.format(cttime).replace(" ", "T");
							} else {
								T2[i] = sdf.format(ctime).replace(" ", "T");
							}
							s = Const.getCurrentShift(T2[i].split("T")[1]);
							sShiftDate = Const.getShiftDate(s, T2[i].split("T")[0], "ddMMyy", T2[i].split("T")[1],
									"HHmmss");
							if (Integer.parseInt(I2[i]) == 0) {
								iCount = dbUtils.getSingleIntData("SELECT COUNT(*) fROM BREAKDOWN WHERE MCODE='" + M
										+ "' " + " AND ETIME=STR_TO_DATE('" + T2[i].replace('T', ' ')
										+ "','%d%m%y %H%i%s') " + " AND STYPE='I2' ", manager);
								if (iCount == 0) {
									String stime = dbUtils.getSingleStringData(
											"SELECT IFNULL((SELECT IF(ETIME IS NULL,SSID,0) FROM s" + M
													+ " WHERE MCODE='" + M + "' ORDER BY SSID DESC LIMIT 1),0)",
											manager);

									if (!stime.equals("0")) {
										iInsertStop = 1;
										manager.createNativeQuery("UPDATE s" + M + " SET ETIME =STR_TO_DATE('"
												+ T2[i].replace('T', ' ') + "','%d%m%y %H%i%s') WHERE MCODE ='" + M
												+ "' AND SSID='" + stime + "'").executeUpdate();
									}
									if (iInsertStop == 1) {
										if (bean.getI3() == null && bean.getI4() == null && bean.getI5() == null) {
											manager.createNativeQuery(
													"INSERT IGNORE INTO BREAKDOWN(MCODE,ETIME,STYPE,SHIFT,SHIFTDATE,BID) VALUES ('"
															+ M + "',STR_TO_DATE('" + T2[i].replace('T', ' ')
															+ "','%d%m%y %H%i%s'),'I2','" + s + "',STR_TO_DATE('"
															+ sShiftDate + "','%Y-%m-%d')," + param.get("BID") + ")")
													.executeUpdate();
										}
									}
								}
							} else {
								iCount = dbUtils.getSingleIntData("SELECT COUNT(*) fROM s" + M + " WHERE MCODE='" + M
										+ "' " + " AND STIME=STR_TO_DATE('" + T2[i].replace('T', ' ')
										+ "','%d%m%y %H%i%s') ", manager);
								if (iCount == 0) {

									String etime = dbUtils.getSingleStringData(
											"SELECT IFNULL((SELECT IF(ETIME IS NULL,SSID,0) FROM s" + M
													+ " WHERE MCODE='" + M + "' ORDER BY SSID DESC LIMIT 1),0)",
											manager);
									if (etime.equals("0")) {
										String sESTime = dbUtils.getSingleStringData(
												"SELECT IFNULL((SELECT IFNULL(ETIME,0) fROM s" + M + " WHERE MCODE='"
														+ M + "' \n" + " ORDER BY SSID DESC LIMIT 1),0)",
												manager);

										manager.createNativeQuery("INSERT IGNORE INTO s" + M
												+ "(MCODE,STIME,STATUS,ESTIME) VALUES ('" + M + "',STR_TO_DATE('"
												+ T2[i].toString().replace('T', ' ') + "','%d%m%y %H%i%s'),'" + I2[i]
												+ "'," + (sESTime.equals("0") ? "NULL" : "'" + sESTime + "'") + ")")
												.executeUpdate();
									}
								}
							}
							String sQuery = "SELECT COUNT(*) FROM BREAKDOWN WHERE MCODE='" + M + "' AND SHIFT=" + s
									+ "\n"
									+ "                        AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('"
									+ sShiftDate + "','%Y-%m-%d')";
							String sEffi = dbUtils.getSingleStringData(sQuery, manager);
							newParam.put("BREAK", sEffi);
						}
					}
					if (iInsertStop == 1) {
						if (bean.getI3() != null) {
							String[] I3 = bean.getI3().split(",");
							String[] T3 = bean.getT3().split(",");

							for (i = 0; i < I3.length; i++) {
								Date ctime = sdf.parse(T3[i].replace('T', ' '));
								Date cttime = Const.getCurrentTime();
								Calendar ccalendar = new GregorianCalendar();
								ccalendar.setTime(ctime);
								Calendar ctcalendar = new GregorianCalendar();
								ctcalendar.setTime(cttime);
								Calendar ctcalendarS = new GregorianCalendar();
								ctcalendarS.setTime(cttime);
								ctcalendarS.add(Calendar.MINUTE, -30);
								Calendar ctcalendarE = new GregorianCalendar();
								ctcalendarE.setTime(cttime);
								ctcalendarE.add(Calendar.MINUTE, 30);
								if (ccalendar.get(Calendar.MONTH) == ctcalendar.get(Calendar.MONTH)
										&& ccalendar.get(Calendar.DAY_OF_MONTH) == ctcalendar.get(Calendar.DAY_OF_MONTH)
										&& ccalendar.get(Calendar.YEAR) != ctcalendar.get(Calendar.YEAR)) {
									ccalendar.set(Calendar.YEAR, ctcalendar.get(Calendar.YEAR));
									ctime = ccalendar.getTime();
								}
								if (ctime.compareTo(ctcalendarS.getTime()) < 0
										|| ctime.compareTo(ctcalendarE.getTime()) > 0) {
									T3[i] = sdf1.format(cttime).replace(" ", "T");
								} else {
									T3[i] = sdf.format(ctime).replace(" ", "T");
								}
								s = Const.getCurrentShift(T3[i].split("T")[1]);
								sShiftDate = Const.getShiftDate(s, T3[i].split("T")[0], "ddMMyy", T3[i].split("T")[1],
										"HHmmss");

								manager.createNativeQuery(
										"INSERT IGNORE INTO BREAKDOWN(MCODE,ETIME,STYPE,SHIFT,SHIFTDATE,BID) VALUES ('"
												+ M + "',STR_TO_DATE('" + T3[i].replace('T', ' ')
												+ "','%d%m%y %H%i%s'),'I3','" + s + "',STR_TO_DATE('" + sShiftDate
												+ "','%Y-%m-%d')," + param.get("BID") + ")")
										.executeUpdate();
								String sQuery = "SELECT COUNT(*) FROM BREAKDOWN WHERE MCODE='" + M + "' AND SHIFT=" + s
										+ "\n"
										+ "                        AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('"
										+ sShiftDate + "','%Y-%m-%d')";
								String sEffi = dbUtils.getSingleStringData(sQuery, manager);
								newParam.put("BREAK", sEffi);
//		                        }
							}
						}
						if (bean.getI4() != null) {
							String[] I4 = bean.getI4().split(",");
							String[] T4 = bean.getT4().split(",");
							for (i = 0; i < I4.length; i++) {
								Date ctime = sdf.parse(T4[i].replace('T', ' '));
								Date cttime = Const.getCurrentTime();
								Calendar ccalendar = new GregorianCalendar();
								ccalendar.setTime(ctime);
								Calendar ctcalendar = new GregorianCalendar();
								ctcalendar.setTime(cttime);
								Calendar ctcalendarS = new GregorianCalendar();
								ctcalendarS.setTime(cttime);
								ctcalendarS.add(Calendar.MINUTE, -30);
								Calendar ctcalendarE = new GregorianCalendar();
								ctcalendarE.setTime(cttime);
								ctcalendarE.add(Calendar.MINUTE, 30);
								if (ccalendar.get(Calendar.MONTH) == ctcalendar.get(Calendar.MONTH)
										&& ccalendar.get(Calendar.DAY_OF_MONTH) == ctcalendar.get(Calendar.DAY_OF_MONTH)
										&& ccalendar.get(Calendar.YEAR) != ctcalendar.get(Calendar.YEAR)) {
									ccalendar.set(Calendar.YEAR, ctcalendar.get(Calendar.YEAR));
									ctime = ccalendar.getTime();
								}
								if (ctime.compareTo(ctcalendarS.getTime()) < 0
										|| ctime.compareTo(ctcalendarE.getTime()) > 0) {
									T4[i] = sdf1.format(cttime).replace(" ", "T");
								} else {
									T4[i] = sdf.format(ctime).replace(" ", "T");
								}
								iCount = dbUtils.getSingleIntData("SELECT COUNT(*) fROM BREAKDOWN WHERE MCODE='" + M
										+ "' " + " AND ETIME=STR_TO_DATE('" + T4[i].replace('T', ' ')
										+ "','%d%m%y %H%i%s') " + " AND STYPE='I4'", manager);
								if (iCount == 0) {
									s = Const.getCurrentShift(T4[i].split("T")[1]);

									sShiftDate = dbUtils
											.getSingleStringData("SELECT IF(1!=" + s + ",IF(TIME_FORMAT(TIME('"
													+ T4[i].split("T")[1] + "'), '%H%i') < TIME_FORMAT(TIME('"
													+ ((HashMap) Const.getListShift().get(0)).get("STARTTIME")
															.toString()
													+ "'), '%H%i'),ADDDATE(STR_TO_DATE('" + T4[i].split("T")[0]
													+ "','%d%m%y'), - 1),STR_TO_DATE('" + T4[i].split("T")[0]
													+ "','%d%m%y')),STR_TO_DATE('" + T4[i].split("T")[0]
													+ "','%d%m%y'))", manager);
									manager.createNativeQuery(
											"INSERT IGNORE INTO BREAKDOWN(MCODE,ETIME,STYPE,SHIFT,SHIFTDATE,BID) VALUES ('"
													+ M + "',STR_TO_DATE('" + T4[i].replace('T', ' ')
													+ "','%d%m%y %H%i%s'),'I4','" + s + "',STR_TO_DATE('" + sShiftDate
													+ "','%Y-%m-%d')," + param.get("BID") + ")")
											.executeUpdate();
									String sQuery = "SELECT COUNT(*) FROM BREAKDOWN WHERE MCODE='" + M + "' AND SHIFT="
											+ s + "\n"
											+ "                        AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('"
											+ sShiftDate + "','%Y-%m-%d')";
									String sEffi = dbUtils.getSingleStringData(sQuery, manager);
									newParam.put("BREAK", sEffi);
								}
							}
						}
						if (bean.getI5() != null) {
							String[] I5 = bean.getI5().split(",");
							String[] T5 = bean.getT5().split(",");
							for (i = 0; i < I5.length; i++) {
								Date ctime = sdf.parse(T5[i].replace('T', ' '));
								Date cttime = Const.getCurrentTime();
								Calendar ccalendar = new GregorianCalendar();
								ccalendar.setTime(ctime);
								Calendar ctcalendar = new GregorianCalendar();
								ctcalendar.setTime(cttime);
								Calendar ctcalendarS = new GregorianCalendar();
								ctcalendarS.setTime(cttime);
								ctcalendarS.add(Calendar.MINUTE, -30);
								Calendar ctcalendarE = new GregorianCalendar();
								ctcalendarE.setTime(cttime);
								ctcalendarE.add(Calendar.MINUTE, 30);
								if (ccalendar.get(Calendar.MONTH) == ctcalendar.get(Calendar.MONTH)
										&& ccalendar.get(Calendar.DAY_OF_MONTH) == ctcalendar.get(Calendar.DAY_OF_MONTH)
										&& ccalendar.get(Calendar.YEAR) != ctcalendar.get(Calendar.YEAR)) {
									ccalendar.set(Calendar.YEAR, ctcalendar.get(Calendar.YEAR));
									ctime = ccalendar.getTime();
								}
								if (ctime.compareTo(ctcalendarS.getTime()) < 0
										|| ctime.compareTo(ctcalendarE.getTime()) > 0) {
									T5[i] = sdf1.format(cttime).replace(" ", "T");
								} else {
									T5[i] = sdf.format(ctime).replace(" ", "T");
								}
								iCount = dbUtils.getSingleIntData("SELECT COUNT(*) fROM BREAKDOWN WHERE MCODE='" + M
										+ "' " + " AND ETIME=STR_TO_DATE('" + T5[i].replace('T', ' ')
										+ "','%d%m%y %H%i%s') " + " AND STYPE='I5'", manager);
								if (iCount == 0) {
									s = Const.getCurrentShift(T5[i].split("T")[1]);
									sShiftDate = dbUtils
											.getSingleStringData("SELECT IF(1!=" + s + ",IF(TIME_FORMAT(TIME('"
													+ T5[i].split("T")[1] + "'), '%H%i') < TIME_FORMAT(TIME('"
													+ ((HashMap) Const.getListShift().get(0)).get("STARTTIME")
															.toString()
													+ "'), '%H%i'),ADDDATE(STR_TO_DATE('" + T5[i].split("T")[0]
													+ "','%d%m%y'), - 1),STR_TO_DATE('" + T5[i].split("T")[0]
													+ "','%d%m%y')),STR_TO_DATE('" + T5[i].split("T")[0]
													+ "','%d%m%y'))", manager);

									manager.createNativeQuery(
											"INSERT INTO BREAKDOWN(MCODE,ETIME,STYPE,SHIFT,SHIFTDATE,BID) VALUES ('" + M
													+ "',STR_TO_DATE('" + T5[i].replace('T', ' ')
													+ "','%d%m%y %H%i%s'),'I5','" + s + "',STR_TO_DATE('" + sShiftDate
													+ "','%Y-%m-%d')," + param.get("BID") + ")")
											.executeUpdate();
								}
							}
						}
					}
					try {
						if (Integer.parseInt(param.get("BRESET").toString()) > Integer
								.parseInt(Const.getListCPANEL().get("BRTIME") != null
										? Const.getListCPANEL().get("BRTIME").toString()
										: "30")) {
							if (param.get("BID") != null) {

								String sQuery = "SELECT GROUP_CONCAT(DISTINCT B.BID) BID,GROUP_CONCAT(DISTINCT M.MCODE) MCODE,SUM(B.MTRS) MTRS,SUM(B.ALERTMTRS) ALERTMTRS,IFNULL(B.MPANNO,1) MPANNO FROM BEAMMASTER B INNER JOIN MACHINE M ON M.MID=B.MID WHERE B.BNO=(SELECT IFNULL((SELECT BNO FROM BEAMMASTER WHERE BID="
										+ param.get("BID") + "),0))";

								List lstBeam = dbUtils.getListKeyValuePairAndListAsync(sQuery, manager, true);
								if (Common.checkListNullAndEmpty(lstBeam)) {
									Map hmBeam = (HashMap) lstBeam.get(0);
									if (hmBeam.get("BID") != null) {
										String[] mcodes = hmBeam.get("MCODE").toString().split(",");
										sQuery = "SELECT IFNULL(ROUND(SUM(IFNULL(PRODUCTIONMTR,0))/"
												+ hmBeam.get("MPANNO").toString() + ",2),0) PRODUCTIONMTR FROM\n"
												+ "(SELECT SUM(IFNULL(PRODUCTIONMTR,0))PRODUCTIONMTR FROM MACHINESHIFTDETAIL WHERE BID IN ("
												+ hmBeam.get("BID").toString() + ")\n" + "\n";

										for (int iM = 0; iM < mcodes.length; iM++) {
											sQuery = sQuery + " UNION ALL ";
											sQuery = sQuery + " SELECT ROUND(SUM(IF(IFNULL(RTIME,0)>0,"
													+ ((HashMap) mapFormulla.get(param.get("MTYPE").toString()))
															.get("F1").toString()
													+ ",0)),2) AS PRODUCTIONMTR FROM " + mcodes[iM] + " M ";
//											if (Const.SOFTTYPE.equals("CR") || Const.SOFTTYPE.equals("RP")) {
											sQuery = sQuery + " INNER JOIN (SELECT * FROM BEAMMASTER WHERE BID IN ("
													+ hmBeam.get("BID").toString() + ")) B ON B.BID=M.BID";
//											}
											sQuery = sQuery + " WHERE M.BID IN (" + hmBeam.get("BID").toString() + ")";
										}
										sQuery = sQuery + ") A";
										List lstBeamData = dbUtils.getListKeyValuePairAndListAsync(sQuery, manager,
												true);
										double PTR = 0;
										if (Common.checkListNullAndEmpty(lstBeamData)) {
											Map hmBeamData = (HashMap) lstBeamData.get(0);
											PTR = Double.parseDouble(hmBeamData.get("PRODUCTIONMTR").toString());
										}
										int pMTR = (int) (Double.parseDouble(hmBeam.get("MTRS").toString()) - PTR);
										newParam.put("BMTR", pMTR);
										if (Const.SOFTTYPE.equals("CR")
												&& Double.parseDouble(hmBeam.get("MTRS").toString()) <= 0.00) {
											newParam.put("BALERT", "0");
										} else {
											if ((Double.parseDouble(hmBeam.get("MTRS").toString()) - PTR) < Double
													.parseDouble(hmBeam.get("ALERTMTRS").toString())) {
												newParam.put("BALERT", "1");
											} else {
												newParam.put("BALERT", "0");
											}
										}
									} else {
										newParam.put("BALERT", "0");
										newParam.put("BMTR", 0);
									}
								} else {
									newParam.put("BALERT", "0");
									newParam.put("BMTR", 0);
								}
							} else {
								newParam.put("BALERT", "0");
								newParam.put("BMTR", 0);
							}
							newParam.put("BRESET", "0");
						} else {
							newParam.put("BRESET", Integer.parseInt(param.get("BRESET").toString()) + 1);
						}
					} catch (Throwable t) {
						loger.error("beam : " + t);
					}

					try {
						if (bean.getS() != null) {
							manager.createNativeQuery("INSERT INTO RESTART(MCODE,ETIME) VALUES ('" + M + "',"
									+ (bean.getT2() != null
											? "STR_TO_DATE('" + bean.getT2().split(",")[0].replace('T', ' ')
													+ "','%d%m%y %H%i%s')"
											: "CURRENT_TIMESTAMP")
									+ ")").executeUpdate();
						}
					} catch (Throwable t) {
						t.printStackTrace();
						loger.error("RESTART : " + t);
					}
					transaction.commit();
				} else {
					loger.error(CommonEnum.UNABLE_TO_ACQUIRE_TRANSACTION.getValue());
				}
			}

		} catch (PersistenceException ex) {
			Common.throwException(ex, null, transaction, responMap, null, emf, request);
			loger.error("EFFI Query 1 Catch : " + checkQuery);
		} catch (Exception e) {
			Common.throwException(null, e, transaction, responMap, null, emf, request);
			loger.error("EFFI Query 2 Catch : " + checkQuery);
		} catch (Throwable ex) {
			Common.throwThrowableException(ex, transaction, responMap, null, emf, request);
			ex.printStackTrace();
			loger.error("EFFI Query 3 Catch : " + checkQuery);
			bean = null;
			s = null;

		} finally {
			if (manager.isOpen() && manager != null) {
				manager.clear();
				manager.close();
			}
		}
		bean = null;
		s = null;
		return newParam;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map insertDataC(Map param) {
		EntityManager manager = null;
		EntityTransaction transaction = null;
		Map newParam = new HashMap();
		HashMap<String, Object> responMap = new HashMap<String, Object>();
		ArrayList bean = (ArrayList) param.get("Bean");
		String s, sShiftDate;
		int i, j, iCount, lPulse, iPulse;
		Map mapFormulla = Const.getListFormulla();
		DBUtils dbUtils = new DBUtils();

		try {
			manager = emf.createEntityManager();
			if (manager.isOpen() && manager != null) {
				transaction = manager.getTransaction();
				if (transaction != null) {
					transaction.begin();
					String M = ((Map) bean.get(0)).get("DeviceId").toString();
					for (i = 0; i < bean.size(); i++) {
						Map hmNN = ((Map) bean.get(i));
						String dTime = hmNN.get("DateTime").toString();
						String drpm = hmNN.get("RPM").toString();
						int tCount = Integer.parseInt(hmNN.get("tCount").toString());
						// int tStop = Integer.parseInt(hmNN.get("tStop").toString());

						iCount = dbUtils.getSingleIntData("SELECT COUNT(*) FROM " + M + " "
								+ " WHERE ETIME=STR_TO_DATE('" + dTime + "','%d-%m-%Y %H:%i:%s')", manager);
						s = Const.getCurrentShift(dTime.split(" ")[1]);
						sShiftDate = Const.getShiftDate(s, dTime.split(" ")[0], "dd-MM-yyyy", dTime.split(" ")[1],
								"HH:mm:ss");
						if (iCount == 0) {
//			                        String sShiftDate = objSQL.getString("SELECT IF(1!=" + s + ",IF(TIME_FORMAT(TIME('" + T1[i].split("T")[1] + "'), '%H%i') < TIME_FORMAT(TIME('" + ((HashMap) Const.getListShift().get(0)).get("STARTTIME").toString() + "'), '%H%i'),ADDDATE(STR_TO_DATE('" + T1[i].split("T")[0] + "','%d%m%y'), - 1),STR_TO_DATE('" + T1[i].split("T")[0] + "','%d%m%y')),STR_TO_DATE('" + T1[i].split("T")[0] + "','%d%m%y'))");
							lPulse = Integer.parseInt(drpm);
							if (Integer.parseInt(drpm) > Integer
									.parseInt(Const.getListCPANEL().get("HRPM").toString())) {
								lPulse = dbUtils.getSingleIntData("SELECT IFNULL((SELECT PULSE FROM " + M
										+ " WHERE  ETIME IN (SELECT max(ETIME) FROM " + M + ")),0)", manager);
							}

							// with beamid
							iPulse = dbUtils.getSingleIntData(
									" SELECT IFNULL((SELECT  PULSE FROM " + M + " WHERE PULSE !=0 AND SHIFT=" + s + "\n"
											+ " AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('" + sShiftDate
											+ "','%Y-%m-%d') GROUP BY PULSE ORDER BY COUNT(PULSE) DESC LIMIT 1),0)",
									manager);

							manager.createNativeQuery("INSERT IGNORE INTO " + M
									+ "(ETIME,PULSE,RPM,PICK,SHIFT,SHIFTDATE,FLAG,RTIME,BID) VALUES "
									+ " (STR_TO_DATE('" + dTime + "','%d-%m-%Y %H:%i:%s')," + " '" + lPulse + "',"
									+ " '" + iPulse + "'," + param.get("PICK").toString() + ",'" + s + "',"
									+ " STR_TO_DATE('" + sShiftDate + "','%Y-%m-%d'),'M','" + tCount + "',"
									+ param.get("BID") + ")").executeUpdate();

							// SET SHIFT WISE RPM FIELD
							// NEW MAX OCCURS
							// OLD MAX PULSE
//			                                int iPulse = objSQL.getInteger("SELECT MAX(PULSE) FROM " + M + " WHERE SHIFT=" + s + "\n"
//			                                + " AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('" + sShiftDate + "','%Y-%m-%d')", null);
							if (!param.get("ARPM").toString().equals(String.valueOf(iPulse))) {
								manager.createNativeQuery("UPDATE " + M + " SET RPM=" + iPulse + " WHERE SHIFT=" + s
										+ "\n" + " AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('" + sShiftDate
										+ "','%Y-%m-%d')").executeUpdate();
								manager.createNativeQuery(
										"UPDATE MACHINE SET RPM=" + iPulse + " WHERE MCODE='" + M + "'")
										.executeUpdate();
								newParam.put("ARPM", iPulse);
							}
							newParam.put("FRPM", iPulse);
							String qShortage = "";
							if (Const.getListCPANEL().get("QTYSHORTAGE") != null
									&& Const.getListCPANEL().get("QTYSHORTAGE").toString().equals("1")) {
								qShortage = "SUM(IF(PULSE>0,IF(IFNULL(RTIME,0)>60,60,IFNULL(RTIME,0)),0)) - ROUND((SUM(IF(PULSE>0,IF(IFNULL(RTIME,0)>60,60,IFNULL(RTIME,0)),0)) * "
										+ param.get("QSHORTAGE").toString() + " /100 ),0) ";
							} else {
								qShortage = "SUM(IF(PULSE>0,IF(IFNULL(RTIME,0)>60,60,IFNULL(RTIME,0)),0))";
							}
							String sQuery = "SELECT ROUND((" + qShortage
									+ ")/60/COUNT(*)*100,2) EFFI,SUM(PULSE) ROUNDS FROM " + M + " WHERE SHIFT=" + s
									+ "\n"
									+ "                        AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('"
									+ sShiftDate + "','%Y-%m-%d')";
							if (Const.getListCPANEL().get("OFFEFF") != null
									&& Const.getListCPANEL().get("OFFEFF").toString().equals("1")) {

								sQuery = "SELECT ROUND((" + qShortage + ")/60/" + Const.getCurrentShiftTime()
										+ "*100,2) EFFI,SUM(PULSE) ROUNDS FROM " + M + " WHERE SHIFT=" + s + "\n"
										+ "                        AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('"
										+ sShiftDate + "','%Y-%m-%d')";
							}

							List mEffi = dbUtils.getListKeyValuePairAndListAsync(sQuery, manager, true);
							// if (Common.checkListNullAndEmpty(mEffi)) {
							if (mEffi.size() > 0) {
								Map hmEE = (HashMap) mEffi.get(0);
								newParam.put("EFFI", hmEE.get("EFFI") != null ? hmEE.get("EFFI").toString() : "0.00");
								newParam.put("ROUNDS",
										hmEE.get("ROUNDS") != null ? hmEE.get("ROUNDS").toString() : "0.00");
							} else {
								newParam.put("EFFI", "0.00");
								newParam.put("ROUNDS", "0");
							}
							sQuery = "SELECT COUNT(*) FROM BREAKDOWN WHERE MCODE='" + M + "' AND SHIFT=" + s + "\n"
									+ "                        AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('"
									+ sShiftDate + "','%Y-%m-%d')";
							String BREAK = dbUtils.getSingleStringData(sQuery, manager);
							newParam.put("BREAK", BREAK);
							newParam.put("CSHIFT", s);

						}

						if (!param.get("TAKA_PROD").toString().equals("-1")) {
							if (Const.SOFTTYPE.equals("CR")) {
								newParam.put("TAKA_PROD",
										String.format("%.2f",
												Double.parseDouble(param.get("TAKA_PROD").toString())
														+ (tCount > 0 && Integer.parseInt(drpm) > 0
																? (Double.parseDouble(drpm)
																		* Double.parseDouble(
																				param.get("KGS").toString())
																		/ Double.parseDouble(
																				param.get("COUNTER").toString()))
																: 0)));
							} else {
								newParam.put("TAKA_PROD", String.format("%.2f", Double
										.parseDouble(param.get("TAKA_PROD").toString())
										+ (tCount > 0 && Integer.parseInt(drpm) > 0
												? (Double.parseDouble(drpm)
														/ Double.parseDouble(param.get("PICK").toString()) / 39.37)
												: 0)));
							}

						}

						int iInsertStop = 0;
						if ((tCount > 0 && param.get("LRUN").toString().equals("0"))
								|| (tCount == 0 && param.get("LRUN").toString().equals("1"))) {
							if (tCount == 0) {
								iCount = dbUtils.getSingleIntData("SELECT COUNT(*) fROM BREAKDOWN WHERE MCODE='" + M
										+ "' " + " AND ETIME=STR_TO_DATE('" + dTime + "','%d-%m-%Y %H:%i:%s') "
										+ " AND STYPE='I2' ", manager);
								if (iCount == 0) {
									// String stime = objSQL.getString("SELECT IFNULL((SELECT IF(ETIME IS
									// NULL,DATE_FORMAT(STIME,'%y-%m-%d %H:%i:%s'),0) FROM STARTSTOP WHERE MCODE='"
									// + M + "' ORDER BY STIME DESC LIMIT 1),0)");
									String stime = dbUtils.getSingleStringData(
											"SELECT IFNULL((SELECT IF(ETIME IS NULL,SSID,0) FROM s" + M
													+ " WHERE MCODE='" + M + "' ORDER BY SSID DESC LIMIT 1),0)",
											manager);

									if (!stime.equals("0")) {
										iInsertStop = 1;
//			                                objSQL.persist("UPDATE STARTSTOP SET ETIME =STR_TO_DATE('" + T2[i].replace('T', ' ') + "','%d%m%y %H%i%s') WHERE MCODE ='" + M + "' AND ETIME IS NULL AND STIME='" + stime + "'", null);
										manager.createNativeQuery("UPDATE s" + M + " SET ETIME =STR_TO_DATE('" + dTime
												+ "','%d-%m-%Y %H:%i:%s') WHERE MCODE ='" + M + "' AND SSID='" + stime
												+ "'").executeUpdate();
									}
									if (iInsertStop == 1) {
										manager.createNativeQuery(
												"INSERT IGNORE INTO BREAKDOWN(MCODE,ETIME,STYPE,SHIFT,SHIFTDATE,BID) VALUES ('"
														+ M + "',STR_TO_DATE('" + dTime
														+ "','%d-%m-%Y %H:%i:%s'),'I2','" + s + "',STR_TO_DATE('"
														+ sShiftDate + "','%Y-%m-%d')," + param.get("BID") + ")")
												.executeUpdate();
									}
								}
							} else {
								iCount = dbUtils.getSingleIntData(
										"SELECT COUNT(*) fROM s" + M + " WHERE MCODE='" + M + "' "
												+ " AND STIME=STR_TO_DATE('" + dTime + "','%d-%m-%Y %H:%i:%s') ",
										manager);
								if (iCount == 0) {

									String etime = dbUtils.getSingleStringData(
											"SELECT IFNULL((SELECT IF(ETIME IS NULL,SSID,0) FROM s" + M
													+ " WHERE MCODE='" + M + "' ORDER BY SSID DESC LIMIT 1),0)",
											manager);
									if (etime.equals("0")) {
										String sESTime = dbUtils.getSingleStringData(
												"SELECT IFNULL((SELECT IFNULL(ETIME,0) fROM s" + M + " WHERE MCODE='"
														+ M + "' \n" + " ORDER BY SSID DESC LIMIT 1),0)",
												manager);

										manager.createNativeQuery(
												"INSERT IGNORE INTO s" + M + "(MCODE,STIME,STATUS,ESTIME) VALUES ('" + M
														+ "',STR_TO_DATE('" + dTime + "','%d-%m-%Y %H:%i:%s'),'1',"
														+ (sESTime.equals("0") ? "NULL" : "'" + sESTime + "'") + ")")
												.executeUpdate();
									}
								}
							}
						}
						param.put("LRUN", hmNN.get("RPM").toString());
						String sQuery = "SELECT COUNT(*) FROM BREAKDOWN WHERE MCODE='" + M + "' AND SHIFT=" + s + "\n"
								+ "                        AND DATE_FORMAT(SHIFTDATE,'%Y-%m-%d')=STR_TO_DATE('"
								+ sShiftDate + "','%Y-%m-%d')";
						String sEffi = dbUtils.getSingleStringData(sQuery, manager);
						newParam.put("BREAK", sEffi);
						param.put("LRUN", tCount > 0 ? "1" : "0");

					}
					newParam.put("BREAK", param.get("LRUN").toString());
					try {
						if (Integer.parseInt(param.get("BRESET").toString()) > Integer
								.parseInt(Const.getListCPANEL().get("BRTIME") != null
										? Const.getListCPANEL().get("BRTIME").toString()
										: "30")) {
							if (param.get("BID") != null) {
//			                        String sQuery = "SELECT GROUP_CONCAT(BID) BID,SUM(MTRS) MTRS,SUM(ALERTMTRS) ALERTMTRS FROM BEAMMASTER WHERE BNO=(SELECT IFNULL((SELECT BNO FROM BEAMMASTER WHERE BID=" + param.get("BID") + "),0))";
								String sQuery = "SELECT GROUP_CONCAT(DISTINCT B.BID) BID,GROUP_CONCAT(DISTINCT M.MCODE) MCODE,SUM(B.MTRS) MTRS,SUM(B.ALERTMTRS) ALERTMTRS,IFNULL(B.MPANNO,1) MPANNO FROM BEAMMASTER B INNER JOIN MACHINE M ON M.MID=B.MID WHERE B.BNO=(SELECT IFNULL((SELECT BNO FROM BEAMMASTER WHERE BID="
										+ param.get("BID") + "),0))";

								List lstBeam = dbUtils.getListKeyValuePairAndListAsync(sQuery, manager, true);
								if (Common.checkListNullAndEmpty(lstBeam)) {
									Map hmBeam = (HashMap) lstBeam.get(0);
									if (hmBeam.get("BID") != null) {
										String[] mcodes = hmBeam.get("MCODE").toString().split(",");

										sQuery = "SELECT IFNULL(ROUND(SUM(IFNULL(PRODUCTIONMTR,0))/"
												+ hmBeam.get("MPANNO").toString() + ",2),0) PRODUCTIONMTR FROM\n"
												+ "(SELECT SUM(IFNULL(PRODUCTIONMTR,0))PRODUCTIONMTR FROM MACHINESHIFTDETAIL WHERE BID IN ("
												+ hmBeam.get("BID").toString() + ") ";
										for (int iM = 0; iM < mcodes.length; iM++) {
											sQuery = sQuery + " UNION ALL ";
											sQuery = sQuery + " SELECT ROUND(SUM(IF(IFNULL(RTIME,0)>0,"
													+ ((HashMap) mapFormulla.get(param.get("MTYPE").toString()))
															.get("F1").toString()
													+ ",0)),2) AS PRODUCTIONMTR FROM " + mcodes[iM] + " M ";
//											if (Const.SOFTTYPE.equals("CR") || Const.SOFTTYPE.equals("RP")) {
											sQuery = sQuery + " INNER JOIN (SELECT * FROM BEAMMASTER WHERE BID IN ("
													+ hmBeam.get("BID").toString() + ")) B ON B.BID=M.BID";
//											}
											sQuery = sQuery + " WHERE M.BID IN (" + hmBeam.get("BID").toString() + ")";
										}
										sQuery = sQuery + ") A";
										List lstBeamData = dbUtils.getListKeyValuePairAndListAsync(sQuery, manager,
												true);
										double PTR = 0;
										if (Common.checkListNullAndEmpty(lstBeamData)) {
											Map hmBeamData = (HashMap) lstBeamData.get(0);
											PTR = Double.parseDouble(hmBeamData.get("PRODUCTIONMTR").toString());
										}
										int pMTR = (int) (Double.parseDouble(hmBeam.get("MTRS").toString()) - PTR);
										newParam.put("BMTR", pMTR);
										if ((Double.parseDouble(hmBeam.get("MTRS").toString()) - PTR) < Double
												.parseDouble(hmBeam.get("ALERTMTRS").toString())) {
											newParam.put("BALERT", "1");
										} else {
											newParam.put("BALERT", "0");
										}
									} else {
										newParam.put("BALERT", "0");
										newParam.put("BMTR", 0);
									}
								} else {
									newParam.put("BALERT", "0");
									newParam.put("BMTR", 0);
								}
							} else {
								newParam.put("BALERT", "0");
								newParam.put("BMTR", 0);
							}
							newParam.put("BRESET", "0");
						} else {
							newParam.put("BRESET", Integer.parseInt(param.get("BRESET").toString()) + 1);
						}
					} catch (Throwable t) {
						loger.error("beam" + t);
					}
					transaction.commit();
				} else {
					loger.error(CommonEnum.UNABLE_TO_ACQUIRE_TRANSACTION.getValue());
				}
			}

		} catch (PersistenceException ex) {
			Common.throwException(ex, null, transaction, responMap, null, emf, request);
		} catch (Exception e) {
			Common.throwException(null, e, transaction, responMap, null, emf, request);
		} catch (Throwable ex) {
			loger.error(ex.toString());
		} finally {
			bean = null;
			s = null;
			mapFormulla = null;
			if (manager.isOpen() && manager != null) {
				manager.clear();
				manager.close();
			}
		}

		return newParam;
	}

	@SuppressWarnings("rawtypes")
	public String getSER(Map param) throws Exception {
		ServiceBean bean = (ServiceBean) param.get("Bean");
		EntityManager manager = null;
		try {
			String[] M = bean.getM().split(",");
			manager = emf.createEntityManager();
			M[0] = manager.createNativeQuery("SELECT MCODE FROM MACHINE WHERE MSER='" + M[0] + "'").getSingleResult()
					.toString();
			return M[0];
		} catch (Throwable ex) {
			throw new Exception(ex.getMessage());
		} finally {
			if (manager.isOpen() && manager != null) {
				manager.clear();
				manager.close();
			}
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String cutTaka(Map param) throws Exception {
		ServiceBean bean = (ServiceBean) param.get("Bean");
		String res = "";
		EntityManager manager = null;
		EntityTransaction transaction = null;
		HashMap<String, Object> responMap = new HashMap<String, Object>();
		try {
			manager = emf.createEntityManager();
			if (manager.isOpen() && manager != null) {
				transaction = manager.getTransaction();
				if (transaction != null) {
					transaction.begin();

//					Session session = emf.createEntityManager().unwrap(Session.class);
//					List<?> cCutE = ListToJSONConverter.queryRunner(session,
//							"SELECT * FROM last_taka WHERE MCODE='" + bean.getM() + "'");
					List<?> cCutE = dbUtils.getListKeyValuePairAndListAsync(
							"SELECT * FROM last_taka WHERE MCODE='" + bean.getM() + "'", manager, true);

					String sQuery = "";
					String time = "";
					Map formulla = (HashMap) Const.getListFormulla().get(Const.SOFTTYPE);
					// if (cCutE.size() > 0) {
					if (Common.checkListNullAndEmpty(cCutE)) {
						time = ((HashMap) cCutE.get(0)).get("STIME").toString();
						sQuery = "SELECT M.BID,B.QUALITY,ROUND(SUM(IF(IFNULL(M.RTIME,0)>0,"
								+ formulla.get("F1").toString() + ",0)),2) PRO,B.MID,CURRENT_TIMESTAMP CUTTIME\n"
								+ "FROM " + bean.getM() + " M LEFT JOIN BEAMMASTER B ON B.BID=M.BID \n"
								+ "WHERE M.ETIME >= '" + time
								+ "' GROUP BY M.BID HAVING ROUND(SUM(IF(IFNULL(M.RTIME,0)>0,"
								+ formulla.get("F1").toString() + ",0)),2) > 0.00";

						List iLst = manager.createNativeQuery(sQuery).getResultList();
//						if (iLst.size() > 0) {
						if (Common.checkListNullAndEmpty(iLst)) {
							String m = manager
									.createNativeQuery("SELECT MID FROM MACHINE WHERE MCODE='" + bean.getM() + "'")
									.getSingleResult().toString();
							String oTime = time;
							sQuery = "SELECT CURRENT_TIMESTAMP";
							time = manager.createNativeQuery(sQuery).getSingleResult().toString();
							sQuery = "INSERT INTO TAKA(BID,QID,PRO,MID,CUTTIME,TTYPE,USERID) \n"
									+ "SELECT M.BID,B.QUALITY,ROUND(SUM(IF(IFNULL(M.RTIME,0)>0,"
									+ formulla.get("F1").toString() + ",0)),2) PRO,B.MID,'" + time
									+ "' CUTTIME,'M' TTYPE,'" + param.get("MUSERID").toString() + "' USERID\n" + "FROM "
									+ bean.getM() + " M LEFT JOIN BEAMMASTER B ON B.BID=M.BID \n" + "WHERE M.ETIME >= '"
									+ oTime + "' AND M.ETIME < '" + time
									+ "'  GROUP BY M.BID HAVING ROUND(SUM(IF(IFNULL(M.RTIME,0)>0,"
									+ formulla.get("F1").toString() + ",0)),2) > 0.00";

							manager.createNativeQuery(sQuery).executeUpdate();

							manager.createNativeQuery(
									"UPDATE last_taka SET STIME='" + time + "' WHERE MCODE='" + bean.getM() + "'")
									.executeUpdate();

							List<?> tk = dbUtils.getListKeyValuePairAndListAsync(
									"SELECT T.TID,T.PRO,Q.QNAME,B.BNO,DATE_FORMAT(CUTTIME,'%d/%m/%Y %H:%i:%s') FTIME FROM TAKA T INNER JOIN BEAMMASTER B ON B.BID=T.BID INNER JOIN QUALITYMASTER Q ON Q.QID=T.QID WHERE B.MID="
											+ m + " AND CUTTIME >= '" + time + "'",
									manager, true);

//							if (tk.size() > 0) {
							if (Common.checkListNullAndEmpty(tk)) {
								Map hm = (HashMap) tk.get(0);
								res = hm.get("TID").toString() + ":" + hm.get("PRO").toString() + ":"
										+ hm.get("QNAME").toString() + ":" + hm.get("BNO").toString() + ":"
										+ hm.get("FTIME").toString();
							} else {
								res = "";
							}
						} else {
							res = "";
						}

					} else {
						sQuery = "SELECT CURRENT_TIMESTAMP";
						time = manager.createNativeQuery(sQuery).getSingleResult().toString();
						manager.createNativeQuery(
								"INSERT INTO last_taka(MCODE,STIME) VALUES ('" + bean.getM() + "','" + time + "')")
								.executeUpdate();
						res = "";
					}
					Map mMain = (HashMap) Const.getListDevices();
					if (mMain.get(bean.getM()) != null) {
						Map hm1 = (HashMap) mMain.get(bean.getM());
						sQuery = "SELECT IFNULL((SELECT ROUND(SUM(IF(IFNULL(RTIME,0)>0," + formulla.get("F1").toString()
								+ ",0)),2) AS PRODUCTIONMTR FROM " + bean.getM() + " M ";
//						if (Const.SOFTTYPE.equals("CR") || Const.SOFTTYPE.equals("RP")) {
						sQuery = sQuery + " INNER JOIN BEAMMASTER B ON B.BID=M.BID";
//						}
						sQuery += " WHERE M.ETIME >='" + time + "'),0) ";
						hm1.put("TAKA_PROD", manager.createNativeQuery(sQuery).getSingleResult().toString());
						mMain.put(bean.getM(), hm1);
						Const.setListDevices(mMain);
					}

					transaction.commit();
				} else {
					loger.error(CommonEnum.UNABLE_TO_ACQUIRE_TRANSACTION.getValue());
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
			Common.throwThrowableException(ex, transaction, responMap, null, emf, request);
		} finally {
			if (manager.isOpen() && manager != null) {
				manager.clear();
				manager.close();
			}
		}
		return res;

	}

	public int startService(String machine, String request) {
		EntityManager manager = null;
		int id = 0;
		try {
			manager = emf.createEntityManager();
			id = manager
					.createNativeQuery("INSERT INTO REQUEST(MCODE,ETIME) VALUES ('" + machine + "','" + request + "')")
					.executeUpdate();
		} catch (Throwable ex) {
			loger.error(ex.toString());
		} finally {
			if (manager.isOpen() && manager != null) {
				manager.clear();
				manager.close();
			}
		}
		return id;

	}

	public static String reverse(String str) {
		String nstr = "";
		char ch;
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i); // extracts each character
			nstr = ch + nstr; // adds each character in front of the existing string
		}
		return nstr;
	}

	public static void sort(String[] I1, String[] T1, String[] R1) {
		if (I1.length > 1) {
			String[] sI1 = Arrays.copyOf(I1, I1.length);
			String[] sT1 = Arrays.copyOf(T1, T1.length);

			for (int i = 0; i < T1.length; i++) {
				T1[i] = reverse(T1[i].substring(0, T1[i].indexOf("T"))) + T1[i].substring(T1[i].indexOf("T"));
			}
			Arrays.sort(T1);
			for (int i = 0; i < T1.length; i++) {
				T1[i] = reverse(T1[i].substring(0, T1[i].indexOf("T"))) + T1[i].substring(T1[i].indexOf("T"));
			}
			for (int i = 0; i < T1.length; i++) {
				I1[i] = sI1[Arrays.asList(sT1).indexOf(T1[i])];
			}
			if (R1 != null) {
				String[] sR1 = Arrays.copyOf(R1, R1.length);
				for (int i = 0; i < T1.length; i++) {
					R1[i] = sR1[Arrays.asList(sT1).indexOf(T1[i])];
				}
			}

		}
	}

	public String endService(int rid) {
		EntityManager manager = null;
		String res = "";
		try {
			manager = emf.createEntityManager();
			manager.createNativeQuery("UPDATE REQUEST SET ESTIME=CURRENT_TIMESTAMP WHERE RID='" + rid + "'")
					.executeUpdate();
		} catch (Throwable ex) {
			loger.error(ex.toString());
		} finally {
			if (manager.isOpen() && manager != null) {
				manager.clear();
				manager.close();
			}
		}
		return res;

	}

	public String startINService(int rid) {

		EntityManager manager = null;
		String res = "";
		try {
			manager = emf.createEntityManager();
			manager.createNativeQuery("UPDATE REQUEST SET ESINTIME=CURRENT_TIMESTAMP WHERE RID='" + rid + "'")
					.executeUpdate();
		} catch (Throwable ex) {
			loger.error(ex.toString());
		} finally {
			if (manager.isOpen() && manager != null) {
				manager.clear();
				manager.close();
			}
		}
		return res;

	}

	public String endINService(int rid) {
		EntityManager manager = null;
		String res = "";
		try {
			manager = emf.createEntityManager();
			manager.createNativeQuery("UPDATE REQUEST SET ESOUTTIME=CURRENT_TIMESTAMP WHERE RID='" + rid + "'")
					.executeUpdate();
		} catch (Throwable ex) {
			loger.error(ex.toString());
		} finally {
			if (manager.isOpen() && manager != null) {
				manager.clear();
				manager.close();
			}
		}
		return res;
	}
}
