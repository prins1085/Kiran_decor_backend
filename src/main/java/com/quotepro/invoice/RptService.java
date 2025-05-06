package com.quotepro.invoice;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/invoice")
@CrossOrigin
public interface RptService {
	
	@PostMapping
	ResponseEntity<Object> rpt(); // Excel And PDF 
	
	@PostMapping("/TAIL")
	ResponseEntity<Object> rptTAIL(@RequestParam("customer_id") String customer_id); // Excel And PDF 
	
	
}
