package com.quotepro.common.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;

/**
 * RequestHandler This is RequestHandler Class.
 */

@Component
public class ServiceHandlerC extends Thread {

	public static final Logger loger = LoggerFactory.getLogger(ServiceHandlerC.class);

	private EntityManagerFactory emf = null;

	@SuppressWarnings("rawtypes")
	private Map[] bean;

	@SuppressWarnings("rawtypes")
	public ServiceHandlerC(Map[] bean) {
		this.bean = bean;
	}

	@Autowired
	public ServiceHandlerC(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run() {

		Date ctime = null;
		SimpleDateFormat sdf = null, sdfs = null;
		SimpleDateFormat sdfdis = null;
		Map mMain1 = null;
		String mcode = null;

		try {

			sdfs = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			sdf = new SimpleDateFormat("ddMMyy HHmmss");
			sdfdis = new SimpleDateFormat("dd/MM HH:mm:ss");
			ArrayList list = new ArrayList();
			for (int i = 0; i < bean.length; i++) {
				Map hm = bean[i];
				mcode = hm.get("DeviceId").toString();
				if (((i + 1) < bean.length && hm.get("DeviceId").toString() != bean[i + 1].get("DeviceId").toString())
						|| (i + 1) == bean.length) {
					Map param = new HashMap();
					mMain1 = (HashMap) Const.getListDevices().get(mcode);
					ctime = sdfs.parse(hm.get("DateTime").toString());
					mMain1.put("PULSETIME", sdf.format(ctime));
					mMain1.put("PULSET", sdfdis.format(ctime));
					mMain1.put("PULSE", hm.get("RPM").toString());
					if (mMain1.get("LAST").toString().equals("0") && mMain1.get("RUN").toString().equals("0")) {
						if (Integer.parseInt(hm.get("RPM").toString()) > 0) {
							// mMain1.put("RUN", bean.getI2());
							mMain1.put("RUN", "1");
						}
						// bean.setI2("1");
						mMain1.put("LAST", "1");
					}
					if (Integer.parseInt(hm.get("RPM").toString()) > 0) {
						mMain1.put("RUN", "1");
						mMain1.put("SFLAG", "I2");
						mMain1.put("STIME", sdf.format(ctime));
					} else {
						mMain1.put("RUN", "0");
						mMain1.put("SFLAG", "I2");
						mMain1.put("STTIME", sdf.format(ctime));
					}
					param.put("EFFITIME", mMain1.get("EFFITIME"));
					param.put("PICK", mMain1.get("PICK"));
					param.put("BID", mMain1.get("BID"));
					param.put("ARPM", mMain1.get("ARPM"));
					param.put("BRESET", mMain1.get("BRESET"));
					param.put("TAKA_PROD", mMain1.get("TAKA_PROD"));
					param.put("CSHIFT", mMain1.get("CSHIFT"));
					param.put("QSHORTAGE", mMain1.get("QSHORTAGE"));
					param.put("MTYPE", mMain1.get("MTYPE"));
					param.put("LRUN", mMain1.get("LRUN"));
					param.put("Bean", list);

					Map newParam = ServiceModel.insertDataC(param);
					if (newParam == null) {
						throw new Exception("SQL Service Error");
					}
					if (newParam.get("LRUN") != null) {
						mMain1.put("LRUN", newParam.get("LRUN").toString());
					}
					if (newParam.get("TAKA_PROD") != null) {
						mMain1.put("TAKA_PROD", newParam.get("TAKA_PROD").toString());
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
						int i1 = Integer.parseInt(hm.get("RPM").toString());
						int r1 = Integer.parseInt(hm.get("tCount").toString());
						if (r1 != 0) {
							mMain1.put("PULSE", Math.round(60 * i1 / r1));
						} else {
							mMain1.put("PULSE", 0);
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
					list = new ArrayList();
				} else {
					list.add(bean[i]);
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
//            loger.error("EEE Status Error : " + bean.getM() + " : " + e);
			loger.error("EEE Status Error : " + e);
		} finally {
			ctime = null;
			sdf = null;
			sdfdis = null;
			mMain1 = null;
			mcode = null;
		}
	}
}
