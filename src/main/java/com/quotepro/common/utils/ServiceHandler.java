package com.quotepro.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;

@Component
public class ServiceHandler extends Thread {

	private static final Logger loger = LoggerFactory.getLogger(ServiceHandler.class);
	private Logger logger1 = LoggerFactory.getLogger("com.example.myapp.CustomLogger");
	private ServiceBean bean;

	private EntityManagerFactory emf = null;

	public ServiceHandler(ServiceBean bean) {
		this.bean = bean;
	}

	@Autowired
	public ServiceHandler(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run() {
		if (bean.getS() != null) {
			loger.error("M=>" + bean.getM() + "\n" + "T=>" + bean.getT1() + "\n" + "S=>" + bean.getS());
		}
		Map param = new HashMap();
		Date ctime = null, sstime = null;
		Date cttime = Const.getCurrentTime();
		SimpleDateFormat sdf = null;
		SimpleDateFormat sdfdis = null;
		Map mMain1 = null;
		String mcode = null;
		Date ftime = Const.getCurrentTime();
		try {
			sdf = new SimpleDateFormat("ddMMyy HHmmss");
			sdfdis = new SimpleDateFormat("dd/MM HH:mm:ss");
			param.put("Bean", bean);
			mcode = bean.getM().split(",")[0];
			mMain1 = (HashMap) Const.getListDevices().get(mcode);
			if (mMain1 != null) {
				if (bean.getI1() != null) {

					if (bean.getT1() != null && !bean.getT1().isEmpty()) {
						ctime = sdf.parse(bean.getT1().split(",")[0].toString().replace('T', ' '));
					}
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
					if (ctime.compareTo(ctcalendarS.getTime()) < 0 || ctime.compareTo(ctcalendarE.getTime()) > 0) {
						mMain1.put("PULSETIME", sdf.format(cttime));
						mMain1.put("PULSET", sdfdis.format(cttime));
					} else {
						mMain1.put("PULSETIME", sdf.format(ctime));
						mMain1.put("PULSET", sdfdis.format(ctime));
					}
					mMain1.put("PULSE", bean.getI1().split(",")[0]);
					if (mMain1.get("LAST").toString().equals("0") && mMain1.get("RUN").toString().equals("0")) {
						if (Integer.parseInt(bean.getI1().split(",")[0]) > 0) {
							mMain1.put("RUN", "1");
						}
						mMain1.put("LAST", "1");
					}
				}
				if (bean.getI2() != null) {
					mMain1.put("RUN", bean.getI2().split(",")[bean.getI2().split(",").length - 1]);
					mMain1.put("SFLAG", "I2");
					mMain1.put("STIME", "0");
					if (bean.getI2().split(",")[bean.getI2().split(",").length - 1].equals("0")) {
						sstime = sdf
								.parse(bean.getT2().split(",")[bean.getT2().split(",").length - 1].replace('T', ' '));
						Calendar ccalendar = new GregorianCalendar();
						ccalendar.setTime(sstime);
						Calendar ctcalendar = new GregorianCalendar();
						ctcalendar.setTime(cttime);
						if (ccalendar.get(Calendar.MONTH) == ctcalendar.get(Calendar.MONTH)
								&& ccalendar.get(Calendar.DAY_OF_MONTH) == ctcalendar.get(Calendar.DAY_OF_MONTH)
								&& ccalendar.get(Calendar.YEAR) != ctcalendar.get(Calendar.YEAR)) {
							ccalendar.set(Calendar.YEAR, ctcalendar.get(Calendar.YEAR));
							sstime = ccalendar.getTime();
						}
						mMain1.put("STIME", sdf.format(cttime));
					} else {
						mMain1.put("STTIME", sdf.format(cttime));
					}
				}
				if (bean.getI3() != null) {
					if (bean.getI2().split(",")[bean.getI2().split(",").length - 1].equals("1")) {
						Date ctime1 = sdf.parse(bean.getT2().split(",")[bean.getT2().split(",").length - 1].toString()
								.replace('T', ' '));
						Date ctime2 = sdf.parse(bean.getT3().split(",")[bean.getT3().split(",").length - 1].toString()
								.replace('T', ' '));
						if (ctime2.after(ctime1)) {
							mMain1.put("RUN", "0");
							mMain1.put("SFLAG", "I3");
							Calendar ctcalendar = new GregorianCalendar();
							ctcalendar.setTime(cttime);
							mMain1.put("STIME", sdf.format(cttime));
							mMain1.put("STTIME", "0");
						}
					} else {
						mMain1.put("RUN", "0");
						mMain1.put("SFLAG", "I3");
						Calendar ctcalendar = new GregorianCalendar();
						ctcalendar.setTime(cttime);
						mMain1.put("STIME", sdf.format(cttime));
						mMain1.put("STTIME", "0");
					}
				}
				if (bean.getI4() != null) {
					Date ctime3 = null;
					if (bean.getI3() != null) {
						ctime3 = sdf.parse(bean.getT3().split(",")[bean.getT3().split(",").length - 1].toString()
								.replace('T', ' '));
					}
					Date ctime2 = sdf.parse(
							bean.getT4().split(",")[bean.getT4().split(",").length - 1].toString().replace('T', ' '));
					if (bean.getI2().split(",")[bean.getI2().split(",").length - 1].equals("1")) {
						Date ctime1 = sdf.parse(bean.getT2().split(",")[bean.getT2().split(",").length - 1].toString()
								.replace('T', ' '));
						if (ctime2.after(ctime1)) {
							if (ctime3 == null || (ctime3 != null && ctime2.after(ctime3))) {
								mMain1.put("RUN", "0");
								mMain1.put("SFLAG", "I4");
								Calendar ctcalendar = new GregorianCalendar();
								ctcalendar.setTime(cttime);
								mMain1.put("STIME", sdf.format(cttime));
								mMain1.put("STTIME", "0");
							}
						}
					} else {
						if (ctime3 == null || (ctime3 != null && ctime2.after(ctime3))) {
							mMain1.put("RUN", "0");
							mMain1.put("SFLAG", "I4");
							Calendar ctcalendar = new GregorianCalendar();
							ctcalendar.setTime(cttime);
							mMain1.put("STIME", sdf.format(cttime));
							mMain1.put("STTIME", "0");
						}
					}
				}
				if (bean.getI5() != null) {
					mMain1.put("RUN", "0");
					mMain1.put("SFLAG", "I5");
					Calendar ctcalendar = new GregorianCalendar();
					ctcalendar.setTime(cttime);
					mMain1.put("STIME", sdf.format(cttime));
					mMain1.put("STTIME", "0");
				}
				param.put("EFFITIME", mMain1.get("EFFITIME"));
				param.put("PICK", mMain1.get("PICK"));
				param.put("COUNTER", mMain1.get("COUNTER"));
                param.put("KGS", mMain1.get("KGS"));
				param.put("BID", mMain1.get("BID"));
				param.put("ARPM", mMain1.get("ARPM"));
				param.put("BRESET", mMain1.get("BRESET"));
				param.put("TAKA_PROD", mMain1.get("TAKA_PROD"));
				param.put("LCUT", mMain1.get("LCUT"));
				param.put("TAKAALERTFLAG", mMain1.get("TAKAALERTFLAG"));
				param.put("DBTAKAALERT", mMain1.get("DBTAKAALERT"));
				param.put("CSHIFT", mMain1.get("CSHIFT"));
				param.put("QSHORTAGE", mMain1.get("QSHORTAGE"));
				param.put("MTYPE", mMain1.get("MTYPE"));
				

				long diffInMillies = Math.abs(Const.getCurrentTime().getTime() - ftime.getTime());
				ftime = Const.getCurrentTime();

				Map newParam = ServiceModel.insertDataN(param);
				long diffInMillies1 = Math.abs(Const.getCurrentTime().getTime() - ftime.getTime());
				
				ftime = Const.getCurrentTime();
				if (newParam == null) {
					throw new Exception("SQL Service Error");
				}
				if (newParam.get("TAKA_PROD") != null) {
					mMain1.put("TAKA_PROD", newParam.get("TAKA_PROD").toString());
				}
				if (newParam.get("TAKAALERT") != null) {
					mMain1.put("TAKAALERT", newParam.get("TAKAALERT").toString());
				}
				if (newParam.get("ARPM") != null) {
					mMain1.put("ARPM", newParam.get("ARPM").toString());
				}
				if (newParam.get("EFFI") != null) {
					mMain1.put("EFFI", newParam.get("EFFI").toString());
				}
				if (newParam.get("ROUNDS") != null) {
					mMain1.put("ROUNDS", newParam.get("ROUNDS").toString());
				}
				if (newParam.get("TRUN") != null) {
					mMain1.put("TRUN", newParam.get("TRUN").toString());
				}
				if (newParam.get("BREAK") != null) {
					mMain1.put("BREAK", newParam.get("BREAK").toString());
				}
				if (newParam.get("CSHIFT") != null) {
					mMain1.put("CSHIFT", newParam.get("CSHIFT").toString());
				}
				if (newParam.get("FRPM") != null && Const.getListCPANEL().get("FRPM").toString().equals("1")
						&& !mMain1.get("PULSE").toString().trim().equals("0")) {
					mMain1.put("PULSE", newParam.get("FRPM").toString());
				} else if (newParam.get("FRPM") != null && Const.getListCPANEL().get("FRPM").toString().equals("2")
						&& !mMain1.get("PULSE").toString().trim().equals("0")) {
					if (bean.getI1() != null) {
						int i1 = Integer.parseInt(bean.getI1().split(",")[0]);
						int r1 = Integer.parseInt(bean.getR1().split(",")[0]);
						if (r1 != 0) {
							mMain1.put("PULSE", Math.round(60 * i1 / r1));
						} else {
							mMain1.put("PULSE", 0);
						}
					}
				}
				if (newParam.get("BRESET") != null) {
					mMain1.put("BRESET", newParam.get("BRESET").toString());
				}
				if (newParam.get("BMTR") != null) {
					mMain1.put("BMTR", newParam.get("BMTR").toString());
				}
				if (newParam.get("BALERT") != null) {
					if (!newParam.get("BALERT").toString().equals("0")
							&& (Double.parseDouble(newParam.get("BMTR").toString()) < Double
									.parseDouble(Const.getListCPANEL().get("BALERTMTR").toString()))) {
						mMain1.put("BALERT", "2");
					} else {
						mMain1.put("BALERT", newParam.get("BALERT").toString());
					}
				}
				Const.getListDevices().put(mcode, mMain1);
				logger1.error("Res Time : " + bean.getM() + " : " + diffInMillies + " "+ diffInMillies1 );
			}
			
		} catch (Throwable e) {
			loger.error("EEE Status Error : " + bean.getM() + " : " + e);
		} finally {
			ctime = null;
			sstime = null;
			cttime = null;
			sdf = null;
			sdfdis = null;
			mMain1 = null;
			mcode = null;
		}
	}
}
