package com.quotepro.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;

@Configuration
public class DbModel {

	@Autowired
	private DBUtils dbUtils;

	@Autowired
	private HttpSession session;

	public List<?> getAllMachines(String MSORT, EntityManager manager) throws Exception {
		String sQuery = " SELECT MCODE,M.MNAME,MG.MGNAME,MG.MGID,M.MTYPE" + " FROM MACHINE M "
				+ " INNER JOIN MACHINEGROUP MG ON MG.MGID=M.MGID" + " WHERE M.ISHIDDEN=0";

		if (Common.checkNullAndEmpty(MSORT)) {
			sQuery += " ORDER BY MORDER,MNAME";
		} else {
			sQuery += " ORDER BY MGNAME,MGORDER,MNAME";
		}

		return dbUtils.getarraylist(dbUtils.getListKeyValuePairAndList(sQuery, manager, true));
	}

	public List<?> getAllMachinesFactorywise(String MSORT, EntityManager manager) throws Exception {
		String where = "";
		String factory = dbUtils.getSingleStringData(
				"SELECT FACTORY  FROM `USER` U WHERE USERID  = " + session.getAttribute("USERID"), manager);
		if (Common.checkNullAndEmpty(factory)) {
			where = "and FID in(" + factory + ")";
		}

		String sQuery = " SELECT MCODE,M.MNAME,MG.MGNAME,MG.MGID,M.MTYPE" + " FROM MACHINE M "
				+ " INNER JOIN MACHINEGROUP MG ON MG.MGID=M.MGID" + " WHERE M.ISHIDDEN=0 " + where;

		if (Common.checkNullAndEmpty(MSORT)) {
			sQuery += " ORDER BY MORDER,MNAME";
		} else {
			sQuery += " ORDER BY MGNAME,MGORDER,MNAME";
		}

		return dbUtils.getarraylist(dbUtils.getListKeyValuePairAndList(sQuery, manager, true));
	}

	public List getTermminalH() throws Exception {

		String sQuery = " SELECT MCODE,M.MNAME,'DEFAULT' MGNAME" + " FROM MACHINE M " + " WHERE M.ISHIDDEN=0 "
				+ " ORDER BY MORDER,MNAME";
		return dbUtils.getArrayList(sQuery);

	}

	public List getTermminalGM() throws Exception {
		String where = "";
		String factory = dbUtils.getSingleStringDataNoEntityManager(
				"SELECT FACTORY  FROM `USER` U WHERE USERID  = " + session.getAttribute("USERID"));
		if (Common.checkNullAndEmpty(factory)) {
			where = " and FID in(" + factory + ")";
		}

		String sQuery = " SELECT M.MCODE,M.MNAME,IFNULL(Q.QNAME,'') MGNAME"
				+ " FROM MACHINE M LEFT JOIN BEAMMASTER B ON M.MID=B.MID LEFT JOIN QUALITYMASTER Q ON Q.QID=B.QUALITY  ";
		sQuery += " WHERE B.FTIME IS NULL AND M.ISHIDDEN = 0 " + where;
		sQuery += " ORDER BY Q.QNAME,MNAME ";
		// return dbUtils.getArrayList(sQuery);
		return dbUtils.getarraylist(dbUtils.getListKeyValuePair(sQuery)); // Change Dashboardtime qulity wise error

	}

	public String getZoomlevel() throws Exception {
		return Const.getListCPANEL().get("ZOOM").toString();
//        SQLServices objSQL = new SQLServices();
//        String sQuery = " SELECT VALUE FROM CPANEL WHERE KEYVAL='ZOOM'";
//        return objSQL.getString(sQuery);
	}

	public List getAllMachinesGroup(Map param) throws Exception {
		String sQuery = " SELECT MGNAME" + " FROM MACHINEGROUP ORDER BY MGNAME";
		return dbUtils.getArrayList(sQuery);

	}

	public List getAllQuality() throws Exception {

		String sQuery = " SELECT QID,QNAME" + " FROM QUALITYMASTER ORDER BY QNAME";
		return dbUtils.getArrayList(sQuery);
	}

	public List getAllindicate(Map param) throws Exception {
		String sQuery = " SELECT COLOUR,NAME"
				+ " FROM EFFIRANGE UNION ALL SELECT MSCOLOUR,NAME FROM MACHINESTATUS WHERE NAME IS NOT NULL";
		return dbUtils.getArrayList(sQuery);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getAllNotification(Map param) throws Exception {
		LoginBean bean = (LoginBean) param.get("Bean");
		Map args = new HashMap();
		args.put("USERNAME", bean.getTxtusername());
		args.put("PASSWORD", bean.getTxtpassword());

		String sQuery = " SELECT NEFFICIENCY,NSTIME,NSTOPAGE FROM USER WHERE USERNAME= '" + bean.getTxtusername()
				+ "' AND PASSWORD= '" + bean.getTxtpassword() + "' ";
		List lhm = dbUtils.getListKeyValuePair(sQuery);

		Map hm = (HashMap) lhm.get(0);
		sQuery = " SELECT * FROM NOTIFICATION " + "WHERE TYPE IN ("
				+ (hm.get("NEFFICIENCY").toString().equals("1") ? "'efficiancy'," : "'',")
				+ (hm.get("NSTIME").toString().equals("1") ? "'stoptime'," : "'',")
				+ (hm.get("NSTOPAGE").toString().equals("1") ? "'stopage'" : "''") + " ) ORDER BY NTIME DESC";
		// System.out.println("query = " + sQuery);

		return dbUtils.getArrayList(sQuery);
	}

	public void setlTermminalConstData(EntityManager manager) {
		try {
			Const.setlTermminal(getAllMachines("1", manager));
			Const.setlTermminalGM(getAllMachines(null, manager));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setlTermminalConstDataFactoryWise(EntityManager manager) {
		try {
			Const.setlTermminal(getAllMachinesFactorywise("1", manager));
			Const.setlTermminalGM(getAllMachinesFactorywise(null, manager));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
