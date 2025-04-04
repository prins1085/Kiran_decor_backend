package com.quotepro.common.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.persistence.Query;

@Component
public class ListToJSONConverter {

	private static final ExecutorService executor = Executors.newFixedThreadPool(1000);
	private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;
	private static String regexCommawithnumber = "([0-9]+([,])(.)*)+";
	private ObjectMapper mapper = new ObjectMapper();
	static List<Map<String, Object>> list = null;

	public String convert(List<?> objectList, String[] columnsToEncrypt, String... columnSpecification)
			throws IOException {
		List<String> columnsToEncryptList = new ArrayList<>();
		if (columnsToEncrypt != null) {
			Collections.addAll(columnsToEncryptList, columnsToEncrypt);
		}

		ArrayNode arrayNode = new ArrayNode(FACTORY);
		for (int i = 0; i < objectList.size(); i++) {
			ObjectNode node = new ObjectNode(FACTORY);

			if (objectList.get(i).getClass().isArray()) {
				Object[] objects = (Object[]) objectList.get(i);
				for (int j = 0; j < columnSpecification.length; j++) {
					if (columnsToEncryptList.contains(columnSpecification[j])) {
						if (String.valueOf(objects[j]).matches(regexCommawithnumber)) {
							String[] numarr = String.valueOf(objects[j]).split(",");
							String encryptval = "";
							int count = 0;
							for (int k = 0; k < numarr.length; k++) {
								if (count == 0)
									encryptval += String.valueOf(numarr[k]);
								else
									encryptval += "," + String.valueOf(numarr[k]);
								count++;
							}
							node.put(columnSpecification[j],
									(encryptval != null && encryptval != "") ? encryptval : "");
						} else {
							node.put(columnSpecification[j],
									(Common.checkNullAndEmpty(objects[j])) ? String.valueOf(objects[j]) : "");
						}
					} else {
						node.put(columnSpecification[j], (objects[j] != null) ? String.valueOf(objects[j]) : "");
					}
				}
			} else {
				Object obj = objectList.get(i);
				if (columnsToEncryptList.contains(columnSpecification[0])) {
					node.put(columnSpecification[0], (obj != null) ? String.valueOf(obj) : "");
				} else {
					node.put(columnSpecification[0], (obj != null) ? String.valueOf(obj) : "");
				}
			}
			arrayNode.add(node);
		}
		return mapper.writeValueAsString(arrayNode);
	}

	public String keyAndValueList(List<?> list, String... columnSpecification) throws IOException {
		ArrayNode arrayNode = new ArrayNode(FACTORY);
		for (int i = 0; i < list.size(); i++) {
			ObjectNode node = new ObjectNode(FACTORY);
			if (list.get(i).getClass().isArray()) {
				Object[] objects = (Object[]) list.get(i);
				for (int j = 0; j < columnSpecification.length; j++) {
					node.put(columnSpecification[j], (objects[j] != null) ? String.valueOf(objects[j]) : "");
				}
			}
			arrayNode.add(node);
		}

		return mapper.writeValueAsString(arrayNode);
	}

	public static List<Map<String, Object>> queryRunner(Session session, String sqlQuery) throws Exception {
		list = null;
		try {
			if (Common.checkNullAndEmpty(sqlQuery)) {
				session.doWork((Work) new Work() {
					@Override
					public void execute(Connection connection) throws SQLException {
						list = new QueryRunner().query(connection, sqlQuery, new MapListHandler());
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (session != null) {
				session.clear();
				session.close();
			}
		}
		return (List<Map<String, Object>>) list;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static CompletableFuture<List<Map<String, Object>>> queryRunnerAsync(Session session, String sqlQuery) {
		return CompletableFuture.supplyAsync(() -> {
			List<Map<String, Object>> list = null;
			try {
				if (Common.checkNullAndEmpty(sqlQuery)) {
					Query query = session.createNativeQuery(sqlQuery).unwrap(NativeQuery.class)
							.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

					list = query.getResultList();
				}
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e); // Wrap in a runtime exception for CompletableFuture
			} finally {
				if (session != null) {
					session.clear();
					session.close();
				}
			}
		}, executor); // Execute the task asynchronously using the provided executor service
	}

//	public static List<Map<String, Object>> queryRunnerNew(Session session, String sqlQuery) throws Exception {
//		list = null;
//		try {
//			if (Common.checkNullAndEmpty(sqlQuery)) {
//				Query query = session.createNativeQuery(sqlQuery).unwrap(org.hibernate.query.NativeQuery.class)
//						.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
//
//				// Execute the query and get the result list
//				list = query.getResultList();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception(e);
//		} finally {
//			if (session != null) {
//				session.clear();
//				session.close();
//			}
//		}
//		return (List<Map<String, Object>>) list;
//	}

}
