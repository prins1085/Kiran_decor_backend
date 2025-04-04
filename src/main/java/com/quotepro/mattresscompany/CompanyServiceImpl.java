package com.quotepro.mattresscompany;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quotepro.common.enumeration.CommonEnum;
import com.quotepro.common.enumeration.ToastrAction;
import com.quotepro.common.utils.Common;
import com.quotepro.common.utils.DBUtils;
import com.quotepro.common.utils.DbModel;
import com.quotepro.common.utils.ListToJSONConverter;
import com.quotepro.quotation.QuationDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class CompanyServiceImpl implements CompanyService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EntityManagerFactory emf;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private DbModel dbModel;

	@Autowired
	DBUtils dbUtils;

	@Autowired
	ListToJSONConverter listToJSONConverter;

	// ============================= Grid ======================== //
	@Override
	public ResponseEntity<Object> Grid() {
		EntityManager manager = null;
		try {
			manager = emf.createEntityManager();
			String query = "select mattcompanyid as id ,company ,productName ,`size` ,price from mattresscompany c ";
			List<?> gridlist = dbUtils.getListKeyValuePairAndList(query, manager, true);
			return ResponseEntity.ok(gridlist);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonEnum.ERROR_OCCURED_WHILE_RETRIWING_DATA.getValue(),
					HttpStatus.BAD_REQUEST);
		} finally {
			if (manager != null) {
				manager.clear();
				manager.close();
			}
		}
	}

	// ================ Save ===============//
	@Override
	public <T> Object save(MattressCompanyDTO dto) {
		EntityManager manager = null;
		EntityTransaction transaction = null;
		HashMap<String, Object> responMap = new HashMap<String, Object>();
		boolean flag = true;
		boolean weftFlag = true;
		try {

			String mattcompanyid = Common.checkNullAndEmpty(dto.getId()) ? dto.getId() : null;
			manager = emf.createEntityManager();
			String desc = CommonEnum.SAVE_SUCCESS.getValue();

			if (manager.isOpen() && manager != null) {
				transaction = manager.getTransaction();
				if (transaction != null) {

					transaction.begin();
					if (Common.checkNullAndEmpty(mattcompanyid)) {/* Edit */
						desc = CommonEnum.UPDATE_SUCCESS.getValue();

						String updateQuery = "UPDATE MATTRESSCOMPANY SET COMPANY=:COMPANY,PRODUCTNAME=:PRODUCTNAME,SIZE=:SIZE,PRICE=:PRICE  WHERE MATTCOMPANYID = "
								+ mattcompanyid;
						manager.createNativeQuery(updateQuery).setParameter("COMPANY", dto.getCompany())
								.setParameter("PRODUCTNAME",
										Common.checkNullAndEmpty(dto.getProductName()) ? dto.getProductName() : null)
								.setParameter("SIZE", Common.checkNullAndEmpty(dto.getSize()) ? dto.getSize() : null)
								.setParameter("PRICE", Common.checkNullAndEmpty(dto.getPrice()) ? dto.getPrice() : null)
								.executeUpdate();

					} else { /* Insert */
						mattcompanyid = dbUtils.getMaxId("MATTRESSCOMPANY", "MATTCOMPANYID", null, manager);
						String insertquery = "INSERT INTO MATTRESSCOMPANY(MATTCOMPANYID, COMPANY, PRODUCTNAME, `SIZE`, PRICE) "
								+ "VALUES(:MATTCOMPANYID,:COMPANY,:PRODUCTNAME,:SIZE,:PRICE)";
						manager.createNativeQuery(insertquery)
								.setParameter("MATTCOMPANYID", Integer.parseInt(mattcompanyid))
								.setParameter("COMPANY", dto.getCompany())
								.setParameter("PRODUCTNAME",
										Common.checkNullAndEmpty(dto.getProductName()) ? dto.getProductName() : null)
								.setParameter("SIZE", Common.checkNullAndEmpty(dto.getSize()) ? dto.getSize() : null)
								.setParameter("PRICE", Common.checkNullAndEmpty(dto.getPrice()) ? dto.getPrice() : null)
								.executeUpdate();
					}

					transaction.commit();

					Common.fillResponseMap(responMap, false, CommonEnum.SUCCESS.getValue(), desc,
							ToastrAction.SUCCESS.getValue());
					return new ResponseEntity<>(responMap, HttpStatus.OK);

				} else {
					Common.fillResponseMap(responMap, true, CommonEnum.ERROR.getValue(),
							CommonEnum.UNABLE_TO_ACQUIRE_TRANSACTION.getValue(), ToastrAction.ERROR.getValue());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responMap);
				}
			}

		} catch (PersistenceException ex) {
			Common.throwException(ex, null, transaction, responMap, null, emf, request);
		} catch (Exception e) {
			Common.throwException(null, e, transaction, responMap, null, emf, request);
		} finally {
			if (manager != null) {
				manager.clear();
				manager.close();
			}
		}

		Common.fillResponseMap(responMap, true, CommonEnum.ERROR.getValue(),
				CommonEnum.INTERNAL_SERVER_ERROR.getValue(), ToastrAction.ERROR.getValue());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responMap);
	}

	@Override
	public ResponseEntity<Object> getData(String mattcompanyid) {
		EntityManager manager = null;
		HashMap<String, Object> responseMap = new HashMap<>();
		try {
			manager = emf.createEntityManager();
			if (Common.checkNullAndEmpty(mattcompanyid)) {
				Session session = emf.createEntityManager().unwrap(Session.class);
				String query = "SELECT * FROM MATTRESSCOMPANY WHERE  MATTCOMPANYID = " + mattcompanyid;
				List<?> list = ListToJSONConverter.queryRunner(session, query);

				if (list.size() > 0) {
					responseMap.put("iserror", "N");
					responseMap.put("data", list);
					return ResponseEntity.ok(responseMap);
				} else {
					Common.fillResponseMap(responseMap, true, CommonEnum.ERROR.getValue(),
							CommonEnum.NO_DATA_FOUND.getValue(), ToastrAction.ERROR.getValue());
					return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
				}
			} else {
				Common.fillResponseMap(responseMap, true, CommonEnum.ERROR.getValue(),
						CommonEnum.INVALID_USER.getValue(), ToastrAction.ERROR.getValue());
				return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.error("{}", e);
		} finally {
			if (manager != null) {
				manager.clear();
				manager.close();
			}
		}
		Common.fillResponseMap(responseMap, true, CommonEnum.ERROR.getValue(),
				CommonEnum.ERROR_OCCURED_WHILE_RETRIWING_DATA.getValue(), ToastrAction.ERROR.getValue());
		return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
	}

	@Override
	public <T> Object deleteData(String mattcompanyid) {
		EntityManager manager = null;
		EntityTransaction transaction = null;
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			mattcompanyid = Common.checkNullAndEmpty(mattcompanyid) ? (mattcompanyid) : null;
			manager = emf.createEntityManager();
			transaction = manager.getTransaction();
			if (transaction != null) {

				transaction.begin();
				String deleteQuery = "DELETE FROM MATTRESSCOMPANY WHERE MATTCOMPANYID = " + mattcompanyid;
				manager.createNativeQuery(deleteQuery).executeUpdate();
				transaction.commit();

				Common.fillResponseMap(map, false, CommonEnum.SUCCESS.getValue(), CommonEnum.DELETE_SUCCESS.getValue(),
						ToastrAction.SUCCESS.getValue());
				return ResponseEntity.ok(map);

			} else {
				logger.error(CommonEnum.UNABLE_TO_ACQUIRE_TRANSACTION.getValue());
				Common.fillResponseMap(map, true, CommonEnum.ERROR.getValue(),
						CommonEnum.ERROR_OCCURED_WHILE_DELETING_DATA.getValue(), ToastrAction.ERROR.getValue());
				return ResponseEntity.ok(map);
			}

		} catch (PersistenceException ex) {
			ex.printStackTrace();
			Common.throwException(ex, null, transaction, map, null, emf, request);
		} catch (Exception e) {
			e.printStackTrace();
			Common.throwException(null, e, transaction, map, null, emf, request);
		} finally {
			if (null != manager) {
				manager.clear();
				manager.close();
			}
		}
		Common.fillResponseMap(map, true, CommonEnum.ERROR.getValue(), CommonEnum.INTERNAL_SERVER_ERROR.getValue(),
				ToastrAction.ERROR.getValue());
		return ResponseEntity.ok(map);

	}
}
