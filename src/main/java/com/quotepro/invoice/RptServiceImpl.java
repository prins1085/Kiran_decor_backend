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
	public ResponseEntity<Object> rpt(String jsonData) {
		HashMap<String, Object> responseMap = new HashMap<>();
		HashMap parameters = new HashMap();
		try {
			// Define the path to the JRXML file
			String reportsDirPath = servletContext.getRealPath("/reports/") + "WJ";
			String reportPath = reportsDirPath + "/AAA.jrxml";
			String logopath = reportsDirPath + "/dd.jpeg";

			// Parse the JSON input
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> jsonMap = objectMapper.readValue(jsonData, Map.class);

			// Extract fields from the JSON (handle missing fields gracefully)
			String orderNo = (String) jsonMap.getOrDefault("order_no", "2001");
			String date = (String) jsonMap.getOrDefault("date", "N/A");
			Map<String, Object> customer = (Map<String, Object>) jsonMap.getOrDefault("customer", new HashMap<>());
			String executive = (String) jsonMap.getOrDefault("executive", "N/A");
			Map<String, Object> company = (Map<String, Object>) jsonMap.getOrDefault("company", new HashMap<>());
			List<Map<String, Object>> items = (List<Map<String, Object>>) jsonMap.getOrDefault("items",
					new ArrayList<>());
			// String totalAmount = (String) jsonMap.getOrDefault("total_amount", "N/A");
			Map<String, Object> bankDetails = (Map<String, Object>) jsonMap.getOrDefault("bank_details",
					new HashMap<>());

			// Flatten the data
			List<Map<String, Object>> flattenedData = new ArrayList<>();
			for (Map<String, Object> item : items) {
				Map<String, Object> flatItem = new HashMap<>();
				flatItem.put("order_no", orderNo);
				flatItem.put("date", date);
				flatItem.put("customer_name", customer.getOrDefault("name", "N/A"));
				flatItem.put("customer_address", customer.getOrDefault("address", "N/A"));
				flatItem.put("executive", executive);
				// flatItem.put("total_amount", totalAmount);
				flatItem.putAll(item); // Add item-specific fields
				flatItem.put("bank_bank", bankDetails.getOrDefault("bank", "N/A"));
				flatItem.put("bank_account_no", bankDetails.getOrDefault("account_no", "N/A"));
				flatItem.put("bank_ifsc_code", bankDetails.getOrDefault("ifsc_code", "N/A"));
				flatItem.put("bank_branch", bankDetails.getOrDefault("branch", "N/A"));
				flattenedData.add(flatItem);
			}
			
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
			
//			InputStream jasperStream = new FileInputStream(reportPath);
//			JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);
//
//			// Fill the report with the flattened data
//			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(flattenedData);
//			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
//
//			// Export to PDF
//			// Export to PDF
//			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
//			response.setContentType("application/pdf");
//			response.setContentLength(bytes.length);
//
//			// Determine whether to display inline or force download
//			String displayMode = "attachment"; // Change to "inline" if you want to display in the browser
//			String fileName = "report_" + orderNo + ".pdf";
//			response.setHeader("Content-Disposition", displayMode + "; filename=" + fileName);
//
//			// Write the PDF bytes to the response output stream
//			response.getOutputStream().write(bytes, 0, bytes.length);
//			response.getOutputStream().flush();

			return new ResponseEntity<>(responseMap, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error generating report: {}", e.getMessage(), e);
			responseMap.put("error", "Failed to generate report: " + e.getMessage());
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
