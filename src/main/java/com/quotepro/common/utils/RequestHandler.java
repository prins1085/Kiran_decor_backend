package com.quotepro.common.utils;

import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.quotepro.common.enumeration.CommonEnum;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class RequestHandler extends Thread {
	public static final Logger loger = LoggerFactory.getLogger(RequestHandler.class);
	private EntityManagerFactory emf = null;
	private Socket socket;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	RequestHandler(EntityManagerFactory emf) {
		this.emf = emf;
	}

	RequestHandler(Socket socket, EntityManagerFactory emf) {
		this.socket = socket;
		this.emf = emf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		EntityManager manager = null;
		EntityTransaction transaction = null;
		try {
			// Get input and output streams
			manager = emf.createEntityManager();
			HashMap<String, Object> responMap = new HashMap<String, Object>();
			if (manager.isOpen() && manager != null) {
				transaction = manager.getTransaction();
				if (transaction != null) {
					String redDataText = "";
					try {
						// Read Response
						int red = -1;
						byte[] buffer = new byte[1024]; // a read buffer of 5KiB
						byte[] redData;
						if ((red = socket.getInputStream().read(buffer)) != -1) {
							redData = new byte[red];
							System.arraycopy(buffer, 0, redData, 0, red);
							redDataText = redDataText + new String(redData); // assumption that client sends data UTF-8
																				// encoded
							System.out.println("redDataText1 = " + redDataText);
						}
					} catch (Throwable ex) {
						loger.error("Time Service Response :" + ex);
						redDataText = "";
					}
//            System.out.println("redDataText = " + redDataText);
					try {
						PrintWriter out = new PrintWriter(socket.getOutputStream());
						if (redDataText.contains("GT")) {
							out.println("{" + Const.getDTime() + "}");
							out.flush();
							try {
								Map mm = new HashMap();
								mm.put("rtime", redDataText);
								transaction.begin();
								loger.info("RequestHandler : " + redDataText);
								manager.createNativeQuery("INSERT INTO RESETTIME(RTIME) VALUES (:rtime)")
										.setParameter("rtime", redDataText).executeUpdate();
								transaction.commit();
							} catch (PersistenceException e) {
								Common.throwException(e, null, transaction, responMap, null, emf, request);
							} catch (Exception ex) {
								loger.error("rTime :" + ex);
								Common.throwException(null, ex, transaction, responMap, null, emf, request);
							} finally {
								if (manager.isOpen() && manager != null) {
									manager.clear();
									manager.close();
								}
							}
						} else {
							out.println("");
							out.flush();
						}
						out.close();

					} catch (Throwable e) {
						loger.error("Time Request Run : " + e);
					}
				} else {
					loger.error(CommonEnum.UNABLE_TO_ACQUIRE_TRANSACTION.getValue());
				}
			}

			socket.close();
		} catch (Throwable e) {
			loger.error("Time Request Run 1: " + e);
		} finally {
			if (manager.isOpen() && manager != null) {
				manager.clear();
				manager.close();
			}
		}

	}
}
