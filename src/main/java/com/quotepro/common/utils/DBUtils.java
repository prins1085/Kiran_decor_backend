package com.quotepro.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

@Configuration
public class DBUtils {
	@Autowired
	private EntityManagerFactory emf;
	public static final Logger LOGGER = LoggerFactory.getLogger(DBUtils.class);
	
	public List<?> returnResultSet(String query, Map<String, Object> keysAndValues, EntityManager em)
			throws Exception {
		Assert.notNull(query, "Sql Query Should Not Null");
		if (query.trim().length() == 0)
			throw new IllegalArgumentException("Sql Query Should Not Empty");
		Query queryObj = em.createNativeQuery(query);
		if (null != keysAndValues && !keysAndValues.isEmpty()) {
			for (Map.Entry<String, Object> keyAndValue : keysAndValues.entrySet()) {
				Assert.notNull(keyAndValue, "Parameter key And Value Should Not Null");
				String key = keyAndValue.getKey();
				Assert.notNull(key, "Parameter key Should Not Null");

				if (key.trim().length() == 0)
					throw new IllegalArgumentException("Parameter key Should Not Empty");

				queryObj.setParameter(key, keyAndValue.getValue());
			}
		}

//		if (log.isDebugEnabled())
//			log.debug("{}", query);
		return queryObj.getResultList();
	}

	public String getMaxId(String tablename, String columnname, String whereClause, EntityManager em) {
		try {
			String query = "SELECT IFNULL(MAX(" + columnname + "),0)+1 FROM " + tablename;
			if (null != whereClause && whereClause.trim().length() > 0)
				query += " " + whereClause;
			return em.createNativeQuery(query).getSingleResult().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getarraylist(List list) {
		List retLst = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Map hm = (HashMap) list.get(i);
			Object[] oMapValue = hm.values().toArray();
			String[] strArr = new String[hm.size()];
			for (int j = 0; j < oMapValue.length; j++) {
				if (oMapValue[j] == null) {
					strArr[j] = "";
				} else {
					strArr[j] = oMapValue[j].toString();
				}
			}
			retLst.add(i, strArr);
		}
		return retLst;
	}

	@SuppressWarnings("rawtypes")
	public String ListToArray(List lst) {
		StringBuilder rst = new StringBuilder();
		String element;
		rst.append("[");
		if (lst != null && lst.size() > 0) {
			for (Object lstItem : lst) {
				Map hm = (HashMap) lstItem;
				Set key = hm.keySet();
				Iterator itr = key.iterator();
				rst.append("[");
				while (itr.hasNext()) {
					element = itr.next().toString();
					rst.append("\"" + (hm.get(element.toString()) == null ? ""
							: hm.get(element.toString()).toString().replace("\"", "\\\"")) + "\",");
				}
				rst.setLength(rst.length() - 1);
				rst.append("],");
			}
		}
		if (rst.length() > 1) {
			rst.setLength(rst.length() - 1);
			rst.append("]");
		} else {
			rst.append("]");
		}
		return rst.toString().replace("\r", "").replace("\n", "");
	}
	
	// OLD METHOD
	@SuppressWarnings("rawtypes")
	public List<?> getListKeyValuePairAndList(String query, EntityManager manager, boolean iskeyvalue) {
		Session session = null;
		List<?> list = new ArrayList();
		try {
			if (iskeyvalue) {
				session = emf.createEntityManager().unwrap(Session.class);
				list = ListToJSONConverter.queryRunner(session, query);
			} else {
				list = manager.createNativeQuery(query).getResultList();
			}
			return list;
		} catch (Exception e) {
			LOGGER.error("Dbutils : " + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return list;
	}
	
//	public List<?> getListKeyValuePairAndList(String query, EntityManager manager, boolean iskeyvalue) {
//		Session session = null;
//		List<?> list = null;
//		try {
//			if (iskeyvalue) {
//				session = emf.createEntityManager().unwrap(Session.class);
//				CompletableFuture<List<Map<String, Object>>>  futureResult =  ListToJSONConverter.queryRunnerAsync(session, query);
//				list = futureResult.get();
//			} else {
//				list = manager.createNativeQuery(query).getResultList();
//			}
//			return list;
//		} catch (Exception e) {
//			LOGGER.error("Dbutils_Async : " + e.getMessage());
//			e.printStackTrace();
//		} finally {
//			
//		}
//		return list;
//	}
	
	public List<?> getListKeyValuePairAndListAsync(String query, EntityManager manager, boolean iskeyvalue) {
		Session session = null;
		List<?> list = null;
		try {
			if (iskeyvalue) {
				session = emf.createEntityManager().unwrap(Session.class);
				CompletableFuture<List<Map<String, Object>>>  futureResult =  ListToJSONConverter.queryRunnerAsync(session, query);
				list = futureResult.get();
			} else {
				list = manager.createNativeQuery(query).getResultList();
			}
			return list;
		} catch (Exception e) {
			LOGGER.error("Dbutils_Async : " + e.getMessage());
			e.printStackTrace();
		} finally {
			
		}
		return list;
	}

	public Map<String, Map<?, ?>> getKeyList(List<?> list, String key) {
		Map<String, Map<?, ?>> keyList = new HashMap<String, Map<?, ?>>();
		for (int i = 0; i < list.size(); i++) {
			Map<?, ?> hm = (HashMap<?, ?>) list.get(i);
			keyList.put(hm.get(key).toString(), hm);

		}
		return keyList;
	}

	public List<?> getArrayList(String query) {
		EntityManager manager = null;
//		Session session = null;
		List<?> list = null;
		try {
			manager = emf.createEntityManager();
//			session = emf.createEntityManager().unwrap(Session.class);
//			list = ListToJSONConverter.queryRunner(session, query);
			list = manager.createNativeQuery(query).getResultList();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (manager.isOpen() && manager != null) {
				manager.clear();
				manager.close();
			}
		}
		return null;
	}

	public List<?> getListKeyValuePair(String query) {
		EntityManager manager = null;
		Session session = null;
		try {
			manager = emf.createEntityManager();
			session = emf.createEntityManager().unwrap(Session.class);
			List<?> list = ListToJSONConverter.queryRunner(session, query);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (manager.isOpen() && manager != null) {
				manager.clear();
				manager.close();
			}
		}
		return null;
	}
	
	/**
	 * Return String SingleResult.
	 *
	 * @param s Any Query
	 * @return The Query passed return SingleResult Otherwise Empty
	 */
//	public String getSingleStringData(String query, EntityManager manager) {
//		Object Object = "";
//		String result = "";
//		if (Common.checkNullAndEmpty(query)) {
//			try {
//				Object = manager.createNativeQuery(query).getSingleResult();
//				result = Common.checkNullAndEmpty(Object) ? Object.toString() : "";
//				return result;
//			} catch (Exception e) {
//				e.printStackTrace();
//				return "";
//			}
//		}
//		return result;
//	}
	
	@SuppressWarnings("unchecked")
	public String getSingleStringData(String query, EntityManager manager) {
	    String result = "";
	    if (Common.checkNullAndEmpty(query)) {
	        try {
	            List<Object> resultList = manager.createNativeQuery(query).getResultList();
	            if (!resultList.isEmpty()) {
	                Object obj = resultList.get(0);
	                result = Common.checkNullAndEmpty(obj) ? obj.toString() : "";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "";
	        }
	    }
	    return result;
	}
	@SuppressWarnings("unchecked")
	public String getSingleStringDataNoEntityManager(String query) {
		EntityManager manager = null;
		String result = "";
		try {
			manager = emf.createEntityManager();
			if (Common.checkNullAndEmpty(query)) {
				try {
					List<Object> resultList = manager.createNativeQuery(query).getResultList();
					if (!resultList.isEmpty()) {
						Object obj = resultList.get(0);
						result = Common.checkNullAndEmpty(obj) ? obj.toString() : "";
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (manager.isOpen() && manager != null) {
				manager.clear();
				manager.close();
			}
		}
		return result;
	}


//	public Integer getSingleIntData(String query, EntityManager manager) {
//		Object Object = "";
//		Integer value = 0;
//		if (Common.checkNullAndEmpty(query)) {
//			try {
//				Object = manager.createNativeQuery(query).getSingleResult();
//				value = Common.checkNullAndEmpty(Object) ? Integer.parseInt(Object.toString()) : 0;
//				//value = (((Number) manager.createNativeQuery(query).getSingleResult()).intValue());
//				return value;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return value;
//	}
	
	@SuppressWarnings("unchecked")
	public Integer getSingleIntData(String query, EntityManager manager) {
	    Integer value = 0;
	    if (Common.checkNullAndEmpty(query)) {
	        try {
	            List<Object> resultList = manager.createNativeQuery(query).getResultList();
	            if (!resultList.isEmpty()) {
	                Object result = resultList.get(0);
	                value = Common.checkNullAndEmpty(result) ? Integer.parseInt(result.toString()) : 0;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return value;
	}

}
