package com.quotepro.invoice;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/invoice")
@CrossOrigin
public interface RptService {
	
	@PostMapping
	ResponseEntity<Object> rpt(@RequestBody String jsonData); // Excel And PDF 
	
	
}
