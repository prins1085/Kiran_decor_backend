package com.quotepro.invoice;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

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

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<Object> rpt() {
		HashMap<String, Object> responseMap = new HashMap<>();
		HashMap parameters = new HashMap();
		try {
			// Define the path to the JRXML file
			String reportsDirPath = servletContext.getRealPath("/reports/") + "WJ";
			String reportPath = reportsDirPath + "/AAA.jrxml";
			String logopath = reportsDirPath + "/dd.jpeg";

			String query = " select main.customer_id,main.disc,main.qty,main.total,main.discount,main.customer_id,customer_name,mobile_number,architect_name  from ( "
					+ " select customer_id, concat( itemname  ,\"(curtain)\") as disc ,   count(curtainid) as qty,  SUM(total)  as total ,discount  from curtain group by customer_id union all  "
					+ "  select customer_id, concat( itemname  ,\"(blind)\"), 	count(blindid) as qty , SUM(total) as total ,discount from blind group by customer_id union all  "
					+ "   select customer_id, concat( itemname  ,\"(mattress)\"),count(mattressid) as qty, SUM(total) as total ,discount from mattress group by customer_id union all  "
					+ "    select customer_id, concat( itemname  ,\" (sofa)\"),  count(sofaid) as qty, SUM(total) as total , 0 from sofa  group by customer_id ) as main left join customers c on main.customer_id = c.customer_id where c.customer_id = 1";
			parameters.put("SQuery", query);
			parameters.put("logopath", logopath);
			// Load the JRXML file

			JasperReport compiledReport = JasperCompileManager.compileReport(reportPath);
			Connection connection = dataSource.getConnection();
			byte[] bytes = JasperRunManager.runReportToPdf(compiledReport, parameters, connection);
			response.setContentType("application/pdf");
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

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<Object> rptTAIL(String customer_id) {
		HashMap<String, Object> responseMap = new HashMap<>();
		HashMap parameters = new HashMap();
		try {
			// Define the path to the JRXML file
			String reportsDirPath = servletContext.getRealPath("/reports/") + "WJ";
			String reportPath = reportsDirPath + "/TAILRPT.jrxml";
			String logopath = reportsDirPath + "/dd.jpeg";

			String query = "select description as disc, qty, rate , total, discount , final_total, customer_name , mobile_number ,architect_name from invoice_items ii left join customers c on c.customer_id = ii.customer_id where ii.customer_id = "+customer_id+" and `type` = \"TAIL\"";
			
			parameters.put("SQuery", query);
			parameters.put("logopath", logopath);
			// Load the JRXML file

			JasperReport compiledReport = JasperCompileManager.compileReport(reportPath);
			Connection connection = dataSource.getConnection();
			byte[] bytes = JasperRunManager.runReportToPdf(compiledReport, parameters, connection);
			response.setContentType("application/pdf");
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
