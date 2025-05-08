package com.quotepro.invoice;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Base64;
import java.util.HashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quotepro.common.utils.DBUtils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;

@Service
public class RptServiceImpl implements RptService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private DBUtils dbUtils;

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<PdfResponse> rpt(String customer_id) {
		try {
			String reportsDirPath = servletContext.getRealPath("/reports/") + "WJ";
			String logopath = reportsDirPath + "/dd.jpeg";

			String cname = dbUtils.getSingleStringDataNoEntityManager(
					"select customer_name from customers where customer_id = " + customer_id);

			PdfResponse response = new PdfResponse();
			response.setTailFileName(cname + "_TAIL.pdf");
			response.setFabFileName(cname + "_FAB.pdf");

			// Generate TAIL PDF
			byte[] tailBytes = generatePdf(customer_id, reportsDirPath, "TAIL", logopath);
			response.setTailPdfBase64(Base64.getEncoder().encodeToString(tailBytes));

			// Generate FAB PDF
			byte[] fabBytes = generatePdf(customer_id, reportsDirPath, "FAB", logopath);
			response.setFabPdfBase64(Base64.getEncoder().encodeToString(fabBytes));

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Error generating reports: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	private byte[] generatePdf(String customerId, String reportsDirPath, String reportType, String logopath)
			throws Exception {
		String reportPath = reportsDirPath + "/" + reportType + "RPT.jrxml";
		String query = "SELECT @srno := @srno + 1 AS srno, description AS disc, qty, rate, total, discount, final_total, "
				+ "customer_name, mobile_number, architect_name FROM (SELECT @srno := 0) AS init, invoice_items ii "
				+ "LEFT JOIN customers c ON c.customer_id = ii.customer_id " + "WHERE ii.customer_id = " + customerId
				+ " AND `type` = '" + reportType + "'";

		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put("SQuery", query);
		parameters.put("logopath", logopath);

		JasperReport compiledReport = JasperCompileManager.compileReport(reportPath);
		Connection connection = dataSource.getConnection();
		return JasperRunManager.runReportToPdf(compiledReport, parameters, connection);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<Object> rptTAIL(String customer_id) {
		HashMap<String, Object> responseMap = new HashMap<>();
		HashMap parameters = new HashMap();
		try {
			// Define the path to the JRXML file
			String jrxmlFile = "reports/WJ/TAILRPT.jrxml";
			String logopath = "reports/WJ/dd.jpeg";
			
			InputStream reportStream = getClass().getClassLoader().getResourceAsStream(jrxmlFile);
			if (reportStream == null) {
			    throw new FileNotFoundException("Report file not found in resources: " + jrxmlFile);
			}
			
			//String reportsDirPath = servletContext.getRealPath("/reports/") + "WJ";
			//String reportPath = reportsDirPath + "/TAILRPT.jrxml";
			//String logopath = reportsDirPath + "/dd.jpeg";
			
			String type = "TAIL";
			String query = "select  @srno := @srno + 1 AS srno,description as disc, qty, rate , total, discount , final_total, customer_name , mobile_number ,architect_name from (SELECT @srno := 0) AS init,invoice_items ii left join customers c on c.customer_id = ii.customer_id where ii.customer_id = "
					+ customer_id + " and `type` = '"+type+"'" ;
			String cname = dbUtils.getSingleStringDataNoEntityManager(
					"select customer_name  from customers c where customer_id = " + customer_id);

			parameters.put("SQuery", query);
			parameters.put("logopath", getClass().getClassLoader().getResource("reports/WJ/dd.jpeg").getPath());
			//parameters.put("logopath", logopath);
			// Load the JRXML file

			JasperReport compiledReport = JasperCompileManager.compileReport(reportStream);
			Connection connection = dataSource.getConnection();
			byte[] bytes = JasperRunManager.runReportToPdf(compiledReport, parameters, connection);
			response.setContentType("application/pdf");
			String fileName = cname + "_" + "TAIL.pdf";
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes, 0, bytes.length);
			responseMap.put("response", response);

			return new ResponseEntity<>(responseMap, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error generating report: {}", e.getMessage(), e);
			responseMap.put("error", "Failed to generate report: " + e.getMessage());
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// =========================FAB======================//
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<Object> rptFAB(String customer_id) {
		HashMap<String, Object> responseMap = new HashMap<>();
		HashMap parameters = new HashMap();
		try {
			// Define the path to the JRXML file
//			String reportsDirPath = servletContext.getRealPath("/reports/") + "WJ";
//			String reportPath = reportsDirPath + "/FABRPT.jrxml";
//			String logopath = reportsDirPath + "/dd.jpeg";
			String jrxmlFile = "reports/WJ/TAILRPT.jrxml";
			String logopath = "reports/WJ/dd.jpeg";
			
			InputStream reportStream = getClass().getClassLoader().getResourceAsStream(jrxmlFile);
			if (reportStream == null) {
			    throw new FileNotFoundException("Report file not found in resources: " + jrxmlFile);
			}
			
			String type = "FAB";
			String query = "select  @srno := @srno + 1 AS srno,description as disc, qty, rate , total, discount , final_total, customer_name , mobile_number ,architect_name from (SELECT @srno := 0) AS init,invoice_items ii left join customers c on c.customer_id = ii.customer_id where ii.customer_id = "
					+ customer_id + " and `type` =  '"+type+"'" ;
			String cname = dbUtils.getSingleStringDataNoEntityManager(
					"select customer_name  from customers c where customer_id = " + customer_id);

			parameters.put("SQuery", query);
			parameters.put("logopath", getClass().getClassLoader().getResource("reports/WJ/dd.jpeg").getPath());
			
			//parameters.put("logopath", logopath);
			// Load the JRXML file

			JasperReport compiledReport = JasperCompileManager.compileReport(reportStream);
			Connection connection = dataSource.getConnection();
			byte[] bytes = JasperRunManager.runReportToPdf(compiledReport, parameters, connection);
			response.setContentType("application/pdf");
			String fileName = cname + "_" + "FAB.pdf";
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes, 0, bytes.length);
			responseMap.put("response", response);

			return new ResponseEntity<>(responseMap, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error generating report: {}", e.getMessage(), e);
			responseMap.put("error", "Failed to generate report: " + e.getMessage());
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
